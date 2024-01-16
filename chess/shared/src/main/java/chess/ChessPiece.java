package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //Instance-Field for Chess Piece object
    private final ChessGame.TeamColor myColor;
    private final PieceType myPieceType; // might need to change final but it maks sense, once this object is created, no need to change it
    private boolean firstMove;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        // simple constructor
        myColor = pieceColor;
        myPieceType = type;
        firstMove = true;
    }
    // using default constructor for empty pieces

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return myColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return myPieceType;
    }

    public boolean getFirstMove() { return firstMove;}

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possible_moves;

        // decides moved based off of which kind of piece it is
        switch(this.myPieceType){
            case KING:
                possible_moves = ChessMove.allowKing(board, myPosition);
                break;
            case QUEEN:
                possible_moves = ChessMove.allowVertical(board, myPosition);
                possible_moves.addAll(ChessMove.allowDiagonal(board,myPosition));
                break;
            case BISHOP:
                possible_moves = ChessMove.allowDiagonal(board, myPosition);
                break;
            case KNIGHT:
                possible_moves = ChessMove.allowKnight(board, myPosition);
                break;
            case ROOK:
                possible_moves = ChessMove.allowVertical(board, myPosition);
                break;
            case PAWN:
                possible_moves = ChessMove.allowPawn(board, myPosition,this);
                break;
            default:
                possible_moves = new ArrayList<>();

        }
        // updates first move condition
        firstMove = false;

        return possible_moves;
    }
}
