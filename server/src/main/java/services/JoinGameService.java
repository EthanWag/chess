package services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import models.*;

public class JoinGameService extends Service{

    private final SqlGameDAO gameAccess;

    public JoinGameService()throws DataAccessException{

        try{
            gameAccess = new SqlGameDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Unable to connect to database",500);
        }

    }

    public void completeJob(String authToken, String clientColor, int gameId) throws DataAccessException{

        // gets user and game, throws exceptions if it can't find it
        User currentUser = checkAuthToken(authToken);
        Game currentGame = getGame(gameId);

        // gets the username of the player
        String username = currentUser.username();

        // adds the player to the game if they are
        addPlayer(currentGame,username,clientColor);
    }

    // Service functions

    private void addPlayer(Game joinGame, String username, String teamColor)throws DataAccessException{

        switch(teamColor){ //checks to see what team they entered
            case "WHITE":
                if(!joinGame.isWhiteTaken()){ // if white is not taken
                    joinGame.joinWhiteSide(username);
                    gameAccess.updatePlayer(joinGame.getGameID(),username,true, ChessGame.TeamColor.WHITE);
                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case "BLACK":
                if(!joinGame.isBlackTaken()){ // if black is not taken
                    joinGame.joinBlackSide(username);
                    gameAccess.updatePlayer(joinGame.getGameID(),username,true, ChessGame.TeamColor.BLACK);

                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case "WATCH":
                break;

            default: // throws an error if they entered in a invalid team color
                throw new DataAccessException("ERROR: Bad request",400);
        }
    }
}