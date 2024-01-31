package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class calculator_knight {

    public static ArrayList<ChessMove> find_moves(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position){

        return allowKnight(board,color,position);
    }

    private static ArrayList<ChessMove> allowKnight(ChessBoard board, ChessGame.TeamColor color,
                                                    ChessPosition position){

        ArrayList<ChessMove> new_moves = new ArrayList<>();
        ArrayList<ChessPosition> new_pos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        // checks up
        if(copyPos.up()){
            ChessPosition right = copyPos.deepCopy();
            ChessPosition left = copyPos.deepCopy();

            if(right.upperRight()){
                ChessPiece temp = board.getPiece(right);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(right.deepCopy());
                    }
                }else{
                    new_pos.add(right.deepCopy());
                }
            }
            // checks the left
            if(left.upperLeft()){
                ChessPiece temp = board.getPiece(left);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(left.deepCopy());
                    }
                }else{
                    new_pos.add(left.deepCopy());
                }
            }
        }
        copyPos = position.deepCopy();

        // checks to the right
        if(copyPos.right()){
            ChessPosition up = copyPos.deepCopy();
            ChessPosition down = copyPos.deepCopy();

            if(up.upperRight()){
                ChessPiece temp = board.getPiece(up);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(up.deepCopy());
                    }
                }else{
                    new_pos.add(up.deepCopy());
                }
            }
            // checks the left
            if(down.lowerRight()){
                ChessPiece temp = board.getPiece(down);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(down.deepCopy());
                    }
                }else{
                    new_pos.add(down.deepCopy());
                }
            }
        }
        copyPos = position.deepCopy();


        // checks down
        if(copyPos.down()){
            ChessPosition right = copyPos.deepCopy();
            ChessPosition left = copyPos.deepCopy();

            if(right.lowerRight()){
                ChessPiece temp = board.getPiece(right);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(right.deepCopy());
                    }
                }else{
                    new_pos.add(right.deepCopy());
                }
            }
            // checks the left
            if(left.lowerLeft()){
                ChessPiece temp = board.getPiece(left);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(left.deepCopy());
                    }
                }else{
                    new_pos.add(left.deepCopy());
                }
            }
        }
        copyPos = position.deepCopy();


        if(copyPos.left()){
            ChessPosition up = copyPos.deepCopy();
            ChessPosition down = copyPos.deepCopy();

            if(up.upperLeft()){
                ChessPiece temp = board.getPiece(up);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(up.deepCopy());
                    }
                }else{
                    new_pos.add(up.deepCopy());
                }
            }
            // checks the left
            if(down.lowerLeft()){
                ChessPiece temp = board.getPiece(down);
                if(temp != board.EMPTY){
                    if(temp.getTeamColor() != color){
                        new_pos.add(down.deepCopy());
                    }
                }else{
                    new_pos.add(down.deepCopy());
                }
            }
        }

        for(ChessPosition pos : new_pos){
            ChessMove new_move = new ChessMove(position,pos,null);
            new_moves.add(new_move);
        }
        return new_moves;
    }

}
