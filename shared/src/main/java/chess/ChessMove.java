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
        myPieceType = promotionPiece; // what does this mean? what does it want me to set this as?

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

    public static ArrayList<ChessMove> allowDiagonal(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        // checks all positions in the upper right position of the current position
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

        // checks all positions in the lower right position of the current position
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

        // checks all positions in the lower left position of the current position
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

        // checks all positions in the upper left position of the current position
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

        // finally creates all the values
        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }

        // makes all the chess moves and returns the list
        return moves;
    }

    public static ArrayList<ChessMove> allowVertical(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        // this will be the list of moves that will be possible given the piece position, this is what will be returned
        ArrayList<ChessMove> moves = new ArrayList<>();
        // This is a large list of positions that will make the moves at the end of the function
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // makes a copy position for me to use later

        ChessPosition copyPos = position.deepCopy();

        // checks all up positions and gives possible moves
        while(copyPos.up()){
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

        // checks all right positions and gives possible moves
        while(copyPos.right()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all down positions and gives possible moves
        while(copyPos.down()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }
        copyPos = position.deepCopy();

        // checks all left positions and gives possible moves
        while(copyPos.left()){
            ChessPiece tempPiece = board.getPiece(copyPos);
            if(board.getPiece(copyPos) != board.EMPTY){
                if(color != tempPiece.getTeamColor()){
                    positions.add(copyPos.deepCopy());
                }
                break;
            }
            positions.add(copyPos.deepCopy());
        }

        // finally creates all moves the a piece can make
        for(ChessPosition pos : positions){
            ChessMove newMove = new ChessMove(position,pos,null); // null because no pawn will ever use this function
            moves.add(newMove);
        }
        // makes all the chess moves and returns the list
        return moves;
    }

    public static ArrayList<ChessMove> allowKing(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // adds all possible moves the king can make
        if(copyPos.up()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.upperRight()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.right()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.lowerRight()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.down()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.lowerLeft()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.left()){positions.add(copyPos.deepCopy());}
        copyPos = position.deepCopy();
        if(copyPos.upperLeft()){positions.add(copyPos.deepCopy());}


        // you may want to add to this function later on in the project, if that is the case, come back to
        // this location

        // this loops from the possible options and adds them if they are possible to make
        for(ChessPosition pos: positions){

            ChessPiece tempPiece = board.getPiece(pos);
            if (tempPiece != ChessBoard.EMPTY) {
                if(color != tempPiece.getTeamColor()){
                    ChessMove newMove = new ChessMove(position,pos,null);
                    moves.add(newMove);
                }
            }else{
                ChessMove newMove = new ChessMove(position,pos,null);
                moves.add(newMove);
            }
        }
        return moves;
    }
    public static ArrayList<ChessMove> allowKnight(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){

        ArrayList<ChessMove> moves = new ArrayList<>();
        // this might create a lot of garbage so come back to this
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition copyPos = position.deepCopy();

        // checks to see if it can move up
        if(copyPos.up()){
            ChessPosition move_left = copyPos.deepCopy();
            ChessPosition move_right = copyPos.deepCopy();

            // checks both sides
            if(move_left.upperLeft()){
                ChessPiece tempPiece = board.getPiece(move_left);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_left.deepCopy());
                    }
                }else{
                    positions.add(move_left.deepCopy());
                }
            }

            if(move_right.upperRight()){
                ChessPiece tempPiece = board.getPiece(move_right);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_right.deepCopy());
                    }
                }else{
                    positions.add(move_right.deepCopy());
                }
            }
        }
        copyPos = position.deepCopy();

        if(copyPos.right()){
            ChessPosition move_up = copyPos.deepCopy();
            ChessPosition move_down = copyPos.deepCopy();

            // checks the left side from current position
            if(move_up.upperRight()){
                ChessPiece tempPiece = board.getPiece(move_up);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_up.deepCopy());
                    }
                }else{
                    positions.add(move_up.deepCopy());
                }
            }
            // checks the right side from current location
            if(move_down.lowerRight()){
                ChessPiece tempPiece = board.getPiece(move_down);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_down.deepCopy());
                    }
                }else{
                    positions.add(move_down.deepCopy());
                }
            }
        }

        copyPos = position.deepCopy();

        if(copyPos.down()){
            ChessPosition move_right = copyPos.deepCopy();
            ChessPosition move_left = copyPos.deepCopy();

            if(move_right.lowerRight()){
                ChessPiece tempPiece = board.getPiece(move_right);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_right.deepCopy());
                    }
                }else{
                    positions.add(move_right.deepCopy());
                }
            }
            if(move_left.lowerLeft()){
                ChessPiece tempPiece = board.getPiece(move_left);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_left.deepCopy());
                    }
                }else{
                    positions.add(move_left.deepCopy());
                }
            }
        }

        copyPos = position.deepCopy();

        if(copyPos.left()){
            ChessPosition move_up = copyPos.deepCopy();
            ChessPosition move_down = copyPos.deepCopy();

            if(move_up.upperLeft()){
                ChessPiece tempPiece = board.getPiece(move_up);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_up.deepCopy());
                    }
                }else{
                    positions.add(move_up.deepCopy());
                }
            }
            if(move_down.lowerLeft()){
                ChessPiece tempPiece = board.getPiece(move_down);
                if(tempPiece != ChessBoard.EMPTY){
                    if(color != tempPiece.getTeamColor()){
                        positions.add(move_down.deepCopy());
                    }
                }else{
                    positions.add(move_down.deepCopy());
                }
            }
        }

        // makes all the moves that a knight can make
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

        // This takes care of the moves up that a pawn can make
        if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY)){
            positions.add(copyPos.deepCopy());

            // checks to see if it can go up two spaces
            if(copyPos.up() && (board.getPiece(copyPos) == ChessBoard.EMPTY) && pawn.getFirstMove()){
                positions.add(copyPos.deepCopy());
            }
        }

        ChessPosition attack_left = position.deepCopy();
        ChessPosition attack_right = position.deepCopy();

        // first makes sure that it is even a legal move
        if(attack_left.upperLeft()) {
            ChessPiece left_p = board.getPiece(attack_left);
            // then checks if there is a piece there, then checks to see if it can take it
            if((left_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != left_p.getTeamColor())) {
                positions.add(attack_left.deepCopy()); // if it can, it adds it as a possible move
            }
        }

        // Also checks to see if upperRight is a valid move, has the same logic as the left
        if(attack_right.upperRight()) {
            ChessPiece right_p = board.getPiece(attack_right);
            if ((right_p != ChessBoard.EMPTY) && (pawn.getTeamColor() != right_p.getTeamColor())) {
                positions.add(attack_right.deepCopy());
            }
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
