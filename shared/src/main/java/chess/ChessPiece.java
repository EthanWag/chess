package chess;

import java.util.Collection;
import java.util.Objects;

import java.util.ArrayList;
import chess.Calculator.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor myColor;
    private ChessPiece.PieceType myType;
    private boolean firstMove;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        myColor = pieceColor;
        myType = type;
        firstMove = true;

    }
    public ChessPiece(ChessPiece other){
        this.myColor = other.myColor;
        this.myType = other.myType;
        this.firstMove = other.firstMove;
    }

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
        return myType;
    }

    public void setPieceType(PieceType newType){
        myType = newType;
    }

    public boolean getFirstMove(){
        return firstMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return myColor == that.myColor && myType == that.myType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myColor, myType, firstMove);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> possible_moves;


        switch(myType){

            case QUEEN:
                possible_moves = CalculatorQueen.findMoves(board,myColor,myPosition);
                break;
            case KING:
                possible_moves = CalculatorKing.findMoves(board,myColor,myPosition);
                break;
            case ROOK:
                possible_moves = CalculatorRook.findMoves(board,myColor,myPosition);
                break;
            case BISHOP:
                possible_moves = CalculatorBishop.findMoves(board,myColor,myPosition);
                break;
            case KNIGHT:
                possible_moves = CalculatorKnight.findMoves(board,myColor,myPosition);
                break;
            case PAWN:
                possible_moves = CalculatorPawn.findMoves(board,myColor,myPosition,this);
                break;
            default:
                possible_moves = new ArrayList<>();
                break;
        }

        firstMove = false;
        return possible_moves;
    }
}
