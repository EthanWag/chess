package webSocketMessages.userCommands;

import chess.ChessGame;
import webSocketMessages.ServerMessages.ServerMessage;

import java.util.Objects;

public class JoinPlayerMessage extends UserGameCommand {

    private final ChessGame.TeamColor playerColor;

    public JoinPlayerMessage(String myAuthToken,int myGameId,ChessGame.TeamColor playerColor){
        super(UserGameCommand.CommandType.JOIN_PLAYER,myAuthToken,myGameId);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor(){
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinPlayerMessage that = (JoinPlayerMessage) o;
        return Objects.equals(playerColor, that.playerColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerColor.hashCode());
    }
}
