package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorBishop extends SharedCalculator{

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position){
        return allowDiagonal(board,color,position);
    }

}
