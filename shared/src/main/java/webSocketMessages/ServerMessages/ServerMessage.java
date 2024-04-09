package webSocketMessages.ServerMessages; // note you changed this, if this gives you issues, come here

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    protected ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    // possibly send the game over the websocket and then update the board


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerMessage that = (ServerMessage) o;
        return serverMessageType == that.serverMessageType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serverMessageType);
    }
}
