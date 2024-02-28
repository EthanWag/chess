package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorKing {

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position){
        return allowKing(board,color,position);
    }

    private static ArrayList<ChessMove> allowKing(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position){

        ArrayList<ChessMove> newMoves = new ArrayList<>();
        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.up()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.upperRight()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.right()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.lowerRight()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.down()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.lowerLeft()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.left()){ newPos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.upperLeft()){ newPos.add(copyPos.deepCopy()); }

        // checks to see if they are enemy pieces and adds them to moves accordingly
        for(ChessPosition pos : newPos){
            ChessPiece temp = board.getPiece(pos);

            if(temp != ChessBoard.EMPTY){
                if(temp.getTeamColor() != color){
                    ChessMove attackMove = new ChessMove(position,pos,null);
                    newMoves.add(attackMove);
                }
            }else{
                ChessMove normalMove = new ChessMove(position,pos,null);
                newMoves.add(normalMove);
            }
        }
        return newMoves;
    }

}
