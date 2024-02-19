package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import chess.Calculator.*;

public class ChessGameSimulator {

    ChessBoard board;
    ChessBoard backup;

    public ChessGameSimulator(){}

    public void setBoard(ChessBoard myBoard){
        // makes an identical copy of the board that we can simulate with
        board = myBoard.clone();
        backup = myBoard.clone();
    }

    // resets the board so that you can it again
    private void resetBoard() {
        board = backup.clone();
    }

    // simulate game functions

    // simulates moves and returns the list of approved moves
    public Collection<ChessMove> approvedMoves(ChessPosition startPosition) {

        ArrayList<ChessMove> approved_moves = new ArrayList<>();


        // get the piece, it's color and it's moves
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        Collection<ChessMove> pieceMoves = piece.pieceMoves(board,startPosition);


        for(ChessMove move : pieceMoves){
            // makes a simple move and if it puts the king in danger, it doesn't add it to the approved moves
            simpleMakeMove(move);
            if(!kingInDanger(teamColor)){
                approved_moves.add(move);
            }
            resetBoard();
        }
        return approved_moves;
    }

    public LinkedHashSet<ChessPosition> findKingMoves(ChessGame.TeamColor teamColor) throws NullPointerException{

        try {
            // first off the bat, get the other team color and get the king position
            ChessGame.TeamColor opp_color = oppColor(teamColor);

            // find the king, his position and his moves
            ChessPosition king_pos = board.getKing(teamColor);
            ChessPiece king = board.getPiece(king_pos);
            Collection<ChessMove> king_moves = king.pieceMoves(board,king_pos);

            // finds other teams moves
            Collection<ChessMove> opp_team_moves = findOppTeamMoves(opp_color,(ArrayList<ChessMove>)king_moves);

            // these functions then convert them to sets for easy comparison
            LinkedHashSet<ChessPosition> opp_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) opp_team_moves, null);
            LinkedHashSet<ChessPosition> king_possible_pos = MoveToSetEnd((ArrayList<ChessMove>) king_moves, king_pos);

            return find_complement(king_possible_pos, opp_possible_pos);

        }catch(NullPointerException m){
            throw m;
        }
    }

    // this method find all a team can attack with given piece moves and a color
    private Collection<ChessMove> findOppTeamMoves(ChessGame.TeamColor color, ArrayList<ChessMove> piece_moves){

        // finds the team moves with the current board
        Collection<ChessMove> opp_team_moves = calculator_Team.find_moves(board, color);

        // finds the team moves with alternitve moves
        for (ChessMove possible_move : piece_moves) {
            simpleMakeMove(possible_move);

            // find all those moves and adds them to the calculator
            ArrayList<ChessMove> opp_moves = calculator_Team.find_moves(board, color); // could optimize here because you are adding unnessary moves

            // add them to the opponents total count
            opp_team_moves.addAll(opp_moves);

            resetBoard(); // resets the board so you can use it again
        }
        return opp_team_moves;
    }

    // kinda like the find king moves but useful for finding weather or not a given move will put the king in danger
    private boolean kingInDanger(ChessGame.TeamColor color){

        try {
            ChessPosition king_pos = board.getKing(color);
            ArrayList<ChessMove> opp_moves = calculator_Team.find_moves(board, oppColor(color));
            LinkedHashSet<ChessPosition> opp_positions = MoveToSetEnd(opp_moves,null);

            return opp_positions.contains(king_pos);

        }catch(NullPointerException m){
            System.err.println(m.getMessage());
        }
        return false;
    }

    // this function is very simple, just makes a simple move without being tied to any ChessGame rules
    private void simpleMakeMove(ChessMove move){

        ChessPiece move_piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(),move_piece);
        board.fillEmptySpot(move.getStartPosition());

    }

    // finds the complement between two lists
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
        if(color == ChessGame.TeamColor.WHITE){
            return ChessGame.TeamColor.BLACK;
        }else{
            return ChessGame.TeamColor.WHITE;
        }
    }
}