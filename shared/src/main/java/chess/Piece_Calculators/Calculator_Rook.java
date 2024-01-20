package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;
public class Calculator_Rook {

    public static ArrayList<ChessMove> findRookMoves(ChessBoard myBoard, ChessPosition myPosition, ChessGame.TeamColor myColor){

        ArrayList<ChessMove> rook_moves = allowVertical(myBoard,myPosition,myColor);
        return rook_moves;
    }

    private static ArrayList<ChessMove> allowVertical(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        // checks all up positions and gives possible moves
        while(copyPos.up()){
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

        // checks all right positions and gives possible moves
        while(copyPos.right()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all down positions and gives possible moves
        while(copyPos.down()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all left positions and gives possible moves
        while(copyPos.left()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }

        // finally creates all moves the a piece can make
        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }
        // makes all the chess moves and returns the list
        return moves;
    }
}
