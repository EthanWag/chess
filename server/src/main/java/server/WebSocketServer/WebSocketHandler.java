package server.WebSocketServer;

import chess.*;
import dataAccess.DataAccessException;
import models.Game;
import services.WebSocketService;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.ServerMessages.*;
import webSocketMessages.ServerMessages.Error;
import webSocketMessages.ServerMessages.ServerMessage;

import webSocketMessages.userCommands.*;
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

    private final WebSocketService webSer = new WebSocketService();
    private static HashMap<Integer,ConnectionManager> connections = new HashMap<>();
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
            String username = webSer.getAuthUsername(userAuth);

            var command = userCommand.getCommandType();

            // here you would put some sort of switch statement that map each server message to a particular value
            switch (command) {
                case CommandType.JOIN_PLAYER:

                    joinGame(session,message,username);
                    break;

                case CommandType.JOIN_OBSERVER:

                    observeGame(session,message,username);
                    break;

                case CommandType.MAKE_MOVE:

                    makeMove(session,message,username);

                    break;

                case CommandType.RESIGN:

                    resign(session,message,username);

                    break;

                case CommandType.LEAVE:

                    var leaveMess = serializer.jsonToObj(message,LeaveMessage.class);
                    LeaveMessage userLeave = (LeaveMessage) leaveMess;
                    leave(session,username,userLeave.getGameID());

                    break;
            }

        }catch(JsonSyntaxException error) {
            sendError(session, "ERROR: Bad request", 400);
        }catch(DataAccessException InvalidAuth){
            sendError(session,"ERROR: Unauthorized",404);
        }
    }

    private void joinGame(Session session,String message,String username){

        // de-serialize the message into a language the join game function can understand
        var output = serializer.jsonToObj(message,JoinPlayerMessage.class);
        JoinPlayerMessage userCmd = (JoinPlayerMessage)output;

        int gameId = userCmd.getGameID();
        ChessGame.TeamColor color = userCmd.getPlayerColor();

        // this double checks to make sure you are getting the correct game and that it even exists
        Game myGame;
        try{
            myGame = findDBGame(gameId);
        }catch(DataAccessException error){
            sendError(session,"ERROR: Invalid game ID", 404);
            return;
        }
        // we can be 100% sure that after this line, that the game exists

        // now we want to see if they correctly can and are able to join the game
        if(!validGameConnect(myGame,username,color)){
            sendError(session, "ERROR: Bad request", 400);
            return;
        }

        // connects to server and if there are any problems, it then sends an error
        if(!connectToSession(session,gameId,username)){
            sendError(session, "ERROR: Bad request", 400);
            return;
        }

        String strGame = serializer.objToJson(myGame);

        // sends message to everyone saying that they have joined the game
        ServerMessage serverMessage = new LoadGameMessage(strGame);
        sendMessage(session, serverMessage);

        // broadcasts message to everyone playing the game
        sendGameBroadcast(username + " has joined the game",gameId,username,true,false);

    }

    // lets a player watch a game if they so wish
    private void observeGame(Session session, String message, String username){

        var output = serializer.jsonToObj(message,JoinObserverMessage.class);
        JoinObserverMessage userCmd = (JoinObserverMessage) output;

        int gameId = userCmd.getGameID();

        // checks to see if the game even exists and that it can be joined
        Game myGame;
        try{
            myGame = findDBGame(gameId);
        }catch(DataAccessException error){
            sendError(session,"ERROR: Invalid game ID", 404);
            return;
        }

        // connects to server and if there are any problems, it then sends an error
        if(!connectToSession(session,gameId,username)){
            sendError(session, "ERROR: Bad request", 400);
            return;
        }

        String strGame = serializer.objToJson(myGame);

        // sends a message to the client as well as all those in playing the game
        ServerMessage serverMessage = new LoadGameMessage(strGame);
        sendMessage(session, serverMessage);

        // sends everyone a message that in the game
        sendGameBroadcast(username + " is now observing the game",gameId,username,true,false);
    }

    private void makeMove(Session session, String message, String username){

        var output = serializer.jsonToObj(message,MakeMoveMessage.class);
        MakeMoveMessage userCmd = (MakeMoveMessage) output;

        int gameId = userCmd.getGameID();
        ChessMove userMove = userCmd.getMove();

        // checks to see if the game exists
        Game myGame;
        try{
            myGame = findDBGame(gameId);
        }catch(DataAccessException error){
            sendError(session,"ERROR: Invalid game ID", 404);
            return;
        }

        // right here we need to make it so
        ChessGame.TeamColor color;
        try{
            color = myGame.getColor(username);
        }catch(Exception error){
            sendError(session,"ERROR: Bad color", 400);
            return;
        }

        // checks to make sure that you can the game hasn't ended
        if(myGame.getGame().getTeamTurn() == ChessGame.TeamColor.RESIGN){
            sendError(session,"Error: Game Already ended", 400);
            return;
        }

        // checks to see if anyone is in checkmate
        if(myGame.getGame().isInCheckmate(ChessGame.TeamColor.WHITE) || myGame.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)){
            sendError(session,"Error: Game Already ended", 400);
            return;
        }

        // makes the move and updates the board
        try {
            makeValidMove(myGame, userMove,color);
            webSer.updateGame(gameId,myGame);

            String strGame = serializer.objToJson(myGame);

            sendGameBroadcast(strGame,gameId,"",false,true);
            sendGameBroadcast(username + "Moved!",gameId,username,true,false);

        }catch(InvalidMoveException error){
            sendError(session,error.getMessage(), 500);
        }catch(DataAccessException error){
            sendError(session,"Unable to update board", 500);
        }
    }

    private void resign(Session session, String message, String username){

        var output = serializer.jsonToObj(message,ResignMessage.class);
        ResignMessage userMessage = (ResignMessage)output;

        int gameId = userMessage.getGameID();

        // first makes sure that they can resign
        try{
            var game = findDBGame(gameId);
            game.getColor(username); // this just checks to make sure they are even a user

            if(game.getGame().getTeamTurn() == ChessGame.TeamColor.RESIGN){
                sendError(session,"Error: game already ended", 400);
                return;
            }

            // ends the game and then lets everyone know that they lost
            game.getGame().endGame();

            WebSocketService webSer = new WebSocketService();
            webSer.updateGame(gameId,game);

            sendGameBroadcast(username + " has lost the game!!", gameId,"",false,false);

        }catch(DataAccessException error){
            sendError(session,"Error: gameID not in database",404);
        }catch(IOException error){
            sendError(session,"Error: Not a player",400);
        }

    }

    private void leave(Session session, String username, int gameId) {

        sendGameBroadcast((username + "has left the game"),gameId,username,true,false);
        leaveSession(gameId, username);
        session.close();
        allLeft(gameId);

    }


    // sending messages back to the client
    private void sendMessage(Session session, ServerMessage message){

        String output = serializer.objToJson(message);

        try {
            session.getRemote().sendString(output);
        }catch(IOException error){
            return;
        }
    }



    private boolean connectToSession(Session session,int gameId,String username){

        // if this condition is true, we know that this is the first person to join this game
        // so we make then a connection manager
        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager()); // now we make then a new connection manager
        }

        ConnectionManager gameManager = connections.get(gameId);

        try {
            gameManager.safeConnect(session,username);
        }catch(IOException error){
            return false;
        }
        return true;
    }

    private void leaveSession(int gameId, String username){
        if(!connections.containsKey(gameId)){
            return;
        }

        var gameManager = connections.get(gameId);
        gameManager.removeSession(username);
    }

    private void sendGameBroadcast(String message, int gameId,String exclusiveUser, boolean isExclusive, boolean isUpdate){
        var gameManager = connections.get(gameId);
        gameManager.broadcast(message,exclusiveUser,isExclusive,isUpdate);
    }

    private boolean validGameConnect(Game game, String username,ChessGame.TeamColor playerColor){
        return switch (playerColor) {
            case ChessGame.TeamColor.WHITE -> game.isWhiteTaken() && game.getWhiteUsername().equals(username);
            case ChessGame.TeamColor.BLACK -> game.isBlackTaken() && game.getBlackUsername().equals(username);
            default -> false;
        };
    }

    private void sendError(Session session, String strMessage,int code){
        ServerMessage message = new Error(strMessage,code);
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

    private void makeValidMove(Game game, ChessMove move, ChessGame.TeamColor color)throws InvalidMoveException{

        ChessGame chessGame = game.getGame();

        // first checks to see if your making the correct move when you start
        if(chessGame.getTeamTurn() != color){
            throw new InvalidMoveException("Not player turn");
        }

        if(chessGame.isInCheckmate(color)){
            throw new InvalidMoveException("Player in Checkmate");
        }

        var startPosition = move.getStartPosition();
        var possibleMoves = chessGame.validMoves(startPosition);

        if(possibleMoves.contains(move)){
            chessGame.makeMove(move);

        }else{
            throw new InvalidMoveException("unable to make move");
        }
    }

    private Game findDBGame(int gameId) throws DataAccessException{
        try{
            var webSer = new WebSocketService();
            return webSer.getGameDb(gameId);
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR:Game Not Found", 404);
        }
    }

    public void clearManager(){
        connections.clear();
    }

}
