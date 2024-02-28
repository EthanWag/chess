package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorPawn {

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> chessMoves;

        if(color == ChessGame.TeamColor.WHITE){
            chessMoves = allowWhite(board,color,position,pawn);
        }else{
            chessMoves = allowBlack(board,color,position,pawn);
        }

        return chessMoves;
    }

    private static ArrayList<ChessMove> allowWhite(ChessBoard board, ChessGame.TeamColor color,
                                                   ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> newMoves = new ArrayList<>();
        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.up()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp == board.EMPTY){
                newPos.add(copyPos.deepCopy());

                if(copyPos.up()){
                    temp = board.getPiece(copyPos);
                    if((temp == board.EMPTY) && checkPawnStart(pawn,position)){
                        newPos.add(copyPos.deepCopy());
                    }
                }
            }
        }

        ChessPosition attackRight = position.deepCopy();
        ChessPosition attackLeft = position.deepCopy();

        // checks to see if it can attack right
        if(attackRight.upperRight()){
            ChessPiece temp = board.getPiece(attackRight);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    newPos.add(attackRight.deepCopy());
                }
            }
        }
        // checks to see if it can attack left
        if(attackLeft.upperLeft()){
            ChessPiece temp = board.getPiece(attackLeft);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    newPos.add(attackLeft.deepCopy());
                }
            }
        }


        for(ChessPosition pos : newPos){

            if(pos.getRow() == 8){

                promotionFill(newMoves,position,pos);

            }else{
                ChessMove newMove = new ChessMove(position,pos, null);
                newMoves.add(newMove);
            }
        }
        return newMoves;
    }


    private static ArrayList<ChessMove> allowBlack(ChessBoard board, ChessGame.TeamColor color,
                                                   ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> newMoves = new ArrayList<>();
        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.down()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp == board.EMPTY){
                newPos.add(copyPos.deepCopy());

                if(copyPos.down()){
                    temp = board.getPiece(copyPos);
                    if((temp == board.EMPTY) && checkPawnStart(pawn,position)){
                        newPos.add(copyPos.deepCopy());
                    }
                }
            }
        }

        ChessPosition attackRight = position.deepCopy();
        ChessPosition attackLeft = position.deepCopy();

        // checks to see if it can attack right
        if(attackRight.lowerRight()){
            ChessPiece temp = board.getPiece(attackRight);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    newPos.add(attackRight.deepCopy());
                }
            }
        }
        // checks to see if it can attack left
        if(attackLeft.lowerLeft()){
            ChessPiece temp = board.getPiece(attackLeft);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    newPos.add(attackLeft.deepCopy());
                }
            }
        }

        for(ChessPosition pos : newPos){

            if(pos.getRow() == 1){

                promotionFill(newMoves,position,pos);

            }else{
                ChessMove chessMove = new ChessMove(position,pos, null);
                newMoves.add(chessMove);
            }
        }
        return newMoves;
    }

    private static boolean checkPawnStart(ChessPiece piece, ChessPosition position){

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return ((position.getRow() == 2) && piece.getFirstMove());
        }else{
            return ((position.getRow() == 7) && piece.getFirstMove());
        }
    }

    private static void promotionFill(ArrayList<ChessMove> newMoves, ChessPosition startPosition,
                                                      ChessPosition endPosition){

        ChessMove newMove = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.QUEEN);
        newMoves.add(newMove);
        newMove = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.KNIGHT);
        newMoves.add(newMove);
        newMove = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.ROOK);
        newMoves.add(newMove);
        newMove = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.BISHOP);
        newMoves.add(newMove);

    }

}
