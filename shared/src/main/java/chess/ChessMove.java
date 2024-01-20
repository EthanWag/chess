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

    public static ArrayList<ChessMove> allowDiagonal(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        while(copyPos.upperRight()){
            ChessPiece tempPiece = board.getPiece(copyPos); // should just be a pointer to checked piece
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy()); // takes the enemy piece if they are not the same color
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();


        while(copyPos.lowerRight()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();


        while(copyPos.lowerLeft()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();


        while(copyPos.upperLeft()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(tempPiece != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }


        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }

        // makes all the chess moves and returns the list
        return moves;
    }

    public static ArrayList<ChessMove> allowVertical(ChessBoard board, ChessPosition position){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        while(copyPos.up()){
            positions.add(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.right()){
            positions.add(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.down()){
            positions.add(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.left()){
            positions.add(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                break;
            }
        }

        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }
        // makes all the chess moves and returns the list
        return moves;
    }

    public static ArrayList<ChessMove> allowKing(ChessBoard board, ChessPosition position){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // this might not work so feel free to scrap this
        if(copyPos.up()){positions.add(copyPos);}
        if(copyPos.right()){positions.add(copyPos);}
        if(copyPos.down()){positions.add(copyPos);}
        if(copyPos.down()){positions.add(copyPos);}
        if(copyPos.left()){positions.add(copyPos);}
        if(copyPos.left()){positions.add(copyPos);}
        if(copyPos.up()){positions.add(copyPos);}
        if(copyPos.up()){positions.add(copyPos);}

        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
    }
    public static ArrayList<ChessMove> allowKnight(ChessBoard board, ChessPosition position){

        ArrayList<ChessMove> moves = new ArrayList<>();
        // this might create a lot of garbage so come back to this
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        if(copyPos.up()){
            ChessPosition copyPos1 = copyPos.deepCopy();
            if(copyPos.upperLeft()){
                positions.add(copyPos);
            }
            if(copyPos1.upperRight()){
                positions.add(copyPos1);
            }
        }
        copyPos = position.deepCopy();

        if(copyPos.right()){
            ChessPosition copyPos1 = copyPos.deepCopy();
            if(copyPos.upperRight()){
                positions.add(copyPos);
            }
            if(copyPos1.lowerRight()){
                positions.add(copyPos1);
            }
        }
        copyPos = position.deepCopy();

        if(copyPos.down()){
            ChessPosition copyPos1 = copyPos.deepCopy();
            if(copyPos.lowerLeft()){
                positions.add(copyPos);
            }
            if(copyPos1.lowerRight()){
                positions.add(copyPos1);
            }
        }
        copyPos = position.deepCopy();

        if(copyPos.left()){
            ChessPosition copyPos1 = copyPos.deepCopy();
            if(copyPos.upperLeft()){
                positions.add(copyPos);
            }
            if(copyPos1.lowerLeft()){
                positions.add(copyPos1);
            }
        }

        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
    }

    public static ArrayList<ChessMove> allowPawn(ChessBoard board, ChessPosition position, ChessPiece pawn){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // checks to see if it can move up once or twice??
        if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY)){
            positions.add(copyPos);

            // checks to see if it can go up two spaces
            if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY) && pawn.getFirstMove()){
                positions.add(copyPos);
            }
        }
        copyPos = position.deepCopy();
        ChessPosition copyPos1 = position.deepCopy();

        copyPos.upperLeft();
        copyPos1.upperRight();

        if(board.getPiece(copyPos) != ChessBoard.EMPTY){
            positions.add(copyPos);
        }
        if(board.getPiece(copyPos1) != ChessBoard.EMPTY){
            positions.add(copyPos1);
        }
        // NOTE: need to program case where pawn can get promoted
        for(ChessPosition pos: positions){
            ChessMove newMove = new ChessMove(position,pos,null);
            moves.add(newMove);
        }
        return moves;
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

    // not implemented yet
    // private static ArrayList<ChessMove> generateMoves(ArrayList<ChessPosition> positions, ChessPosition cur_pos){ return null;}

    /*
    TODO:
    - next, I need to program how chess pieces interact with other team colors, becomes right now, they are set to replace any piece
    - test all methods and classes to make sure they are working correctly
    - may optimize a bit more
     */


}
