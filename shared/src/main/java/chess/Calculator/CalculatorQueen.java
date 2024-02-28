package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorQueen extends SharedCalculator{

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position){
        ArrayList<ChessMove> newMoves;

        newMoves = allowStright(board,color,position);
        newMoves.addAll(allowDiagonal(board,color,position));

        return newMoves;
    }

}
