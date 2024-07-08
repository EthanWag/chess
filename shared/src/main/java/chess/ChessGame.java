package chess;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor myTeam;
    private ChessBoard myBoard;

    private static ChessGameSimulator simulator = new ChessGameSimulator();

    public ChessGame() {

        myTeam = TeamColor.WHITE;
        myBoard = new ChessBoard();
        myBoard.resetBoard();  // creates and makes the board

    }

    public ChessGame(ChessGame other){
        myTeam = other.myTeam;
        myBoard = other.myBoard.clone();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return myTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        myTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        RESIGN,
        WATCH
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    // possibly throw an error here
    public Collection<ChessMove> validMoves(ChessPosition startPosition){
        simulator.setBoard(myBoard);
        return simulator.approvedMoves(startPosition);
    }
    // helpful for makeMove function below, will find a position and find the moves it can make

    public void endGame(){
        myTeam = TeamColor.RESIGN;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // just to check to see if the game has already ended
        if(myTeam == TeamColor.RESIGN){
            throw new InvalidMoveException("ERROR: Game already ended");
        }

        // get the start position, the piece and the valid moves
        ChessPosition start = move.getStartPosition();
        ChessPiece movePiece = myBoard.getPiece(move.getStartPosition());

        // first checks to see if your not trying to move an empty space
        if(movePiece == ChessBoard.EMPTY){
            throw new InvalidMoveException("Cannot Move Empty Position");
        }
        // then checks to see if your not trying to move opponent piece
        if(movePiece.getTeamColor() != myTeam){
            throw new InvalidMoveException("Cannot Move Opponents piece");
        }

        Collection<ChessMove> validMoves = validMoves(start);

        // checks the valid move and throws an error if it doesn't work
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("Invalid Move");
        }

        // special moves are done in this in the first two if statements, other cases go to default

        if(move.getPromotionPiece() == ChessPiece.PieceType.CASTLE){

            moveCastle(move,movePiece);

        }else {
            // makes the actual move on the board
            myBoard.moveOnBoard(move,movePiece);
        }

        // finally promotes the pawn if applicable
        if(movePiece.getPieceType() == ChessPiece.PieceType.PAWN){
            promotePawn(movePiece,move.getEndPosition(),move.getPromotionPiece());
        }

        // changes the turn after you're done
        changeTurn();
    }

    // grab the start position and then grab the piece currently at that location
    // find moves and if it isn't found then throws a InvalidMoveException

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        simulator.setBoard(myBoard);
        LinkedHashSet<ChessPosition> isCheck = simulator.findKingMoves(teamColor);
        ChessPosition kingPos = myBoard.getKing(teamColor);

        if(!isCheck.isEmpty()){
            return !isCheck.contains(kingPos);
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        simulator.setBoard(myBoard);
        LinkedHashSet<ChessPosition> isCheckMate = simulator.findKingMoves(teamColor);

        return isCheckMate.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        simulator.setBoard(myBoard);
        ChessPosition kingPos = myBoard.getKing(teamColor);
        LinkedHashSet<ChessPosition> isStalemate = simulator.findKingMoves(teamColor);

        return (isStalemate.size() == 1) && isStalemate.contains(kingPos);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    private void changeTurn(){
        if (myTeam == TeamColor.WHITE) {
            myTeam = TeamColor.BLACK;
        }else{
            myTeam = TeamColor.WHITE;
        }
    }

    private void promotePawn(ChessPiece pawn, ChessPosition pawnPos, ChessPiece.PieceType promotion){
        if(myTeam == TeamColor.WHITE){ // white case
            // 8 is just the row that white pawns have to get to in order to be promoted
           if(pawnPos.getRow() == 8){
               pawn.setPieceType(promotion);
           }
        }else{ // black case
            if(pawnPos.getRow() == 1){
                pawn.setPieceType(promotion);
            }
        }
    }

    private void moveCastle(ChessMove move,ChessPiece piece){

        myBoard.moveOnBoard(move,piece);

        // we can assume the rook is there because it passed all the tests
        ChessPosition rookStartPos = new ChessPosition(move.getStartPosition().getRow(),8);
        ChessPiece rook = myBoard.getPiece(rookStartPos);
        ChessPosition rookEndPos = new ChessPosition(move.getStartPosition().getRow(),6);

        ChessMove rookMove = new ChessMove(rookStartPos,rookEndPos,null);
        myBoard.moveOnBoard(rookMove,rook);

    }
}
