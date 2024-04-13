package server.WebSocketServer;

import java.util.HashMap;

import ConvertToGson.GsonConverter;
import dataAccess.DataAccessException;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import services.WebSocketService;

import java.io.IOException;
import java.util.Iterator;

public class ConnectionManager {

    private final HashMap<String,Connection> serverConnections = new HashMap<>();

    public ConnectionManager(){}

    public void removeSession(String username){
        serverConnections.remove(username);
    }

    // this also allows for you to give a message to everyone, and exclude one user
    public void broadcast(String message, String exclusiveUser, boolean isExclusive){

        try {

            Iterator<Connection> iterator = serverConnections.values().iterator();

            while (iterator.hasNext()) {
                Connection curConnection = iterator.next();
                Session curSession = curConnection.getSession();

                // handles people just disappearing out of nowhere
                if (curSession.isOpen()) {

                    if (isExclusive && (exclusiveUser.equals(curConnection.getUsername()))){
                        continue; // just continues if it is the exclusive user
                    }

                    curConnection.send(message);

                } else {
                    curSession.close();
                    iterator.remove();
                }
            }


        }catch(IOException error){
            return;
        }
    }

    public void updateEveryone(int gameId){

        Game myGame;
        try{

            WebSocketService webSer = new WebSocketService();
            myGame = webSer.getGame(gameId);

        }catch(DataAccessException ignored){
            return;
        }

        GsonConverter converter = new GsonConverter();
        String strGame = converter.objToJson(myGame);

        try {
            for (Connection con : serverConnections.values()) {

                var curSession = con.getSession();

                // handles people just disappearing out of nowhere
                if (curSession.isOpen()) {
                    con.sendUpdate(strGame);

                } else {
                    curSession.close();
                    serverConnections.remove(con.getUsername());
                }
            }

        }catch(IOException ignored){
        }
    }

    public void safeConnect(Session session, String username)throws IOException{
        // make sure they haven't already joined the game twice

        if(serverConnections.containsKey(username)){
            throw new IOException("ERROR: User Already joined");
        }

        // makes a new connection with the username already in it
        Connection newConnection = new Connection(username,session);

        // adds the new username and connection to te server connections
        serverConnections.put(username,newConnection);
    }

    public boolean containsUser(String username){
        return serverConnections.containsKey(username);
    }

    public boolean isEmpty(){
        return serverConnections.isEmpty();
    }
}
