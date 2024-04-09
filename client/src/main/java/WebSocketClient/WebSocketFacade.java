package WebSocketClient;

import ConvertToGson.GsonConverter;
import chess.ChessMove;
import webSocketMessages.userCommands.*;
import webSocketMessages.ServerMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

import static java.lang.Thread.sleep;

public class WebSocketFacade extends Endpoint{

    private final GsonConverter serializer = new GsonConverter();

    private final ServerMessageHandler messageHandler = new ServerMessageHandler();

    private Session session;

    public WebSocketFacade(String url){

        try {

            String wsUrl = url.replace("http", "ws"); // changes the connection to a websocket connection
            URI sockURI = new URI(wsUrl + "/connect");

            System.out.println(sockURI);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,sockURI);

            // from here I need more research
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
    public void joinGame(String authToken,int gameId,boolean isWhite){

        try {
            var serverCmd = new JoinPlayerMessage(authToken,gameId,isWhite);

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













    public static void main(String[]args){
        WebSocketFacade test = new WebSocketFacade("http://localhost:8080");
        test.joinGame("6733b021-92fa-48a4-8ce8-49e74e220d3b",1,true);

        WebSocketFacade test2 = new WebSocketFacade("http://localhost:8080");
        test2.observeGame("0d19368e-545e-436c-8609-600faecb34bd",1);

        try {
            while(true) {
                sleep(1);
            }
        }catch(Exception e){
            System.out.println("invalid stuff");
        }

    }
}
