package chess.Piece_Calculators;

import chess.*;
import java.util.ArrayList;
public class Calculator_Pawn {

    public static ArrayList<ChessMove> findPawnMoves(ChessBoard myBoard, ChessPosition myPosition, ChessPiece myPawn){

        ArrayList<ChessMove> pawn_moves;

        if(myPawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            pawn_moves = allowPawnWhite(myBoard, myPosition, myPawn);
        }else{
            pawn_moves = allowPawnBlack(myBoard, myPosition, myPawn);
        }

        return pawn_moves;

    }

    private static ArrayList<ChessMove> allowPawnWhite(ChessBoard board, ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // This takes care of the moves up that a pawn can make
        if(copyPos.down() && (board.getPiece(copyPos) == ChessBoard.EMPTY)){
            positions.add(copyPos.deepCopy());

            // checks to see if it can go up two spaces
            if(copyPos.down() && (board.getPiece(copyPos) == ChessBoard.EMPTY) && pawn.pawnFirstMove(copyPos)){
                positions.add(copyPos.deepCopy());
            }
        }

        ChessPosition attack_left = position.deepCopy();
        ChessPosition attack_right = position.deepCopy();

        // first makes sure that it is even a legal move
        if(attack_left.lowerLeft()) {
            ChessPiece left_p = board.getPiece(attack_left);
            // then checks if there is a piece there, then checks to see if it can take it
            if((left_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != left_p.getTeamColor())) {
                positions.add(attack_left.deepCopy()); // if it can, it adds it as a possible move
            }
        }

        // Also checks to see if upperRight is a valid move, has the same logic as the left
        if(attack_right.lowerRight()) {
            ChessPiece right_p = board.getPiece(attack_right);
            if ((right_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != right_p.getTeamColor())) {
                positions.add(attack_right.deepCopy());
            }
        }
        // NOTE: need to program case where pawn can get promoted
        for(ChessPosition pos: positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
    }






    private static ArrayList<ChessMove> allowPawnBlack(ChessBoard board, ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // This takes care of the moves up that a pawn can make
        if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY)){
            positions.add(copyPos.deepCopy());

            // checks to see if it can go up two spaces
            if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY) && pawn.pawnFirstMove(copyPos)){
                positions.add(copyPos.deepCopy());
            }
        }

        ChessPosition attack_left = position.deepCopy();
        ChessPosition attack_right = position.deepCopy();

        // first makes sure that it is even a legal move
        if(attack_left.upperLeft()) {
            ChessPiece left_p = board.getPiece(attack_left);
            // then checks if there is a piece there, then checks to see if it can take it
            if((left_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != left_p.getTeamColor())) {
                positions.add(attack_left.deepCopy()); // if it can, it adds it as a possible move
            }
        }

        // Also checks to see if upperRight is a valid move, has the same logic as the left
        if(attack_right.upperRight()) {
            ChessPiece right_p = board.getPiece(attack_right);
            if ((right_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != right_p.getTeamColor())) {
                positions.add(attack_right.deepCopy());
            }
        }
        // NOTE: need to program case where pawn can get promoted
        for(ChessPosition pos: positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
    }

}
