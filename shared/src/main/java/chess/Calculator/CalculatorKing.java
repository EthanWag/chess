package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorKing {

    private static final ChessMove NO_CASTLE = null;
    private static final ChessPosition NO_ROOK = null;


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

        ChessMove castle = castleKing(board,color,position);

        // checks to see if it can castle the king and if it can, it adds it to the move list
        if(castle != NO_CASTLE) newMoves.add(castle);

        return newMoves;
    }


    // keep in mind this is just the move for the king
    private static ChessMove castleKing(ChessBoard board, ChessGame.TeamColor color, ChessPosition pos){

        // first a couple checks
        ChessPiece king = board.getPiece(pos);

        // is it the kings first move
        if(!king.getFirstMove()) return NO_CASTLE;

        // is the rook in the correct place and is the same color
        ChessPosition rook = findRook(board,color,pos);
        if(rook == NO_ROOK) return NO_CASTLE;

        // is there nothing in between

        ChessPiece emptyLeft = board.getPiece(new ChessPosition(pos.getRow(),pos.getColumn() + 1));

        ChessPosition kingEnd = new ChessPosition(pos.getRow(),pos.getColumn() + 2);
        ChessPiece emptyRight = board.getPiece(kingEnd);

        if(emptyLeft == ChessBoard.EMPTY && emptyRight == ChessBoard.EMPTY){
            return new ChessMove(pos,kingEnd, ChessPiece.PieceType.CASTLE);
        }else{
            return NO_CASTLE;
        }
    }

    private static ChessPosition findRook(ChessBoard board, ChessGame.TeamColor color, ChessPosition pos){

        int rookRow = pos.getRow();
        int rookCol = pos.getColumn();
        ChessPosition rookPos;

        // checks to make sure your not going over the boarder of the board
        if(rookCol + 3 != 8) return NO_ROOK;
        rookPos = new ChessPosition(rookRow,rookCol + 3);

        ChessPiece piece = board.getPiece(rookPos);

        // is the space empty, if it is, then it cannot castle
        if(piece == ChessBoard.EMPTY) return NO_ROOK;

        // checks to see if it means qualifications
        if(!piece.getFirstMove() || (piece.getTeamColor() != color) || (piece.getPieceType() != ChessPiece.PieceType.ROOK)){
            return NO_ROOK;
        }else{
            return rookPos;
        }
    }

}
