package services;

import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import models.*;

public class JoinGameService extends Service{

    public JoinGameService() {}

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

    private Game getGame(int gameID) throws DataAccessException{
        var gameAccess = new SqlGameDAO();
        Game foundGame = gameAccess.read(gameID);

        gameAccess.close();
        return foundGame;

    }

    private void addPlayer(Game joinGame, String username, String teamColor)throws DataAccessException{

        switch(teamColor){ //checks to see what team they entered
            case "WHITE":
                if(!joinGame.isWhiteTaken()){ // if white is not taken
                    joinGame.joinWhiteSide(username);

                    var accessGame = new SqlGameDAO();
                    accessGame.updatePlayer(joinGame.getGameID(),username,"WHITE");
                    accessGame.commit();


                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case "BLACK":
                if(!joinGame.isBlackTaken()){ // if black is not taken

                    var accessGame = new SqlGameDAO();
                    accessGame.updatePlayer(joinGame.getGameID(),username,"BLACK");
                    accessGame.commit();

                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case null:
                break; // they just want to watch the game


            default: // throws an error if they entered in a invalid team color

                // right here you need to add functionality of watching a game
                throw new DataAccessException("ERROR: Bad request",400);
        }
    }
}