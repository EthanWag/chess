package WebSocketClient;

import ConvertToGson.GsonConverter;
import chess.ChessGame;
import chess.ChessMove;
import ui.GameScreenUI;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

import static java.lang.Thread.sleep;

public class WebSocketFacade extends Endpoint{

    private final GsonConverter serializer = new GsonConverter();

    private final ServerMessageHandler messageHandler;

    private Session session;

    public WebSocketFacade(String url,GameScreenUI screenUI){

        messageHandler = new ServerMessageHandler(screenUI);

        try {

            String wsUrl = url.replace("http", "ws"); // changes the connection to a websocket connection
            URI sockURI = new URI(wsUrl + "/connect");

            System.out.println(sockURI);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,sockURI);


            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    messageHandler.handleMessage(message);
                }
            });

        }catch(Exception error){
            System.out.println("error");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    // these methods reach the server and send messages to it

    // join game method
    public void joinGame(String authToken,int gameId,String isWhite){

        ChessGame.TeamColor color;
        try {
            color = findColor(isWhite);
        }catch(Exception error){
            return;
        }

        try {
            var serverCmd = new JoinPlayerMessage(authToken,gameId,color);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    // observe game function
    public void observeGame(String authToken,int gameId){
        try {
            var serverCmd = new JoinObserverMessage(authToken,gameId);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    // make move function
    public void makeMove(String authToken, ChessMove move, int gameId){
        try {
            var serverCmd = new MakeMoveMessage(authToken,gameId,move);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    // leave the game, usually an observer
    public void leave(String authToken,int gameId){
        try {
            var serverCmd = new LeaveMessage(authToken,gameId);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    // resigns and gives up from someone playing the game
    public void resign(String authToken,int gameId){
        try {
            var serverCmd = new ResignMessage(authToken,gameId);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }


    private ChessGame.TeamColor findColor(String color)throws Exception{
        if(color.equalsIgnoreCase("white")){
            return ChessGame.TeamColor.WHITE;
        }else if(color.equalsIgnoreCase("black")){
            return ChessGame.TeamColor.BLACK;
        }else{
            throw new Exception("invalid color type");
        }
    }







}
