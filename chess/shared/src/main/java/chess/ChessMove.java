package chess;

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
        myPieceType = promotionPiece;

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

    // right now, this function makes sure that
    public boolean AllowedMove(){
        // might want to replace magic number, it stands for the boundries of the board
        return true;
    }






















}
