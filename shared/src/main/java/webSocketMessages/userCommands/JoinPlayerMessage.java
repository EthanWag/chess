package webSocketMessages.userCommands;

import webSocketMessages.ServerMessages.ServerMessage;

import java.util.Objects;

public class JoinPlayerMessage extends UserGameCommand {

    private final boolean isWhite;

    public JoinPlayerMessage(String myAuthToken,int myGameId,boolean myColor){
        super(UserGameCommand.CommandType.JOIN_PLAYER,myAuthToken,myGameId);
        isWhite = myColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinPlayerMessage that = (JoinPlayerMessage) o;
        return isWhite == that.isWhite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isWhite);
    }
}
