package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class calculator_pawn {

    public static ArrayList<ChessMove> find_moves(ChessBoard board, ChessGame.TeamColor color,
                                                  ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> new_moves;

        if(color == ChessGame.TeamColor.WHITE){
            new_moves = allowWhite(board,color,position,pawn);
        }else{
            new_moves = allowBlack(board,color,position,pawn);
        }

        return new_moves;
    }

    private static ArrayList<ChessMove> allowWhite(ChessBoard board, ChessGame.TeamColor color,
                                                   ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> new_moves = new ArrayList<>();
        ArrayList<ChessPosition> new_pos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.up()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp == board.EMPTY){
                new_pos.add(copyPos.deepCopy());

                if(copyPos.up()){
                    temp = board.getPiece(copyPos);
                    if((temp == board.EMPTY) && checkPawnStart(pawn,position)){
                        new_pos.add(copyPos.deepCopy());
                    }
                }
            }
        }

        ChessPosition attack_right = position.deepCopy();
        ChessPosition attack_left = position.deepCopy();

        // checks to see if it can attack right
        if(attack_right.upperRight()){
            ChessPiece temp = board.getPiece(attack_right);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(attack_right.deepCopy());
                }
            }
        }
        // checks to see if it can attack left
        if(attack_left.upperLeft()){
            ChessPiece temp = board.getPiece(attack_left);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(attack_left.deepCopy());
                }
            }
        }


        for(ChessPosition pos : new_pos){

            if(pos.getRow() == 8){
                ChessMove new_move = new ChessMove(position,pos, ChessPiece.PieceType.QUEEN);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.KNIGHT);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.ROOK);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.BISHOP);
                new_moves.add(new_move);
            }else{
                ChessMove new_move = new ChessMove(position,pos, null);
                new_moves.add(new_move);
            }
        }
        return new_moves;
    }


    private static ArrayList<ChessMove> allowBlack(ChessBoard board, ChessGame.TeamColor color,
                                                   ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> new_moves = new ArrayList<>();
        ArrayList<ChessPosition> new_pos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        if(copyPos.down()){
            ChessPiece temp = board.getPiece(copyPos);
            if(temp == board.EMPTY){
                new_pos.add(copyPos.deepCopy());

                if(copyPos.down()){
                    temp = board.getPiece(copyPos);
                    if((temp == board.EMPTY) && checkPawnStart(pawn,position)){
                        new_pos.add(copyPos.deepCopy());
                    }
                }
            }
        }

        ChessPosition attack_right = position.deepCopy();
        ChessPosition attack_left = position.deepCopy();

        // checks to see if it can attack right
        if(attack_right.lowerRight()){
            ChessPiece temp = board.getPiece(attack_right);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(attack_right.deepCopy());
                }
            }
        }
        // checks to see if it can attack left
        if(attack_left.lowerLeft()){
            ChessPiece temp = board.getPiece(attack_left);
            if(temp != board.EMPTY){
                if(temp.getTeamColor() != color){
                    new_pos.add(attack_left.deepCopy());
                }
            }
        }


        for(ChessPosition pos : new_pos){

            if(pos.getRow() == 1){
                ChessMove new_move = new ChessMove(position,pos, ChessPiece.PieceType.QUEEN);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.KNIGHT);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.ROOK);
                new_moves.add(new_move);
                new_move = new ChessMove(position,pos, ChessPiece.PieceType.BISHOP);
                new_moves.add(new_move);
            }else{
                ChessMove new_move = new ChessMove(position,pos, null);
                new_moves.add(new_move);
            }
        }
        return new_moves;
    }

    private static boolean checkPawnStart(ChessPiece piece, ChessPosition position){

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return ((position.getRow() == 2) && piece.getFirstMove());
        }else{
            return ((position.getRow() == 7) && piece.getFirstMove());
        }
    }
}
