package chess;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition myStartPosition;
    private ChessPosition myEndPosition;
    private ChessPiece.PieceType myPieceType; // double check and make sure that it valid syntax


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        myStartPosition = startPosition;
        myEndPosition = endPosition;
        myPieceType = promotionPiece; // what does this mean? what does it want me to set this as?

    }

    public ChessPosition getStartPosition() {
        return myStartPosition;
    }

    public ChessPosition getEndPosition() {
        return myEndPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        // This function does a bit more than expected, will let use know if it can change types
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(myStartPosition, chessMove.myStartPosition) && Objects.equals(myEndPosition, chessMove.myEndPosition) && myPieceType == chessMove.myPieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myStartPosition, myEndPosition, myPieceType);
    }
}
