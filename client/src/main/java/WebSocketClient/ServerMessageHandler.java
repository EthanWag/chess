package WebSocketClient;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
import models.Game;
import webSocketMessages.ServerMessages.*;
import webSocketMessages.ServerMessages.ServerMessage;

public class ServerMessageHandler {

    private final GsonConverter serializer = new GsonConverter();

    public ServerMessageHandler() {}

    // these functions help receive messages from the server and handle them appropriately
    public void handleMessage(String strMessage){

        try{
            Object objMessage = serializer.jsonToObj(strMessage, ServerMessage.class);
            ServerMessage message = (ServerMessage) objMessage;

            switch(message.getServerMessageType()){
                case LOAD_GAME -> handleLoadBoard(strMessage);
                case ERROR -> handleError(strMessage);
                case NOTIFICATION -> handleNotification(strMessage);
            }

        }catch(JsonSyntaxException jsonError){
            System.err.println("error in making obj");
        }
    }

    // different kinds of messages that we can be passed

    // TODO: Go through these functions and do according what you need to do depending on the context
    private String handleError(String message){

        var objMessage = serializer.jsonToObj(message,ErrorMessage.class);
        ErrorMessage err = (ErrorMessage)objMessage;

        String errMessage = err.getMessage();

        System.out.println("got here");
        System.out.println(errMessage);

        return errMessage;
    }

    private void handleLoadBoard(String message){

        var objMessage = serializer.jsonToObj(message,LoadGameMessage.class);
        LoadGameMessage userGameObj = (LoadGameMessage)objMessage;

        var strGame = serializer.jsonToObj(userGameObj.getGame(), Game.class);
        Game game = (Game)strGame;

        System.out.println("got here");
        System.out.println(game);

    }

    private String handleNotification(String message){

        var objMessage = serializer.jsonToObj(message,NotificationMessage.class);
        NotificationMessage userNotification = (NotificationMessage) objMessage;

        System.out.println("got here");
        System.out.println(userNotification.getMessage());

        return userNotification.getMessage();
    }
}
