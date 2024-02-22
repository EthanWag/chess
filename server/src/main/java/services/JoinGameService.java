package services;

import dataAccess.DataAccessException;
import models.*;
import org.eclipse.jetty.io.ssl.ALPNProcessor;

public class JoinGameService extends Service{

    public JoinGameService() {}

    public void completeJob(String authToken, String ClientColor, int gameId) throws DataAccessException{

        // gets user and game, throws exceptions if it can't find it
        User currentUser = checkAuthToken(authToken);
        Game currentGame = getGame(gameId);

        // gets the username of the player
        String username = currentUser.getUsername();

        // adds the player to the game if they are
        addPlayer(currentGame,username,ClientColor);
    }

    // Service functions

    private Game getGame(int gameID) throws DataAccessException{
        return GameDAO.read(gameID);
    }

    private void addPlayer(Game joinGame, String username, String TeamColor)throws DataAccessException{

        switch(TeamColor){ //checks to see what team they entered
            case "WHITE":
                if(!joinGame.isWhiteTaken()){ // if white is not taken
                    joinGame.joinWhiteSide(username);
                }else{
                    throw new DataAccessException("[403] already taken");
                }
                break;

            case "BLACK":
                if(!joinGame.isBlackTaken()){ // if black is not taken
                    joinGame.joinBlackSide(username);
                }else{
                    throw new DataAccessException("[403] already taken");
                }
                break;

            default: // throws an error if they entered in a invalid team color
                throw new DataAccessException("[400] bad request");
        }
    }
}

// complete for now