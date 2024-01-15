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
                possible_moves = this.Poss_Move_King(board,myPosition);
                break;
            case QUEEN:
                possible_moves = this.Poss_Move_Queen(board,myPosition);
                break;
            case BISHOP:
                possible_moves = this.Poss_Move_Bishop(board,myPosition);
                break;
            case KNIGHT:
                possible_moves = this.Poss_Move_Knight(board,myPosition);
                break;
            case ROOK:
                possible_moves = this.Poss_Move_Rook(board,myPosition);
                break;
            case PAWN:
                possible_moves = this.Poss_Move_Pawn(board, myPosition);
                break;
            default:
                possible_moves = new ArrayList<ChessMove>();

        }
        // updates first move condition
        firstMove = false;

        return possible_moves;
    }

    /*
     * So for this function, given the chess board and the current position it will give you a list of moves you can make.
     * Tips for starting,
     * make some sort of switch statement that will go through and do the math for each piece and possible spots it can go too
     * you may want to break this up a bit more, and create more methods to find spots or you might not.
     *
     * NOTE: take into account if there are other pieces that are currently in the way
     *
     */

    // methods for possible moves for each of the pieces

    public ArrayList<ChessMove> Poss_Move_King(ChessBoard board, ChessPosition myPosition){

        // makes all the variables we need
        ArrayList<ChessMove> possible_move_king = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        if(newPosition.up()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.upperRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.right()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.lowerRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.down()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.lowerLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.left()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        if(newPosition.upperLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_king.add(newMove);
            newPosition.setRow(copyRow);
            newPosition.setCol(copyCol);
        }
        return possible_move_king;
    }

    public ArrayList<ChessMove> Poss_Move_Queen(ChessBoard board, ChessPosition myPosition){

        // makes all the variables we need
        ArrayList<ChessMove> possible_move_queen = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        while(newPosition.up()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.upperRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.right()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.lowerRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.down()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.lowerLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.left()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.upperLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_queen.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        return possible_move_queen;
    }

    public ArrayList<ChessMove> Poss_Move_Bishop(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_move_bishop = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        while(newPosition.upperRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_bishop.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.lowerRight()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_bishop.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.lowerLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_bishop.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.upperLeft()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_bishop.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        return possible_move_bishop;
    }

    public ArrayList<ChessMove> Poss_Move_Rook(ChessBoard board, ChessPosition myPosition){

        ArrayList<ChessMove> possible_move_Rook = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        while(newPosition.up()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_Rook.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.right()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_Rook.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.down()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_Rook.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        while(newPosition.left()){
            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_Rook.add(newMove);
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        return possible_move_Rook;
    }

    public ArrayList<ChessMove> Poss_Move_Knight(ChessBoard board, ChessPosition myPosition){

        ArrayList<ChessMove> possible_move_knight = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        if(newPosition.up()){

            int diagRow = myPosition.getRow();
            if(newPosition.upperLeft()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
            newPosition.setRow(diagRow);
            if(newPosition.upperRight()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        if(newPosition.down()){

            int diagRow = myPosition.getRow();
            if(newPosition.lowerLeft()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
            newPosition.setRow(diagRow);
            if(newPosition.lowerRight()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        if(newPosition.right()){

            int diagCol = myPosition.getColumn();
            if(newPosition.upperRight()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
            newPosition.setRow(diagCol);
            if(newPosition.lowerRight()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        if(newPosition.left()){

            int diagCol = myPosition.getColumn();
            if(newPosition.upperLeft()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
            newPosition.setRow(diagCol);
            if(newPosition.lowerLeft()){
                ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_knight.add(newMove);
            }
        }
        newPosition.setRow(copyRow);
        newPosition.setCol(copyCol);

        return possible_move_knight;
    }

    public ArrayList<ChessMove> Poss_Move_Pawn(ChessBoard board, ChessPosition myPosition){

        ArrayList<ChessMove> possible_move_pawn = new ArrayList<ChessMove>();
        int copyRow = myPosition.getRow();
        int copyCol = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(copyRow,copyCol);

        if(newPosition.up()){

            ChessMove newMove = new ChessMove(myPosition,newPosition,myPieceType);
            possible_move_pawn.add(newMove);

            if(newPosition.up() && firstMove){
                ChessMove pawnSkip = new ChessMove(myPosition,newPosition,myPieceType);
                possible_move_pawn.add(pawnSkip);
            }
        }
        return possible_move_pawn;
    }
}
