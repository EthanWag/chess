package server.WebSocketServer;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {

    private String username;
    private Session session;

    // possibly add a authToken variable here
    public Connection(String myUsername, Session mySession){
        username = myUsername;
        session = mySession;
    }

    // sends a message to that user
    public void send(String message)throws IOException{
        try {
            session.getRemote().sendString(message);
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
