package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;

public class Calculator_Knight {

    public static ArrayList<ChessMove> findKnightMoves(ChessBoard myBoard, ChessPosition myPosition, ChessGame.TeamColor myColor){

        ArrayList<ChessMove> knight_moves = allowKnight(myBoard,myPosition,myColor);
        return knight_moves;
    }

    private static ArrayList<ChessMove> allowKnight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        ArrayList<ChessMove> moves = new ArrayList<>();
        // this might create a lot of garbage so come back to this
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // checks to see if it can move up
        if(copyPos.up()){
            ChessPosition move_left = copyPos.deepCopy();
            ChessPosition move_right = copyPos.deepCopy();

            // checks both sides
            if(move_left.upperLeft()){
                ChessPiece tempPiece = board.getPiece(move_left);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_left.deepCopy());
                    }
                }else{
                    positions.add(move_left.deepCopy());
                }
            }

            if(move_right.upperRight()){
                ChessPiece tempPiece = board.getPiece(move_right);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_right.deepCopy());
                    }
                }else{
                    positions.add(move_right.deepCopy());
                }
            }
        }
        copyPos = position.deepCopy();

        if(copyPos.right()){
            ChessPosition move_up = copyPos.deepCopy();
            ChessPosition move_down = copyPos.deepCopy();

            // checks the left side from current position
            if(move_up.upperRight()){
                ChessPiece tempPiece = board.getPiece(move_up);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_up.deepCopy());
                    }
                }else{
                    positions.add(move_up.deepCopy());
                }
            }
            // checks the right side from current location
            if(move_down.lowerRight()){
                ChessPiece tempPiece = board.getPiece(move_down);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_down.deepCopy());
                    }
                }else{
                    positions.add(move_down.deepCopy());
                }
            }
        }

        copyPos = position.deepCopy();

        if(copyPos.down()){
            ChessPosition move_right = copyPos.deepCopy();
            ChessPosition move_left = copyPos.deepCopy();

            if(move_right.lowerRight()){
                ChessPiece tempPiece = board.getPiece(move_right);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_right.deepCopy());
                    }
                }else{
                    positions.add(move_right.deepCopy());
                }
            }
            if(move_left.lowerLeft()){
                ChessPiece tempPiece = board.getPiece(move_left);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_left.deepCopy());
                    }
                }else{
                    positions.add(move_left.deepCopy());
                }
            }
        }

        copyPos = position.deepCopy();

        if(copyPos.left()){
            ChessPosition move_up = copyPos.deepCopy();
            ChessPosition move_down = copyPos.deepCopy();

            if(move_up.upperLeft()){
                ChessPiece tempPiece = board.getPiece(move_up);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_up.deepCopy());
                    }
                }else{
                    positions.add(move_up.deepCopy());
                }
            }
            if(move_down.lowerLeft()){
                ChessPiece tempPiece = board.getPiece(move_down);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_down.deepCopy());
                    }
                }else{
                    positions.add(move_down.deepCopy());
                }
            }
        }

        // makes all the moves that a knight can make
        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
    }
}
