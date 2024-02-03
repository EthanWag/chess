package chess;

import java.util.Collection;
import java.util.ArrayList;

import chess.Calculator.calculator_Team;
/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor myTeam;
    ChessBoard myBoard;

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
        return my_piece.pieceMoves(myBoard,startPosition);

    }
    // helpful for makeMove function below, will find a position and find the moves it can make


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
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

        try{

            ChessPosition king_pos = myBoard.getKing(teamColor);

            // find the moves the king and other team can make

            Collection<ChessMove> opp_team_moves = calculator_Team.find_moves(myBoard,teamColor);
            Collection<ChessMove> king_moves = validMoves(king_pos);

            king_moves = find_complement((ArrayList<ChessMove>)king_moves, (ArrayList<ChessMove>) opp_team_moves);

            // returns true if the only if there are chess moves, and none of them can move to itself
            return king_moves.isEmpty();

        }catch(NullPointerException m){
            // catches the error and prints the message
            System.err.println(m.getMessage());
            System.err.println("Board not intilized properly?");
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition king_pos = myBoard.getKing(teamColor);

        // find the moves the king and other team can make

        Collection<ChessMove> opp_team_moves = calculator_Team.find_moves(myBoard,teamColor);
        Collection<ChessMove> king_moves = validMoves(king_pos);

        king_moves = find_complement((ArrayList<ChessMove>)king_moves, (ArrayList<ChessMove>) opp_team_moves);

        // returns true if the complement is empty
        return king_moves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition king_pos = myBoard.getKing(teamColor);

        // find the moves the king and other team can make

        Collection<ChessMove> opp_team_moves = calculator_Team.find_moves(myBoard,teamColor);
        Collection<ChessMove> king_moves = validMoves(king_pos);

        king_moves = find_complement((ArrayList<ChessMove>)king_moves, (ArrayList<ChessMove>) opp_team_moves);

        // your going to want to change this to find other conditions, in this case, if king_moves list has only 1 move
        // and it is two the king_pos
        return king_moves.isEmpty();
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


    // helper functions for the ChessGame class

    private static ArrayList<ChessMove> find_complement(ArrayList<ChessMove> king_moves, ArrayList<ChessMove> team_moves){

        // should create a copy ArrayList
        ArrayList<ArrayList> king_comp_moves = new ArrayList<>(king_moves);
        king_comp_moves.removeAll(team_moves);

        return king_comp_moves;
    }
}
