package server.WebSocketServer;


import ConvertToGson.GsonConverter;
import chess.ChessGame;
import dataAccess.DataAccessException;
import models.Game;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.ServerMessages.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class makes sure that people can enter and exit games appropriately, you can leave join games and this class makes
 * sure everything is updated. Also will send broadcasts to everyone in a given connection manager
 *
 * make a class that allows you leave a game
 * make a class that allows you to join a game as player OR an observer (might need to create an enum)
 * broadcasts to everyone or some players given the parameters
 *
 */

public class ConnectionHandler {

    private static final HashMap<Integer,ConnectionManager> connections = new HashMap<>();
    private static final GsonConverter serializer = new GsonConverter();

    public ConnectionHandler(){}


    public void joinGame(Session mySession, Game game, int gameId, String myUsername, ChessGame.TeamColor myColor)throws DataAccessException {

        String notification = "";

        if(myColor == ChessGame.TeamColor.WATCH){
            connectToSession(mySession,gameId,myUsername);
            notification = myUsername + "is now observing the game";

        }else if(myColor == ChessGame.TeamColor.WHITE){
            if(game.isWhiteTaken() && game.getWhiteUsername().equals(myUsername)){
                connectToSession(mySession,gameId,myUsername);
                notification = myUsername + "is now playing as white";
            }else{
                throw new DataAccessException("ERROR: Cannot join white side",400);
            }

        }else if(myColor == ChessGame.TeamColor.BLACK){
            if(game.isBlackTaken() && game.getBlackUsername().equals(myUsername)){
                connectToSession(mySession,gameId,myUsername);
                notification = myUsername + "is now playing as black";
            }else{
                throw new DataAccessException("ERROR: Cannot join black side",400);
            }

        }else{ // for resign or any other type of weird entry
            throw new DataAccessException("ERROR: Bad Request",400);

        }
        sendNotificationBroadcast(gameId,notification,true,myUsername);
    }

    public void leaveGame(String username, int gameId, Game game, ChessGame.TeamColor color) throws DataAccessException{

        ConnectionManager allSessions = connections.get(gameId);

        // checks to see if they are still in the session
        if(!allSessions.isInSession(username)){
            throw new DataAccessException(("ERROR: " + username + " is not in the session!"),400);
        }

        if(color == ChessGame.TeamColor.WHITE){
            game.leaveWhiteSide();

        }else if(color == ChessGame.TeamColor.BLACK){
            game.leaveBlackSide();
        }

        allSessions.removeSession(username);

        if(allSessions.isEmpty()){
            connections.remove(gameId);
        }else{
            sendNotificationBroadcast(gameId,(username + " has left the game!"),true,username);
        }
    }

    public void sendNotification(Session session, String strMessage){

        NotificationMessage message = new NotificationMessage(strMessage);
        String data = serializer.objToJson(message);

        try{
            sendMessage(session,data);
        }catch(IOException ignored){ // just doesn't send anything if it fails
        }

    }

    // returns if was successful in connecting to the session
    private void connectToSession(Session session,int gameId,String username)throws DataAccessException{

        if(!connections.containsKey(gameId)){
            connections.put(gameId,new ConnectionManager()); // now we make then a new connection manager
        }

        ConnectionManager gameManager = connections.get(gameId);

        try {
            gameManager.safeConnect(session,username);
        }catch(IOException error){
            throw new DataAccessException("ERROR: Failure to join game",500);
        }
    }

    public void sendUpdate(Session session,Game game){

        String strGame = serializer.objToJson(game);
        LoadGameMessage update = new LoadGameMessage(strGame);
        String data = serializer.objToJson(update);

        try{
            sendMessage(session,data);
        }catch(IOException ignored){
        }
    }

    public void sendError(Session session,String message, int errCode){
        ErrorMessage error = new ErrorMessage(message,errCode);
        String data = serializer.objToJson(error);

        try{
            sendMessage(session,data);
        }catch(IOException ignored){
        }
    }

    private void sendMessage(Session session, String data)throws IOException{
        session.getRemote().sendString(data);
    }

    public void sendNotificationBroadcast(int gameId,String message,
                                          boolean isExclusive,String exclusiveUser){
        sendBroadcast(gameId,message,false,isExclusive,exclusiveUser);
    }

    public void sendUpdateBroadcast(int gameId,Game game,
                                    boolean isExclusive,String exclusiveUser){
        String strGame = serializer.objToJson(game);
        sendBroadcast(gameId,strGame,true,isExclusive,exclusiveUser);
    }

    private void sendBroadcast(int gameId,String data,boolean isUpdate,
                               boolean isExclusive, String exclusiveUser) {
        var gameManager = connections.get(gameId);
        gameManager.broadcast(data,exclusiveUser,isExclusive,isUpdate);
    }

    public void clear(){
        connections.clear();
    }

    // helper functions to help out with the database

}






