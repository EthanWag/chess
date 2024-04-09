package server.WebSocketServer;

import dataAccess.DataAccessException;
import models.Game;
import services.WebSocketService;
import services.JoinGameService;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.ServerMessages.*;
import webSocketMessages.ServerMessages.ServerMessage;

import webSocketMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;

import static java.lang.System.exit;

@WebSocket
public class WebSocketHandler {

    // so the way this datastructures works is that in this map, you have a list of maps, each element representing one game
    // currently being played. the Id is the key and the map inside is the user and their authToken plus their session
    private static final HashMap<Integer,ConnectionManager> connections = new HashMap<>();
    private final GsonConverter serializer = new GsonConverter();

    public WebSocketHandler(){}

    @OnWebSocketMessage
    public void onMessage(Session session, String message){

        try {
            var output = serializer.jsonToObj(message,UserGameCommand.class);
            UserGameCommand userCommand = (UserGameCommand)output;

            // before we do anything, lets get the username, authToken and gameId we are going to use
            String userAuth = userCommand.getAuthString();

            // checks for authTokens to make sure you can make a valid move
            WebSocketService webSer = new WebSocketService();
            String username = webSer.getAuthUsername(userAuth);


            var command = userCommand.getCommandType();

            // here you would put some sort of switch statement that map each server message to a particular value
            switch (command) {
                case CommandType.JOIN_PLAYER:

                    var joinMess = serializer.jsonToObj(message,JoinPlayerMessage.class);
                    JoinPlayerMessage join = (JoinPlayerMessage)joinMess;
                    joinGame(session,username,join.getGameId());

                    break;

                case CommandType.JOIN_OBSERVER:

                    var oberMess = serializer.jsonToObj(message,JoinObserverMessage.class);
                    JoinObserverMessage observe = (JoinObserverMessage) oberMess;
                    observeGame(session,username,observe.getGameId());

                    break;

                case CommandType.MAKE_MOVE:

                    var moveMess = serializer.jsonToObj(message,MakeMoveMessage.class);
                    MakeMoveMessage move = (MakeMoveMessage) moveMess;
                    makeMove(session,username,move.getGameId());

                    break;

                case CommandType.RESIGN:

                    var resMess = serializer.jsonToObj(message,ResignMessage.class);
                    ResignMessage userResign = (ResignMessage) resMess;
                    resign(session,username,userAuth,userResign.getGameId());

                    break;

                case CommandType.LEAVE:

                    var leaveMess = serializer.jsonToObj(message,LeaveMessage.class);
                    LeaveMessage userLeave = (LeaveMessage) leaveMess;
                    leave(session,username,userLeave.getGameId());

                    break;
            }

        }catch(JsonSyntaxException error) {
            sendError(session, "ERROR: Bad request", 400);
        }catch(DataAccessException InvalidAuth){
            sendError(session,"ERROR: Unauthorized",404);
        }
    }

    private void joinGame(Session session, String username,int gameId){

        // if this is the first person in the database, it adds them
        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager());
        }

        // gets the correct ConnectionManager
        ConnectionManager gameManager = validateUser(session,gameId,username);

        // if error is detected, it just ends the program
        if(gameManager == null){
            return;
        }

        // get the game right here

        var myGame = getGame(session,gameId);
        if(myGame == null){
            return;
        }

        String strGame = serializer.objToJson(myGame);

        ServerMessage serverMessage = new LoadGameMessage(strGame);
        sendMessage(session, serverMessage);

        // broadcasts message to everyone playing the game
        gameManager.addSession(username,session);
        String message = username + " has joined the game";
        gameManager.broadcast(message,username,true);

    }

    private void observeGame(Session session, String username, int gameId){

        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager());
        }

        ConnectionManager gameManager = validateUser(session,gameId,username);

        if(gameManager == null){
            return;
        }

        var myGame = getGame(session,gameId);
        if(myGame == null){
            return;
        }

        String strGame = serializer.objToJson(myGame);

        // sends a message to the client as well as all those in playing the game
        ServerMessage serverMessage = new LoadGameMessage(strGame);
        sendMessage(session, serverMessage);

        gameManager.addSession(username,session);
        String message = username + " is now observing the game";
        gameManager.broadcast(message,username,true);
    }

    private void makeMove(Session session, String username,int gameId){
        System.out.println("Making move");
    }

    private void resign(Session session, String authToken, String username,int gameId){

        // first grabs the correct connection
        var gameManager = validateUser(session,gameId,username);

        // checks to see if stuff changed if it didn't it throws an error
        if(gameManager == null){
            return;
        }
        try {

            // removes them from the database
            JoinGameService exitSer = new JoinGameService();
            exitSer.deletePlayer(authToken, username, gameId);

            // now we need to move them from the connections
            gameManager.removeSession(username);

            // deletes the map if there is nobody left in the session
            allLeft(gameId);

        }catch(DataAccessException error){
            sendError(session,"ERROR: Invalid user data",400);
        }
    }

    private void leave(Session session, String username, int gameId){

        var gameManager = validateUser(session,gameId,username);

        if(gameManager == null){
            return;
        }

        // should remove the user from the session and that is about it
        gameManager.removeSession(username);
        allLeft(gameId);

    }

    // sending messages back to the client
    private void sendMessage(Session session, ServerMessage message){

        String output = serializer.objToJson(message);

        try {
            session.getRemote().sendString(output);
        }catch(IOException error){
            return; // just gives up if it can't be sent over the connection
        }
    }

    private ConnectionManager validateUser(Session session, int gameId, String username){
        if(!connections.containsKey(gameId)){
            sendError(session,"ERROR: Game does not exist",400);
            return null;
        }

        // gets the game manager in the connections map
        ConnectionManager gameManager = connections.get(gameId);

        // checks to see if the user is already in the game
        if(gameManager.containsUser(username)){
            sendError(session,"ERROR: User is already ",401);
            return null;
        }
        return gameManager;
    }

    private void sendError(Session session, String strMessage,int code){
        ServerMessage message = new ErrorMessage(strMessage,code);
        sendMessage(session,message);
    }

    // helper functions for the handler
    private void allLeft(int gameId){
        if(connections.containsKey(gameId)){
            var gameManager = connections.get(gameId);

            if(gameManager.isEmpty()){
                connections.remove(gameId);
            }
        }
    }

    private Game getGame(Session session, int gameId){
        try{
            var webSer = new WebSocketService();
            return webSer.getGame(gameId);
        }catch(DataAccessException error){
            sendError(session,"ERROR: Invalid game ID", 404);
            return null;
        }
    }

}
