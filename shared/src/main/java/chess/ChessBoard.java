package chess;

import javax.management.relation.InvalidRoleInfoException;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public static final ChessPiece EMPTY = null;
    public static final int BOUNDRIES = 8;
    private static final int FLIP_VAL = 8;
    private final ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[BOUNDRIES][BOUNDRIES];
        fillBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */

    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = FLIP_VAL - position.getRow();
        int col = position.getColumn() - 1;

        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = FLIP_VAL - position.getRow();
        int col = position.getColumn() - 1;

        return board[row][col];
    }

    public ChessPiece[][] __getBoard() {
        return board;
    }

    public void fillEmptySpot(ChessPosition position){
        int row = FLIP_VAL - position.getRow();
        int col = position.getColumn() - 1;

        board[row][col] = EMPTY;
    }

    private void fillBoard(){
        for(int i = 1; i <= BOUNDRIES; i++){
            for(int k = 1; k <= BOUNDRIES; k++){
                ChessPosition empty = new ChessPosition(i,k);
                fillEmptySpot(empty);
            }
        }
    }

    // given a color, will return the position of the king, given the color
    public ChessPosition getKing(ChessGame.TeamColor color) throws NullPointerException {
        for(int i = 1; i <= BOUNDRIES; i++){
            for(int k = 1; k <= BOUNDRIES; k++){

                ChessPosition check_pos = new ChessPosition(i,k);
                ChessPiece check_king = getPiece(check_pos);

                if(check_king != EMPTY){
                    // checks to see the piece and it's color
                    if((check_king.getPieceType() == ChessPiece.PieceType.KING) && (check_king.getTeamColor() == color)){
                        return check_pos;
                    }
                }
            }
        }

        // if for some reason the king is not on the board, it will throw an NullPointerExecption
        throw new NullPointerException("Error: King for color " + color + " not found");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public ChessBoard clone(){

        ChessBoard newChessBoard = new ChessBoard();

        for(int row = 0; row < BOUNDRIES; row++){
            for(int col = 0; col < BOUNDRIES; col++){

                if(board[row][col] == EMPTY){
                    newChessBoard.__getBoard()[row][col] = EMPTY;
                }else {
                    newChessBoard.__getBoard()[row][col] = new ChessPiece(board[row][col]);
                }
            }
        }
        return newChessBoard;
    }




    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {

        int WHITE_PAWN = 2;
        int WHITE_BACK = 1;
        int BLACK_PAWN = 7;
        int BLACK_BACK = 8;

        fillBoard();

        fillTeam(ChessGame.TeamColor.WHITE,WHITE_PAWN,WHITE_BACK);
        fillTeam(ChessGame.TeamColor.BLACK,BLACK_PAWN,BLACK_BACK);
    }

    private void fillTeam(ChessGame.TeamColor color, int pawn_row, int back_row){

        int col = 1;
        // mirrors all first pieces
        mirrorSide(color, ChessPiece.PieceType.ROOK,back_row,col++);
        mirrorSide(color, ChessPiece.PieceType.KNIGHT,back_row,col++);
        mirrorSide(color, ChessPiece.PieceType.BISHOP,back_row,col++);

        // creates queen
        ChessPosition queen_pos = new ChessPosition(back_row,col++);
        ChessPiece queen = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        addPiece(queen_pos,queen);

        // creates king
        ChessPosition king_pos = new ChessPosition(back_row,col);
        ChessPiece king = new ChessPiece(color, ChessPiece.PieceType.KING);
        addPiece(king_pos,king);

        for(int i = 1; i <= BOUNDRIES; i++){
            ChessPosition pawn_pos = new ChessPosition(pawn_row,i);
            ChessPiece pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            addPiece(pawn_pos,pawn);
        }
    }

    private void mirrorSide(ChessGame.TeamColor color,
                            ChessPiece.PieceType type, int row, int col){

        ChessPosition pos = new ChessPosition(row,col);
        ChessPiece new_piece = new ChessPiece(color,type);
        addPiece(pos,new_piece);

        // mirrors other side

        ChessPosition other_pos = new ChessPosition(row,(BOUNDRIES - (col - 1)));
        ChessPiece other_piece = new ChessPiece(color,type);
        addPiece(other_pos,other_piece);

    }

}
