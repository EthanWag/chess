package server.WebSocketServer;

import java.util.HashMap;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class ConnectionManager {

    private final HashMap<String,Connection> serverConnections = new HashMap<>();

    public ConnectionManager(){}

    public void addSession(String username,Session mySession){

        var newConnection = new Connection(username, mySession);
        serverConnections.put(username,newConnection);

    }
    public void removeSession(String username){
        serverConnections.remove(username);
    }

    // this also allows for you to give a message to everyone, and exclude one user
    public void broadcast(String message, String exclusiveUser, boolean isExclusive){

        try {

            for (Connection con : serverConnections.values()) {

                var curSession = con.getSession();

                // handles people just disappearing out of nowhere
                if (curSession.isOpen()) {

                    if (isExclusive && (exclusiveUser.equals(con.getUsername()))){
                        continue; // just continues if it is the exclusive user
                    }

                    con.send(message);

                } else {
                    // FIXME: come back here to fix potential bugs, editing a data structure while looping
                    curSession.close();
                    serverConnections.remove(con.getUsername());
                }
            }


        }catch(IOException error){
            return;
        }

    }


    public boolean containsUser(String username){
        return serverConnections.containsKey(username);
    }

    public boolean isEmpty(){
        return serverConnections.isEmpty();
    }
}
