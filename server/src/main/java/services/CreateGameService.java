package services;

import dataAccess.SqlGameDAO;
import models.*;
import dataAccess.DataAccessException;
import models.resModels.ResponseGamePackage;

import javax.xml.crypto.Data;


public class CreateGameService extends Service{

    private SqlGameDAO gameAccess;

    public CreateGameService()throws DataAccessException{
        try{
            gameAccess = new SqlGameDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("Error: Unable to connect to the database",500);
        }
    }

    public ResponseGamePackage completeJob(String authToken, String gameName)throws DataAccessException{

        try {
            checkAuthToken(authToken);
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Invalid authorization",401);
        }

        int newGameID = createGame(gameName);
        return new ResponseGamePackage(newGameID);
    }

    // helper functions for the class
    private int createGame(String gameName)throws DataAccessException{

        // creates all the new variables
        String black = null;
        String white = null;

        // creates game and adds it to the database
        Game newGame = new Game(-1,white,black,gameName,false,false);

        // creates game and then commits changes
        int newGameId = gameAccess.create(newGame);

        newGame.setGameID(newGameId); // sets the new game ID
        return newGameId;
    }
}
