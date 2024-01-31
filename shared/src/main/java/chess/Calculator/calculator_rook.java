package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class calculator_rook {

    public static ArrayList<ChessMove> find_moves(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position){
        return allowStright(board,color,position);
    }

    private static ArrayList<ChessMove> allowStright(ChessBoard board, ChessGame.TeamColor color, ChessPosition position){

        ArrayList<ChessMove> new_moves = new ArrayList<>();
        ArrayList<ChessPosition> new_pos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        while(copyPos.up()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(copyPos.deepCopy());
                }
                break;
            }else{
                new_pos.add(copyPos.deepCopy());
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.right()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(copyPos.deepCopy());
                }
                break;
            }else{
                new_pos.add(copyPos.deepCopy());
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.down()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(copyPos.deepCopy());
                }
                break;
            }else{
                new_pos.add(copyPos.deepCopy());
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.left()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(copyPos.deepCopy());
                }
                break;
            }else{
                new_pos.add(copyPos.deepCopy());
            }
        }

        for(ChessPosition pos : new_pos){
            ChessMove new_move = new ChessMove(position,pos,null);
            new_moves.add(new_move);
        }
        return new_moves;
    }


}
