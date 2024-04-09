package webSocketMessages.userCommands;
import chess.ChessMove;

import java.util.Objects;

public class MakeMoveMessage extends UserGameCommand{

    int gameId;
    private final ChessMove move;

    public MakeMoveMessage(String myAuthToken,int myGameId, ChessMove myMove){
        super(CommandType.MAKE_MOVE,myAuthToken,myGameId);
        move = myMove;
    }

    public ChessMove getMove(){
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MakeMoveMessage that = (MakeMoveMessage) o;
        return gameId == that.gameId && Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameId, move.hashCode());
    }
}
