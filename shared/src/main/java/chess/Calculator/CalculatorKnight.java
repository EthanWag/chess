package chess.Calculator;

import java.util.ArrayList;
import chess.*;
public class CalculatorKnight extends SharedCalculator{

    public static ArrayList<ChessMove> findMoves(ChessBoard board, ChessGame.TeamColor color,
                                                 ChessPosition position){

        return allowKnight(board,color,position);
    }

    private static ArrayList<ChessMove> allowKnight(ChessBoard board, ChessGame.TeamColor color,
                                                    ChessPosition position){

        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        // checks up
        if(copyPos.up()){
            ChessPosition right = copyPos.deepCopy();
            ChessPosition left = copyPos.deepCopy();

            if(right.upperRight()){
                finishedPos(board,color,newPos,right);
            }
            // checks the left
            if(left.upperLeft()){
                finishedPos(board,color,newPos,left);
            }
        }
        copyPos = position.deepCopy();

        // checks to the right
        if(copyPos.right()){
            ChessPosition up = copyPos.deepCopy();
            ChessPosition down = copyPos.deepCopy();

            if(up.upperRight()){
                finishedPos(board,color,newPos,up);
            }
            // checks the left
            if(down.lowerRight()){
                finishedPos(board, color, newPos, down);
            }
        }
        copyPos = position.deepCopy();


        // checks down
        if(copyPos.down()){
            ChessPosition right = copyPos.deepCopy();
            ChessPosition left = copyPos.deepCopy();

            if(right.lowerRight()){
                finishedPos(board,color,newPos,right);
            }
            // checks the left
            if(left.lowerLeft()){
                finishedPos(board,color,newPos,left);
            }
        }
        copyPos = position.deepCopy();


        if(copyPos.left()){
            ChessPosition up = copyPos.deepCopy();
            ChessPosition down = copyPos.deepCopy();

            if(up.upperLeft()){
                finishedPos(board,color,newPos,up);
            }
            // checks the left
            if(down.lowerLeft()) {
                finishedPos(board, color, newPos, down);
            }
        }

        return posToMove(newPos,position);
    }

}
