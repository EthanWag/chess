package WebSocketClient;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
import models.Game;
import ui.GameScreenUI;
import webSocketMessages.ServerMessages.*;
import webSocketMessages.ServerMessages.Error;
import webSocketMessages.ServerMessages.ServerMessage;

public class ServerMessageHandler {

    private final GsonConverter serializer = new GsonConverter();

    private final GameScreenUI playerScreen;

    public ServerMessageHandler(GameScreenUI screen) {
        playerScreen = screen;
    }

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

    private void handleError(String message){

        var objMessage = serializer.jsonToObj(message, Error.class);
        Error err = (Error)objMessage;

        String errMessage = err.getErrorMessage();

        playerScreen.print(errMessage);

    }

    private void handleLoadBoard(String message){

        var objMessage = serializer.jsonToObj(message,LoadGameMessage.class);
        LoadGameMessage userGameObj = (LoadGameMessage)objMessage;

        var strGame = serializer.jsonToObj(userGameObj.getGame(), Game.class);
        Game game = (Game)strGame;

        playerScreen.drawBoard(game.getGame().getBoard());

    }

    private void handleNotification(String message){

        var objMessage = serializer.jsonToObj(message,NotificationMessage.class);
        NotificationMessage userNotification = (NotificationMessage) objMessage;

        playerScreen.print(userNotification.getMessage());
    }
}
