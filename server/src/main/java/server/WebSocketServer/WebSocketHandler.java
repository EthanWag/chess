package server.WebSocketServer;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(){}

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
    // FIXME: This needs to have some more development and you need to pass around sessions

        try {
            GsonConverter gsonConverter = new GsonConverter();
            var output = gsonConverter.jsonToObj(message, UserGameCommand.class);
            UserGameCommand userCommand = (UserGameCommand)output;

            var command = userCommand.getCommandType();

            // here you would put some sort of switch statement that map each server message to a particular value
            switch (command) {
                case CommandType.JOIN_PLAYER -> joinGame();
                case CommandType.JOIN_OBSERVER -> observeGame();
                case CommandType.MAKE_MOVE -> makeMove();
                case CommandType.RESIGN -> resign();
                case CommandType.LEAVE -> leave();
            }

        }catch(JsonSyntaxException error){
            System.err.println("Broken");
        }
    }

    private void joinGame(){
        System.out.println("joining game");

        /**
         * TODO
         * when a player joins a game, it needs to first update the game and database and put the user into the game.
         * your going to want to have authTokens and usernames and plan for what happens if it is invalid or a bad username
         * - throw errors when it does not make sense
         *
         * then your going to want to send a message to everyone in the session that a user has joined, use broadcast
         * user the connection manager to make sure you add them to the correct spot
         *  - similarly, throw errors when needed
         */
    }

    private void observeGame(){
        System.out.println("Observing game");
    }

    private void makeMove(){
        System.out.println("Making move");
    }

    private void resign(){
        System.out.println("Resign game");
    }

    private void leave(){
        System.out.println("leaving game");
    }





    // just for testing, be sure to get rid of this once you are done
    public static void main(String [] args){

    }


}
