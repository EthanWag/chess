package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;
public class Calculator_Bishop {

    public static ArrayList<ChessMove> findBishopMoves(ChessBoard myBoard, ChessPosition myPosition, ChessGame.TeamColor myColor){

        ArrayList<ChessMove> bishop_moves = allowDiagonal(myBoard,myPosition,myColor);
        return bishop_moves;
    }

    private static ArrayList<ChessMove> allowDiagonal(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        // checks all positions in the upper right position of the current position
        while(copyPos.upperRight()){
            ChessPiece tempPiece = board.getPiece(copyPos); // should just be a pointer to checked piece
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy()); // takes the enemy piece if they are not the same color
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all positions in the lower right position of the current position
        while(copyPos.lowerRight()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all positions in the lower left position of the current position
        while(copyPos.lowerLeft()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all positions in the upper left position of the current position
        while(copyPos.upperLeft()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }

        // finally creates all the values
        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }

        // makes all the chess moves and returns the list
        return moves;
    }
}
