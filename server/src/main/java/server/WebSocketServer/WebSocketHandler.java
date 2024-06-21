package server.WebSocketServer;

import chess.*;
import dataAccess.DataAccessException;
import models.Game;
import services.WebSocketService;

import webSocketMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

@WebSocket
public class WebSocketHandler {

    // so the way this datastructures works is that in this map, you have a list of maps, each element representing one game
    // currently being played. the Id is the key and the map inside is the user and their authToken plus their session

    private WebSocketService webSer;

    private final static ConnectionHandler manager = new ConnectionHandler();

    private final GsonConverter serializer = new GsonConverter();

    public WebSocketHandler()throws DataAccessException{
        try{
            webSer = new WebSocketService();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: unable to connect to database",500);
        }
    }


    // This seems fine, keeps this the same
    @OnWebSocketMessage
    public void onMessage(Session session, String message){

        try {
            var output = serializer.jsonToObj(message,UserGameCommand.class);

            UserGameCommand userCommand = (UserGameCommand)output;
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
                    leave(session,message,username);
                    break;
            }

        }catch(JsonSyntaxException error) {
            manager.sendError(session, "ERROR: Bad request", 400);
        }catch(DataAccessException InvalidAuth){
            manager.sendError(session,"ERROR: Unauthorized",404);
        }
    }

    private void joinGame(Session session,String message,String username){

        // de-serialize the message into a language the join game function can understand
        var output = serializer.jsonToObj(message,JoinPlayerMessage.class);
        JoinPlayerMessage userCmd = (JoinPlayerMessage)output;

        int gameId = userCmd.getGameID();
        ChessGame.TeamColor myColor = userCmd.getPlayerColor();

        try{

            Game myGame = findDBGame(gameId);
            manager.joinGame(session,myGame,gameId,username,myColor);
            manager.sendUpdate(session,myGame);

        }catch(DataAccessException error){
            switch(error.getErrorCode()){
                case 500 -> manager.sendError(session,"ERROR: Failure to connect",500);
                case 404 -> manager.sendError(session,"ERROR: Not found",404);
                case 400 -> manager.sendError(session,error.getMessage(),400);
                default -> manager.sendError(session,"ERROR: unknown error",502);
            }
        }

    }

    // lets a player watch a game if they so wish
    private void observeGame(Session session, String message, String username){

        // de-serialize the message into a language the join game function can understand
        var output = serializer.jsonToObj(message,JoinObserverMessage.class);
        JoinObserverMessage userCmd = (JoinObserverMessage)output;

        int gameId = userCmd.getGameID();

        try{

            Game myGame = findDBGame(gameId);
            manager.joinGame(session,myGame,gameId,username, ChessGame.TeamColor.WATCH);
            manager.sendUpdate(session,myGame);

        }catch(DataAccessException error){
            switch(error.getErrorCode()){
                case 500 -> manager.sendError(session,"ERROR: Failure to connect",500);
                case 404 -> manager.sendError(session,"ERROR: Game Not Found",404);
                case 400 -> manager.sendError(session,error.getMessage(),400);
                default -> manager.sendError(session,"ERROR: unknown error",502);
            }
        }
    }

    private void makeMove(Session session, String message, String username){

        var output = serializer.jsonToObj(message,MakeMoveMessage.class);
        MakeMoveMessage userCmd = (MakeMoveMessage) output;

        int gameId = userCmd.getGameID();
        ChessMove userMove = userCmd.getMove();

        // checks to see if the game exists
        try{
            Game myGame = findDBGame(gameId);

            MoveValidator.validMove(username,myGame,userMove);

            webSer.updateGame(gameId,myGame);

            manager.sendUpdateBroadcast(gameId,myGame,false,"");
            manager.sendNotificationBroadcast(gameId,(username + " moved!"), true,username);

        }catch(DataAccessException error){

            switch(error.getErrorCode()){ // other cases below, this includes a poor move made by the user
                case 404 -> manager.sendError(session,"ERROR: Game Not Found", 404);
                case 400 -> manager.sendError(session,"ERROR: Bad Move Made",400);
                case 500 -> manager.sendError(session,"ERROR: Unable to update board",500);
            }
        }
    }

    private void resign(Session session, String message, String username){

        var output = serializer.jsonToObj(message,ResignMessage.class);
        ResignMessage userMessage = (ResignMessage)output;

        int gameId = userMessage.getGameID();

        try{

            Game game = findDBGame(gameId); // gets game object
            ChessGame playerGame = game.getGame(); // gets the actual chess game

            // checks to see if they are a player in the game
            if(!isPlayer(game,username)){
                manager.sendError(session,"ERROR: Not a player", 400);
                return;
            }

            // checks to see if the game is already resigned
            if(playerGame.getTeamTurn() == ChessGame.TeamColor.RESIGN){
                manager.sendError(session, "ERROR: Game already resigned",400);
                return;
            }

            playerGame.endGame();
            webSer.updateGame(gameId,game);
            manager.sendNotificationBroadcast(gameId,(username + " has lost the game!!"),false,"");


        }catch(DataAccessException error){
            switch(error.getErrorCode()){
                case 404 -> manager.sendError(session,"ERROR: Game not found",404);
                case 500 -> manager.sendError(session, "ERROR: unable to update board",500);
            }
        }

    }

    private void leave(Session session, String message, String username) {

        var output = serializer.jsonToObj(message,LeaveMessage.class);
        LeaveMessage userMessage = (LeaveMessage)output;

        int gameId = userMessage.getGameID();

        try{
            Game game = findDBGame(gameId);
            ChessGame.TeamColor color = getColor(game,username);

            manager.leaveGame(username,gameId,game,color);
            session.close();

            // finally updates the database to reflect someone leaving
            webSer.leaveGame(gameId,color);

        }catch(DataAccessException error){
            switch(error.getErrorCode()){
                case 400 -> manager.sendError(session, "ERROR: Player not currently in game",400);
                case 404 -> manager.sendError(session, "ERROR: Game Not Found",404);
                case 500 -> manager.sendError(session, "ERROR: Server Game could not be updated",500);
            }
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

    private static boolean isPlayer(Game game,String username){
        if(game.isWhiteTaken() && (game.getWhiteUsername().equals(username))){
            return true;
        }else{
            return game.isBlackTaken() && (game.getBlackUsername().equals(username));
        }
    }

    // simple function that tells you if you white, black or watching. NOT SAVE
    private static ChessGame.TeamColor getColor(Game game, String username){
        if(isPlayer(game, username)){
            if((game.getWhiteUsername() != null) && game.getWhiteUsername().equals(username)){
                return ChessGame.TeamColor.WHITE;
            }else{
                return ChessGame.TeamColor.BLACK;
            }
        }else{
            return ChessGame.TeamColor.WATCH;
        }
    }

    public void clearManager(){
        manager.clear();
    }


    // Methods to make for chess 2.0

    /*
    - Make some new classes to handle websocket moves and or processes.
    - Also need to handle concurrency, maybe go through options that handle that better
    - Update sql statements so they handle concurrency better

    TODO UI:

    - Learn Javascript basics to design UI
    - make a nice website for you chess game

     */










}
