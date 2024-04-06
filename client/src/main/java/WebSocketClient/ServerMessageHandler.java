package WebSocketClient;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
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
                case LOAD_GAME -> handleLoadBoard();
                case ERROR -> handleError();
                case NOTIFICATION -> handleNotification();
            }

        }catch(JsonSyntaxException jsonError){
            System.err.println("error in making obj");
        }
    }

    // different kinds of messages that we can be passed

    // TODO: Go through these functions and do according what you need to do depending on the context
    private void handleError(){
        System.out.println("handling error");
    }

    private void handleLoadBoard(){
        System.out.println("loading board");
    }

    private void handleNotification(){
        System.out.println("handling notification");
    }

}
