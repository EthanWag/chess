package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] myChessBoard;
    private final static int BOUNDRIES = 8;
    private final static ChessPiece EMPTY = null; // used to repusent an empty space

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

        int row = position.getRow();
        int col = position.getColumn();

        // puts piece on board
        myChessBoard[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return myChessBoard[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        int WHITE_BACK_ROW = 0;
        int WHITE_FRONT_ROW= 1;
        int BLACK_FRONT_ROW = 6;
        int BLACK_BACK_ROW = 7;

        this.fillEmptySpace();

        // creates white side
        this.fillTeam(ChessGame.TeamColor.WHITE,WHITE_BACK_ROW,WHITE_FRONT_ROW);
        // creates the black side
        this.fillTeam(ChessGame.TeamColor.BLACK,BLACK_BACK_ROW,BLACK_FRONT_ROW);

        // fills in the rest of the empty space

    }
    //
    private void fillTeam(ChessGame.TeamColor color, int back_row, int pawn_row){
        // fills the back row with rooks, knights and bishops
        int back_col = 0;
        mirrorBackRow(color,ChessPiece.PieceType.ROOK,back_row,back_col++);
        mirrorBackRow(color,ChessPiece.PieceType.KNIGHT,back_row,back_col++);
        mirrorBackRow(color,ChessPiece.PieceType.BISHOP,back_row,back_col++);

        // puts in king and queen in the order they are supposed to come in
        if(color == ChessGame.TeamColor.WHITE){
            ChessPosition queen_index = new ChessPosition(back_row,back_col++);
            ChessPiece queen = new ChessPiece(color,ChessPiece.PieceType.QUEEN);// should be white if it entered this if statement
            this.addPiece(queen_index,queen);

            ChessPosition king_index = new ChessPosition(back_row,back_col);
            ChessPiece king = new ChessPiece(color,ChessPiece.PieceType.KING);
            this.addPiece(king_index,king);
        }else{
            ChessPosition king_index = new ChessPosition(back_row,back_col++);
            ChessPiece king = new ChessPiece(color,ChessPiece.PieceType.KING);// should be black if it entered this if statement
            this.addPiece(king_index,king);

            ChessPosition queen_index = new ChessPosition(back_row,back_col);
            ChessPiece queen = new ChessPiece(color,ChessPiece.PieceType.QUEEN);
            this.addPiece(queen_index,queen);
        }
        // fills pawns for both colors
        for(int col = 0; col < BOUNDRIES; col++){
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
        ChessPosition back_position_mirror = new ChessPosition(back_row, (BOUNDRIES - (1 + back_col)));
        ChessPiece back_piece_mirror = new ChessPiece(color,type);
        this.addPiece(back_position_mirror, back_piece_mirror);
    }

    private void fillEmptySpace(){
        for(int i = 0; i < BOUNDRIES; i++){
            for(int k = 0; k < BOUNDRIES; k++){
                myChessBoard[i][k] = EMPTY;
            }
        }
    }

}
