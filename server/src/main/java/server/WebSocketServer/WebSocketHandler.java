package server.WebSocketServer;

import dataAccess.DataAccessException;
import services.WebSocketService;
import services.JoinGameService;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.ServerMessages.ServerMessage;
import webSocketMessages.ServerMessages.ServerMessage.ServerMessageType;

import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {

    // so the way this datastructures works is that in this map, you have a list of maps, each element representing one game
    // currently being played. the Id is the key and the map inside is the user and their authToken plus their session
    private static final HashMap<Integer,ConnectionManager> connections = new HashMap<>();

    public WebSocketHandler(){}

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
    // FIXME: This needs to have some more development and you need to pass around sessions

        try {
            GsonConverter gsonConverter = new GsonConverter();
            var output = gsonConverter.jsonToObj(message, UserGameCommand.class);
            UserGameCommand userCommand = (UserGameCommand)output;

            // before we do anything, lets get the username, authToken and gameId we are going to use
            String userAuth = userCommand.getAuthString();
            int userGameId = userCommand.getGameId();

            // checks for authTokens to make sure you can make a valid move
            WebSocketService webSer = new WebSocketService();
            // String username = webSer.getAuthUsername(userAuth);

            String username = "hello";


            var command = userCommand.getCommandType();

            // here you would put some sort of switch statement that map each server message to a particular value
            switch (command) {
                case CommandType.JOIN_PLAYER -> joinGame(username,userGameId,session);
                case CommandType.JOIN_OBSERVER -> observeGame(username,userGameId,session);
                case CommandType.MAKE_MOVE -> makeMove(username,userGameId,session);
                case CommandType.RESIGN -> resign(userAuth,username,userGameId);
                case CommandType.LEAVE -> leave();
            }

        }catch(JsonSyntaxException error){
            System.err.println("Broken");
        }
        /*
        catch(DataAccessException InvalidAuth){
            System.err.println("throw error here");
        }
         */
    }

    private void joinGame(String username, int gameId, Session session){

        // TIP: makes a new connection if it is already not in the database
        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager());
        }

        // gets the game manager in the connections map
        var gameManager = connections.get(gameId);

        // checks to see if the user is already in the game
        if(gameManager.containsUser(username)){
            System.err.println("print an error here");
        }

        // FIXME: be sure to make some better messages so the server knows what to do
        ServerMessage serverMessage = new ServerMessage(ServerMessageType.LOAD_GAME,"Load client board");
        sendMessage(session, serverMessage);

        // broadcasts message to everyone playing the game
        gameManager.addSession(username,session);
        String message = username + " has joined the game";
        gameManager.broadcast(message,username,true);

    }

    private void observeGame(String username, int gameId, Session session){

        // TIP: makes a new connection if it is already not in the database
        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager());
        }

        // gets the game manager in the connections map
        var gameManager = connections.get(gameId);

        // checks to see if the user is already in the game
        if(gameManager.containsUser(username)){
            System.err.println("print an error here");
        }

        gameManager.addSession(username,session);
        String message = username + " is now observing the game";
        gameManager.broadcast(message,username,true);

        // send a message back to the client


    }

    private void makeMove(String username, int gameId, Session session){
        System.out.println("Making move");
    }

    private void resign(String authToken, String username, int gameId){
        // throws an error because there should be a manager here
        if(!connections.containsKey(gameId)){
            System.err.println("Error here, can't resign if there is no game being played here");
        }

        // gets the game manager in the connections map
        var gameManager = connections.get(gameId);

        // checks to see if the user is already in the game
        if(gameManager.containsUser(username)){
            System.err.println("print an error here");
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
            System.err.println("Error here, not valid user info");
        }
    }

    private void leave(){
        System.out.println("leaving game");
    }

    // sending messages back to the client
    private void sendMessage(Session session, ServerMessage message){

        GsonConverter gsonConverter = new GsonConverter();
        String output = gsonConverter.objToJson(message);

        try {
            session.getRemote().sendString(output);
        }catch(IOException error){
            System.err.println("error here");
        }
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



    // just for testing, be sure to get rid of this once you are done
    public static void main(String [] args){

    }
}
