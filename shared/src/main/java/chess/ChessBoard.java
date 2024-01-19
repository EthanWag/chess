package chess;

import java.util.Objects;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public final static ChessPiece EMPTY = null; // empty just means null, nothing special
    private final static int BOUNDRIES = 8;
    private final ChessPiece[][] myChessBoard;

    public ChessBoard() {
        myChessBoard = new ChessPiece[BOUNDRIES][BOUNDRIES]; // check syntax on this intilzation
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        // puts piece on board
        myChessBoard[row][col] = piece;
    }
    // just adds a null values given a position
    public void addEmpty(ChessPosition position){
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        myChessBoard[row][col] = EMPTY;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        return myChessBoard[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        int WHITE_BACK_ROW = 1;
        int WHITE_FRONT_ROW= 2;
        int BLACK_FRONT_ROW = 7;
        int BLACK_BACK_ROW = 8;

        this.fillEmptySpace();

        // creates white side
        this.fillTeam(ChessGame.TeamColor.WHITE,WHITE_BACK_ROW,WHITE_FRONT_ROW);
        // creates the black side
        this.fillTeam(ChessGame.TeamColor.BLACK,BLACK_BACK_ROW,BLACK_FRONT_ROW);

        // fills in the rest of the empty space

    }

    // note that you need to edit this code, you should use a deepequals method that checks everything
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.equals(myChessBoard, that.myChessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(myChessBoard);
    }

    // fills the entire team given a color and two rows
    private void fillTeam(ChessGame.TeamColor color, int back_row, int pawn_row){
        // fills the back row with rooks, knights and bishops
        int back_col = 1;
        mirrorBackRow(color,ChessPiece.PieceType.ROOK,back_row,back_col++);
        mirrorBackRow(color,ChessPiece.PieceType.KNIGHT,back_row,back_col++);
        mirrorBackRow(color,ChessPiece.PieceType.BISHOP,back_row,back_col++);

        // puts in king and queen in the order they are supposed to come in
        ChessPosition queen_index = new ChessPosition(back_row,back_col++);
        ChessPiece queen = new ChessPiece(color,ChessPiece.PieceType.QUEEN);
        this.addPiece(queen_index,queen);

        ChessPosition king_index = new ChessPosition(back_row,back_col);
        ChessPiece king = new ChessPiece(color,ChessPiece.PieceType.KING);
        this.addPiece(king_index,king);

        // fills pawns for both colors
        for(int col = 1; col <= BOUNDRIES; col++){
            // creates the piece's index as well as the pawn and adds it to the board
            ChessPosition index = new ChessPosition(pawn_row,col);
            ChessPiece pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            this.addPiece(index,pawn);
        }
    }

    // Fills the back row and mirrors all pieces. Rook,Knight,Bishop are the same both sides
    private void mirrorBackRow(ChessGame.TeamColor color,ChessPiece.PieceType type, int back_row, int back_col){
        // creates piece one
        ChessPosition back_position = new ChessPosition(back_row,back_col);
        ChessPiece back_piece = new ChessPiece(color,type);
        this.addPiece(back_position,back_piece);

        // makes a mirror of piece one
        ChessPosition back_position_mirror = new ChessPosition(back_row, (BOUNDRIES - (back_col-1)));
        ChessPiece back_piece_mirror = new ChessPiece(color,type);
        this.addPiece(back_position_mirror, back_piece_mirror);
    }

    // just fills all slots on the board with null values
    private void fillEmptySpace(){
        for(int i = 1; i <= BOUNDRIES; i++){
            for(int k = 1; k <= BOUNDRIES; k++){
                ChessPosition newPosition = new ChessPosition(i,k);
                this.addEmpty(newPosition);
            }
        }
    }
}
