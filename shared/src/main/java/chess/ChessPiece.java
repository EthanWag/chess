package chess;

import chess.Piece_Calculators.*;
import chess.ChessGame;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //Instance-Field for Chess Piece object
    private final ChessGame.TeamColor myColor;
    private final PieceType myPieceType;
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


    public boolean pawnFirstMove(ChessPosition myPosition){

        if(myColor == ChessGame.TeamColor.WHITE){
            return (myPosition.getRow() == 2) && firstMove;
        }else{
            return (myPosition.getRow() == 7) && firstMove;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return firstMove == that.firstMove && myColor == that.myColor && myPieceType == that.myPieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myColor, myPieceType, firstMove);
    }

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
                // This and the rest of the Calculator classes aren't objects, they strictly do math
                possible_moves = Calculator_King.findKingMoves(board, myPosition,myColor);
                break;
            case QUEEN:
                possible_moves = Calculator_Queen.findQueenMoves(board, myPosition,myColor);
                break;
            case BISHOP:
                possible_moves = Calculator_Bishop.findBishopMoves(board,myPosition,myColor);
                break;
            case KNIGHT:
                possible_moves = Calculator_Knight.findKnightMoves(board,myPosition,myColor);
                break;
            case ROOK:
                possible_moves = Calculator_Rook.findRookMoves(board,myPosition,myColor);
                break;
            case PAWN:
                possible_moves = Calculator_Pawn.findPawnMoves(board, myPosition,this);
                break;
            default:
                possible_moves = new ArrayList<>();

        }
        // updates first move condition
        firstMove = false;
        return possible_moves;
    }

}
