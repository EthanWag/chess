package server.WebSocketServer;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import models.Game;

/**
 * Things validator checks for
 *
 * AUTHENTICATION
 * Are they a valid color moving at the correct time
 * is it the correct players turn?
 * is that player even there?
 * does that player have an authToken
 *
 *
 * GAME LOGIC:
 * is the game over?
 * are they in checkmate?
 * are they in stalemate?
 * are they in check?
 * did they make a valid move?
 *
 */



public class MoveValidator {

    // decides if it can make that move, throws an error otherwise
    public static void validMove(String username, Game game, ChessMove playerMove)throws DataAccessException {

        ChessGame playerGame = game.getGame();

        // is game resigned, if yes, throws an error
        if(gameResign(playerGame)){
            throw new DataAccessException("MESSAGE: Game already finished",400);
        }

        // gets the color of the game and checks to see if that player can move
        ChessGame.TeamColor gameTurn = playerGame.getTeamTurn();

        if(!correctPlayer(username,gameTurn,game)){
            throw new DataAccessException("MESSAGE: Not correct player",400);
        }

        if(!playableMove(playerGame,playerMove)){
            throw new DataAccessException("MESSAGE: Not Valid Move",400);
        }
    }

    // is this move playable from a chess game perspective
    private static boolean playableMove(ChessGame game, ChessMove move){

        var possibleMoves = game.validMoves(move.getStartPosition());

        if(!possibleMoves.contains(move)){
            return false;
        }else{
            try {
                game.makeMove(move);
                return true;
            }catch(InvalidMoveException error){
                return false;
            }
        }
    }

    // is the game resigned or is the game re
    private static boolean gameResign(ChessGame game){

        // checks to see if anyone is in checkmate
        if(game.getTeamTurn() == ChessGame.TeamColor.RESIGN){
            return true;
        }

        // checks to see if anyone is in checkmate
        if(game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            game.endGame();
            return true;
        }else{
            return false;
        }
    }

    private static boolean correctPlayer(String username, ChessGame.TeamColor turn, Game gameInfo){

        // is that player in the game AND is it the same username
        if(turn == ChessGame.TeamColor.WHITE){
            return gameInfo.isWhiteTaken() && gameInfo.getWhiteUsername().equals(username);
        }else{
            return gameInfo.isBlackTaken() && gameInfo.getBlackUsername().equals(username);
        }
    }
}