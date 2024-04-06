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
    String message;
    int errCode;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type,String message,int code) {
        this.serverMessageType = type;
        this.message = message;
        this.errCode = code;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerMessage that = (ServerMessage) o;
        return errCode == that.errCode && serverMessageType == that.serverMessageType && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverMessageType, message, errCode);
    }
}
