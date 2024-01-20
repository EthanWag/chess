package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;
public class Calculator_King {

    public static ArrayList<ChessMove> findKingMoves(ChessBoard myBoard, ChessPosition myPosition, ChessGame.TeamColor myColor){

        ArrayList<ChessMove> king_moves = allowKing(myBoard, myPosition, myColor);
        return king_moves;
    }

    private static ArrayList<ChessMove> allowKing(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // adds all possible moves the king can make
        if(copyPos.up()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.upperRight()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.right()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.lowerRight()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.down()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.lowerLeft()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.left()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.upperLeft()){positions.add(copyPos.deepCopy());}


        // you may want to add to this function later on in the project, if that is the case, come back to
        // this location

        // this loops from the possible options and adds them if they are possible to make
        for(ChessPosition pos: positions){

            ChessPiece tempPiece = board.getPiece(pos);
            if (tempPiece != ChessBoard.EMPTY) {
                if(color != tempPiece.getTeamColor()){
                    ChessMove newMove = new ChessMove(position,pos,null);
                    moves.add(newMove);
                }
            }else{
                ChessMove newMove = new ChessMove(position,pos,null);
                moves.add(newMove);
            }
        }
        return moves;
    }

}
