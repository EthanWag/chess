package passoffTests.chessTests.chessPieceTests;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;

import static passoffTests.TestFactory.*;

public class CastleMoveTests {


    @Test
    public void whiteCastle() {
        validateCastle("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |K| | |R|
                        """,
                startPosition(1, 5),
                endPositions(new int[][]{{1, 4}, {2, 4}, {2, 5}, {2, 6},{1,6},{1,7}})
        );
    }

    // need to edit the code for castling for black because it won't work
    @Test
    public void blackCastle() {
        validateCastle("""
                        | | | | |k| | |r|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                startPosition(8, 5),
                endPositions(new int[][]{{8, 4}, {7, 4}, {7, 5}, {7, 6},{8,6},{8,7}})
        );
    }

    @Test
    public void noRook() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |K| | | |
                        """,
                startPosition(1, 5),
                endPositions(new int[][]{{1, 4}, {2, 4}, {2, 5}, {2, 6},{1,6}})
        );
    }

    @Test
    public void kingNoCorrect() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |K| | | |
                        | | | | | | | |R|
                        """,
                startPosition(2, 5),
                endPositions(new int[][]{{1, 5},{1,4},{2,4},{3,4},{3,5},{3,6},{2,6},{1,6}})
        );
    }

    private void validateCastle(String boardText, ChessPosition startPosition, int[][] endPositions){
        var board = TestFactory.loadBoard(boardText);
        var testPiece = board.getPiece(startPosition);
        var validMoves = new HashSet<ChessMove>();
        for (var endPosition : endPositions) {

            if(endPosition[1] == 7){
                var end = startPosition(endPosition[0], endPosition[1]);
                validMoves.add(TestFactory.getNewMove(startPosition, end, ChessPiece.PieceType.CASTLE));
            }else{
                var end = startPosition(endPosition[0], endPosition[1]);
                validMoves.add(TestFactory.getNewMove(startPosition, end,null));
            }
        }
        validateMoves(board, testPiece, startPosition, validMoves);
    }
}
