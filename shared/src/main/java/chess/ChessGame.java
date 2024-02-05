package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import chess.Calculator.calculator_Team;
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
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece my_piece = myBoard.getPiece(startPosition);
        // checks to make sure it is not an empty space
        if(my_piece == ChessBoard.EMPTY){ return null; } // handles edge case were you pass in an empty piece

        TeamColor my_color = my_piece.getTeamColor();
        Collection<ChessMove> my_moves = my_piece.pieceMoves(myBoard,startPosition);

        // using an iterator for this loop
        Iterator<ChessMove> iterator = my_moves.iterator();

        // so it look like it can detect wrong moves the first time but not any other time. I'm thinking
        // you need to be careful moving pieces on the board. Might be worth making a simulation class

        while(iterator.hasNext()){
            ChessMove move = iterator.next();
            simpleMakeMove(move,myBoard);
            if(isInCheck(my_color)){
                iterator.remove(); // error here
                undo(move,myBoard);
            }
        }

        return my_moves;
    }
    // helpful for makeMove function below, will find a position and find the moves it can make


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // get the start variable and valid moves it can make
        ChessPosition start = move.getStartPosition();
        Collection<ChessMove> valid_moves = validMoves(start);

        // checks the valid move and throws an error if it doesn't work
        if(!valid_moves.contains(move)){
            throw new InvalidMoveException("Invalid Move");
        }
        // extra conditions if it in check

        ChessPiece move_piece = myBoard.getPiece(move.getStartPosition());
        myBoard.addPiece(move.getEndPosition(),move_piece);
        myBoard.fillEmptySpot(move.getStartPosition());

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
        ChessPosition king_pos = myBoard.getKing(teamColor);

        if(!isCheck.isEmpty()){
            return !isCheck.contains(king_pos);
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
        ChessPosition king_pos = myBoard.getKing(teamColor);
        LinkedHashSet<ChessPosition> isStalemate = simulator.findKingMoves(teamColor);

        return (isStalemate.size() == 1) && isStalemate.contains(king_pos);
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

    // this function is very simple, just makes a simple move without being tied to any ChessGame rules
    private void simpleMakeMove(ChessMove move, ChessBoard thisBoard){

        ChessPiece move_piece = thisBoard.getPiece(move.getStartPosition());
        thisBoard.addPiece(move.getEndPosition(),move_piece);
        thisBoard.fillEmptySpot(move.getStartPosition());

    }

    // simple function that returns the other team color
    private static ChessGame.TeamColor oppColor(ChessGame.TeamColor color){
        if(color == TeamColor.WHITE){
            return TeamColor.BLACK;
        }else{
            return TeamColor.WHITE;
        }
    }
}
