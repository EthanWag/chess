package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedHashSet;

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

        ChessPiece move_piece = myBoard.getPiece(move.getStartPosition());
        myBoard.addPiece(move.getEndPosition(),move_piece);
        myBoard.fillEmptySpot(move.getStartPosition());

        // need to add execption case

    }

    // This function does the same thing as makeMove, but your allowed to pass in a any board
    public void makeMove(ChessMove move, ChessBoard thisBoard){

        ChessPiece move_piece = thisBoard.getPiece(move.getStartPosition());
        thisBoard.addPiece(move.getEndPosition(),move_piece);
        thisBoard.fillEmptySpot(move.getStartPosition());

        // need to add exeption case

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

        try {
            // first off the bat, get the other team color and get the king position
            TeamColor opp_color = oppColor(teamColor);
            ChessPosition king_pos = myBoard.getKing(teamColor);

            // finds the kings moves and how the other team can move in response to that

            Collection<ChessMove> king_moves = validMoves(king_pos);
            Collection<ChessMove> opp_team_moves = findOppTeamMoves(opp_color,(ArrayList<ChessMove>)king_moves);

            // these functions then convert them to sets for easy comparison
            LinkedHashSet<ChessPosition> opp_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) opp_team_moves, null);
            LinkedHashSet<ChessPosition> king_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) king_moves, king_pos);

            LinkedHashSet<ChessPosition> isCheck = find_complement(king_possible_pos, opp_possible_pos);

            // EDGE CASE :
            if(!isCheck.isEmpty()){
                return !isCheck.contains(king_pos);
            }
            return false;

        }catch(NullPointerException m){

            System.err.println(m);
            System.err.println("error occured in the is checkname function");
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

        try {
            // first off the bat, get the other team color and get the king position
            TeamColor opp_color = oppColor(teamColor);
            ChessPosition king_pos = myBoard.getKing(teamColor);

            // finds the kings moves and how the other team can move in response to that

            Collection<ChessMove> king_moves = validMoves(king_pos);
            Collection<ChessMove> opp_team_moves = findOppTeamMoves(opp_color,(ArrayList<ChessMove>)king_moves);

            // these functions then convert them to sets for easy comparison
            LinkedHashSet<ChessPosition> opp_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) opp_team_moves, null);
            LinkedHashSet<ChessPosition> king_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) king_moves, king_pos);

            LinkedHashSet<ChessPosition> isCheck = find_complement(king_possible_pos, opp_possible_pos);

            return isCheck.isEmpty();

        }catch(NullPointerException m){

            System.err.println(m);
            System.err.println("error occured in the is checkname function");
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        try {
            // first off the bat, get the other team color and get the king position
            TeamColor opp_color = oppColor(teamColor);
            ChessPosition king_pos = myBoard.getKing(teamColor);

            // finds the kings moves and how the other team can move in response to that

            Collection<ChessMove> king_moves = validMoves(king_pos);
            Collection<ChessMove> opp_team_moves = findOppTeamMoves(opp_color,(ArrayList<ChessMove>)king_moves);

            // these functions then convert them to sets for easy comparison
            LinkedHashSet<ChessPosition> opp_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) opp_team_moves, null);
            LinkedHashSet<ChessPosition> king_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) king_moves, king_pos);

            LinkedHashSet<ChessPosition> isStalemate = find_complement(king_possible_pos, opp_possible_pos);

            // checks to see if it can only stay in the same place
            return (isStalemate.size() == 1) && isStalemate.getFirst() == king_pos;

        }catch(NullPointerException m){

            System.err.println(m);
            System.err.println("error occured in the is checkname function");
        }
        return false;
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

    // this method find all a team can attack with given piece moves and a color
    private Collection<ChessMove> findOppTeamMoves(TeamColor color, ArrayList<ChessMove> piece_moves){

        // finds the team moves with the current board
        Collection<ChessMove> opp_team_moves = calculator_Team.find_moves(myBoard, color);

        // finds the team moves with alternitve moves
        for (ChessMove possible_move : piece_moves) {
            ChessBoard copy_board = myBoard.clone(); // cloning works but doesn't quite override

            makeMove(possible_move, copy_board);

            // find all those moves and adds them to the calculator
            ArrayList<ChessMove> opp_moves = calculator_Team.find_moves(copy_board, color); // could optimize here because you are adding unnessary moves

            // add them to the opponents total count
            opp_team_moves.addAll(opp_moves);
        }
        return opp_team_moves;
    }


    private static LinkedHashSet<ChessPosition> find_complement(LinkedHashSet<ChessPosition> king_positions,
                                                                LinkedHashSet<ChessPosition> team_positions){

        LinkedHashSet<ChessPosition> copy_king_pos = new LinkedHashSet<>(king_positions);

        // will remove the current position if it is found in team positions
        copy_king_pos.removeIf(team_positions::contains);
        return copy_king_pos;
    }

    // This function takes in an ArrayList and converts them to a set with all possible end positions, also your allowed
    // to add one extra position to the set, but you don't have to
    private static LinkedHashSet<ChessPosition> MoveToSetEnd(ArrayList<ChessMove> all_moves, ChessPosition start_pos){

        LinkedHashSet<ChessPosition> pos_set = new LinkedHashSet<>();

        for(ChessMove move : all_moves){
            // Converts all moves into end positions
            pos_set.add(move.getEndPosition());
        }
        // adds the start_pos, if you want it
        if(start_pos != null){
            pos_set.add(start_pos);
        }
        return pos_set;
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
