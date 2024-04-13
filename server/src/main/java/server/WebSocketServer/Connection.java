package server.WebSocketServer;

import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

import ConvertToGson.GsonConverter;
import webSocketMessages.ServerMessages.LoadGameMessage;
import webSocketMessages.ServerMessages.NotificationMessage;

public class Connection {

    private final String username;
    private final Session session;
    private final GsonConverter serializer = new GsonConverter();

    // possibly add a authToken variable here
    public Connection(String myUsername, Session mySession){
        username = myUsername;
        session = mySession;
    }

    // sends a message to that user
    public void send(String message,boolean update)throws IOException{

        Object userMessage;
        if(update){
            userMessage = new LoadGameMessage(message);
        }else {
            userMessage = new NotificationMessage(message);
        }
        String gsonMessage = serializer.objToJson(userMessage);

        try {
            session.getRemote().sendString(gsonMessage);
        }catch(IOException error){
            throw new IOException("Error: Session is invalid");
        }
    }

    // simple getters and setters
    public String getUsername(){
        return username;
    }
    public Session getSession(){
        return session;
    }

}
