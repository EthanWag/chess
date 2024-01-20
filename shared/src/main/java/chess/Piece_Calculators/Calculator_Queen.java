package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;

public class Calculator_Queen {

    public static ArrayList<ChessMove> findQueenMoves(ChessBoard myBoard, ChessPosition myPosition, ChessGame.TeamColor myColor){

        // finds moves
        ArrayList<ChessMove> queen_moves = allowVertical(myBoard,myPosition,myColor);
        queen_moves.addAll(allowDiagonal(myBoard,myPosition,myColor));

        // returns all moves it can make
        return queen_moves;
    }

    // finds all moves vertically for the the Queen
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
