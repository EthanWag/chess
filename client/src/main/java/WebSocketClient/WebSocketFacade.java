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

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,sockURI);


            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
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
    public void joinGame(String authToken,int gameId,boolean isWhite){

        ChessGame.TeamColor color;
        if (isWhite) {
            color = ChessGame.TeamColor.WHITE;
        }else {
            color = ChessGame.TeamColor.BLACK;
        }

        try {
            var serverCmd = new JoinPlayerMessage(authToken,gameId,color);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);
        }catch(IOException error){
            System.err.println("Error: Unable to join game");
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

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    public void resign(String authToken,int gameId){
        try {
            var serverCmd = new LeaveMessage(authToken,gameId);
            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

}
