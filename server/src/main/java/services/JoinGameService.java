package services;

import dataAccess.DataAccessException;
import models.*;
import org.eclipse.jetty.io.ssl.ALPNProcessor;

public class JoinGameService extends Service{

    public JoinGameService() {}

    public boolean completeJob(String authToken, String ClientColor, int gameId){

        try{

            // just checks to see if authData even exists
            getAuthData(authToken);
            Game joinGame = getGame(gameId);
            updateGame(joinGame, ClientColor);
            return true;

        }catch(DataAccessException error){

            System.err.println("[401] unauthorized");
            return false;
        }

    }

    // Service functions

    // getAuthToken is found in Service function

    private Game getGame(int gameID) throws DataAccessException{

        return GameDAO.read(gameID);

    }

    private void updateGame(Game joinGame, String TeamColor)throws DataAccessException{

        Game updatedGame = new Game(joinGame);
        updatedGame.setWhiteUsername(TeamColor);

        GameDAO.update(updatedGame,joinGame);
    }

}

// complete for now