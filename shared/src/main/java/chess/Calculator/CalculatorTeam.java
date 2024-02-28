package chess.Calculator;

import java.util.ArrayList;
import chess.*;

public class CalculatorTeam {

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color){

        ArrayList<ChessMove> allMoves;


        allMoves = findTeamMoves(board,color);

        return allMoves;
    }


    // finds all the moves that a team can make on the board
    private static ArrayList<ChessMove> findTeamMoves(ChessBoard board, ChessGame.TeamColor color){

        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for(int row = 1; row <= ChessBoard.BOUNDRIES; row++){
            for(int col = 1; col <= ChessBoard.BOUNDRIES; col++){

                // creates the position that we will be looking at
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);

                // checks to see if piece is empty and if it is not, it will also grab the color
                if((piece != ChessBoard.EMPTY) && (piece.getTeamColor() == color)){

                    // find the pieces the move can make
                    allMoves.addAll(piece.pieceMoves(board,pos));
                }
            // otherwise continues and moves onto the next piece in the array
            }
        }

        return allMoves;
    }
}
