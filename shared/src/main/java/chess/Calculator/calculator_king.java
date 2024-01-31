package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class calculator_king {

    public static ArrayList<ChessMove> find_moves(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position){
        return allowKing(board,color,position);
    }


    private static ArrayList<ChessMove> allowKing(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position){

        ArrayList<ChessMove> new_moves = new ArrayList<>();
        ArrayList<ChessPosition> new_pos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.up()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.upperRight()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.right()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.lowerRight()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.down()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.lowerLeft()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.left()){ new_pos.add(copyPos.deepCopy()); }
        copyPos = position.deepCopy();

        if(copyPos.upperLeft()){ new_pos.add(copyPos.deepCopy()); }

        // checks to see if they are enemy pieces and adds them to moves accordingly
        for(ChessPosition pos : new_pos){
            ChessPiece temp = board.getPiece(pos);

            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    ChessMove attack_move = new ChessMove(position,pos,null);
                    new_moves.add(attack_move);
                }
            }else{
                ChessMove normal_move = new ChessMove(position,pos,null);
                new_moves.add(normal_move);
            }
        }
        return new_moves;
    }

}
