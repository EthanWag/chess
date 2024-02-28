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

        ArrayList<ChessMove> approvedMoves = new ArrayList<>();


        // get the piece, it's color and it's moves
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        Collection<ChessMove> pieceMoves = piece.pieceMoves(board,startPosition);


        for(ChessMove move : pieceMoves){
            // makes a simple move and if it puts the king in danger, it doesn't add it to the approved moves
            simpleMakeMove(move);
            if(!kingInDanger(teamColor)){
                approvedMoves.add(move);
            }
            resetBoard();
        }
        return approvedMoves;
    }

    public LinkedHashSet<ChessPosition> findKingMoves(ChessGame.TeamColor teamColor) throws NullPointerException{

        try {
            // first off the bat, get the other team color and get the king position
            ChessGame.TeamColor oppColor = oppColor(teamColor);

            // find the king, his position and his moves
            ChessPosition kingPos = board.getKing(teamColor);
            ChessPiece king = board.getPiece(kingPos);
            Collection<ChessMove> kingMoves = king.pieceMoves(board,kingPos);

            // finds other teams moves
            Collection<ChessMove> oppTeamMoves = findOppTeamMoves(oppColor,(ArrayList<ChessMove>)kingMoves);

            // these functions then convert them to sets for easy comparison
            LinkedHashSet<ChessPosition> oppPossiblePos = moveToSetEnd((ArrayList<ChessMove>) oppTeamMoves, null);
            LinkedHashSet<ChessPosition> kingPossiblePos = moveToSetEnd((ArrayList<ChessMove>) kingMoves, kingPos);

            return findComplement(kingPossiblePos, oppPossiblePos);

        }catch(NullPointerException m){
            throw m;
        }
    }

    // this method find all a team can attack with given piece moves and a color
    private Collection<ChessMove> findOppTeamMoves(ChessGame.TeamColor color, ArrayList<ChessMove> pieceMoves){

        // finds the team moves with the current board
        Collection<ChessMove> oppTeamMoves = CalculatorTeam.findMoves(board, color);

        // finds the team moves with alternitve moves
        for (ChessMove possibleMove : pieceMoves) {
            simpleMakeMove(possibleMove);

            // find all those moves and adds them to the calculator
            ArrayList<ChessMove> oppMoves = CalculatorTeam.findMoves(board, color); // could optimize here because you are adding unnessary moves

            // add them to the opponents total count
            oppTeamMoves.addAll(oppMoves);

            resetBoard(); // resets the board so you can use it again
        }
        return oppTeamMoves;
    }

    // kinda like the find king moves but useful for finding weather or not a given move will put the king in danger
    private boolean kingInDanger(ChessGame.TeamColor color){

        try {
            ChessPosition kingPos = board.getKing(color);
            ArrayList<ChessMove> oppMoves = CalculatorTeam.findMoves(board, oppColor(color));
            LinkedHashSet<ChessPosition> opp_positions = moveToSetEnd(oppMoves,null);

            return opp_positions.contains(kingPos);

        }catch(NullPointerException m){
            System.err.println(m.getMessage());
        }
        return false;
    }

    // this function is very simple, just makes a simple move without being tied to any ChessGame rules
    private void simpleMakeMove(ChessMove move){

        ChessPiece movePiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(),movePiece);
        board.fillEmptySpot(move.getStartPosition());

    }

    // finds the complement between two lists
    private static LinkedHashSet<ChessPosition> findComplement(LinkedHashSet<ChessPosition> kingPositions,
                                                               LinkedHashSet<ChessPosition> teamPositions){

        LinkedHashSet<ChessPosition> copyKingPos = new LinkedHashSet<>(kingPositions);

        // will remove the current position if it is found in team positions
        copyKingPos.removeIf(teamPositions::contains);
        return copyKingPos;
    }

    // This function takes in an ArrayList and converts them to a set with all possible end positions, also your allowed
    // to add one extra position to the set, but you don't have to
    private static LinkedHashSet<ChessPosition> moveToSetEnd(ArrayList<ChessMove> allMoves, ChessPosition startPos){

        LinkedHashSet<ChessPosition> posSet = new LinkedHashSet<>();

        for(ChessMove move : allMoves){
            // Converts all moves into end positions
            posSet.add(move.getEndPosition());
        }
        // adds the startPos, if you want it
        if(startPos != null){
            posSet.add(startPos);
        }
        return posSet;
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
