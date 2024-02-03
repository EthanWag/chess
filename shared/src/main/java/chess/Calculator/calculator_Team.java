package chess.Calculator;

import java.util.ArrayList;
import chess.*;

public class calculator_Team {

    public static ArrayList<ChessMove> find_moves(ChessBoard board, ChessGame.TeamColor color){

        ArrayList<ChessMove> all_moves;


        all_moves = find_team_moves(board,color);

        return all_moves;
    }


    // finds all the moves that a team can make on the board
    private static ArrayList<ChessMove> find_team_moves(ChessBoard board, ChessGame.TeamColor color){

        ArrayList<ChessMove> all_moves = new ArrayList<>();

        for(int row = 1; row <= ChessBoard.BOUNDRIES; row++){
            for(int col = 1; col <= ChessBoard.BOUNDRIES; col++){

                // creates the position that we will be looking at
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);

                // checks to see if piece is empty and if it is not, it will also grab the color
                if((piece != ChessBoard.EMPTY) && (piece.getTeamColor() == color)){

                    // find the pieces the move can make
                    all_moves.addAll(piece.pieceMoves(board,pos));
                }
            // otherwise continues and moves onto the next piece in the array
            }
        }

        return all_moves;
    }
}
