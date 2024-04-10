package WebSocketClient;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
import models.Game;
import ui.GameScreenUI;
import webSocketMessages.ServerMessages.*;
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

        var objMessage = serializer.jsonToObj(message,ErrorMessage.class);
        ErrorMessage err = (ErrorMessage)objMessage;

        String errMessage = err.getMessage();

        // FIXME: this is a temp solution that I just put here for testing
        playerScreen.print(errMessage);

    }

    private void handleLoadBoard(String message){

        var objMessage = serializer.jsonToObj(message,LoadGameMessage.class);
        LoadGameMessage userGameObj = (LoadGameMessage)objMessage;

        var strGame = serializer.jsonToObj(userGameObj.getGame(), Game.class);
        Game game = (Game)strGame;

        // FIXME: need to be able to how it should draw the board, rn always prints it as white
        playerScreen.drawBoard(true,game.getGame().getBoard());

    }

    private void handleNotification(String message){

        var objMessage = serializer.jsonToObj(message,NotificationMessage.class);
        NotificationMessage userNotification = (NotificationMessage) objMessage;

        playerScreen.print(userNotification.getMessage());
    }
}
