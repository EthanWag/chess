package WebSocketClient;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import webSocketMessages.ServerMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

import static java.lang.Thread.sleep;

public class WebSocketFacade extends Endpoint{

    // might cause errors for threads,
    private final GsonConverter serializer = new GsonConverter();

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
                    handleMessage(message);
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
    public void joinGame(String authToken,int gameId){

        try {
            var serverCmd = new UserGameCommand(authToken,gameId);
            serverCmd.setCommandType(CommandType.JOIN_PLAYER);

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
            var serverCmd = new UserGameCommand(authToken,gameId);
            serverCmd.setCommandType(CommandType.JOIN_OBSERVER);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }

    // make move function
    public void makeMove(String authToken,int gameId){
        try {
            var serverCmd = new UserGameCommand(authToken,gameId);
            serverCmd.setCommandType(CommandType.MAKE_MOVE);

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
            var serverCmd = new UserGameCommand(authToken,gameId);
            serverCmd.setCommandType(CommandType.LEAVE);

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
            var serverCmd = new UserGameCommand(authToken,gameId);
            serverCmd.setCommandType(CommandType.RESIGN);

            String jsonCmd = serializer.objToJson(serverCmd);

            // we will make a user connection object here
            session.getBasicRemote().sendText(jsonCmd);

            // I'll have some more code here that process that connection

        }catch(IOException error){
            System.err.println("Put error here");
        }
    }


    // these functions help receive messages from the server and handle them appropriately
    private void handleMessage(String strMessage){

        try{
            Object objMessage = serializer.jsonToObj(strMessage,ServerMessage.class);
            ServerMessage message = (ServerMessage) objMessage;

            switch(message.getServerMessageType()){
                case LOAD_GAME -> handleLoadBoard();
                case ERROR -> handleError();
                case NOTIFICATION -> handleNotification();
            }

        }catch(JsonSyntaxException jsonError){
            System.err.println("error in making obj");
        }
    }

    // different kinds of messages that we can be passed
    private void handleError(){
        System.out.println("handling error");
    }

    private void handleLoadBoard(){
        System.out.println("loading board");
    }

    private void handleNotification(){
        System.out.println("handling notification");
    }


    public static void main(String[]args){
        WebSocketFacade test = new WebSocketFacade("http://localhost:8080");
        test.joinGame("abc123",1);

        try {
            sleep(3000);
        }catch(Exception e){
            System.out.println("invalid stuff");
        }

    }
}
