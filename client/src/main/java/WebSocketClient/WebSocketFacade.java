package WebSocketClient;

import ConvertToGson.GsonConverter;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import webSocketMessages.ServerMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint{

    // might cause errors for threads,
    private static GsonConverter serializer = new GsonConverter();

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
                @Override
                public void onMessage(String message) { // on a message when you recive it, go here to figure out what to do
                    // we will figure this out soon enough
                }
            });

        }catch(Exception error){
            System.out.println("error");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }



    // I want to add all the functions that you can do to communicate with the server
    // TODO: these functions are completely up for redesign, I'm just trying to write down my train of thought
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

    public static void main(String[]args){
        WebSocketFacade test = new WebSocketFacade("http://localhost:8080");
        test.joinGame("abc123",1);
    }
}
