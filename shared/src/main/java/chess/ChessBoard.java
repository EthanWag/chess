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

                ChessPosition checkPos = new ChessPosition(i,k);
                ChessPiece checKing = getPiece(checkPos);

                if(checKing != EMPTY){
                    // checks to see the piece and it's color
                    if((checKing.getPieceType() == ChessPiece.PieceType.KING) && (checKing.getTeamColor() == color)){
                        return checkPos;
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

        int whitePawn = 2;
        int whiteBack = 1;
        int blackPawn = 7;
        int blackBack = 8;

        fillBoard();

        fillTeam(ChessGame.TeamColor.WHITE,whitePawn,whiteBack);
        fillTeam(ChessGame.TeamColor.BLACK,blackPawn,blackBack);
    }

    private void fillTeam(ChessGame.TeamColor color, int pawnRow, int backRow){

        int col = 1;
        // mirrors all first pieces
        mirrorSide(color, ChessPiece.PieceType.ROOK,backRow,col++);
        mirrorSide(color, ChessPiece.PieceType.KNIGHT,backRow,col++);
        mirrorSide(color, ChessPiece.PieceType.BISHOP,backRow,col++);

        // creates queen
        ChessPosition queenPos = new ChessPosition(backRow,col++);
        ChessPiece queen = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        addPiece(queenPos,queen);

        // creates king
        ChessPosition kingPos = new ChessPosition(backRow,col);
        ChessPiece king = new ChessPiece(color, ChessPiece.PieceType.KING);
        addPiece(kingPos,king);

        for(int i = 1; i <= BOUNDRIES; i++){
            ChessPosition pawnPos = new ChessPosition(pawnRow,i);
            ChessPiece pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            addPiece(pawnPos,pawn);
        }
    }

    private void mirrorSide(ChessGame.TeamColor color,
                            ChessPiece.PieceType type, int row, int col){

        ChessPosition pos = new ChessPosition(row,col);
        ChessPiece newPiece = new ChessPiece(color,type);
        addPiece(pos,newPiece);

        // mirrors other side

        ChessPosition otherPos = new ChessPosition(row,(BOUNDRIES - (col - 1)));
        ChessPiece otherPiece = new ChessPiece(color,type);
        addPiece(otherPos,otherPiece);

    }

}
