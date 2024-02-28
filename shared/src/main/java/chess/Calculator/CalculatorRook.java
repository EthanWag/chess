package chess.Calculator;

import java.util.ArrayList;
import chess.*;

public class CalculatorRook extends SharedCalculator{

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position){
        return allowStright(board,color,position);
    }

}
