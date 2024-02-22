package services;

import models.*;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;


public class CreateGameService extends Service{

    public CreateGameService(){}

    public int completeJob(String authToken, String gameName)throws DataAccessException{

        checkAuthToken(authToken); // throws exception in case if can't find authToken

        // returns gameId
        return createGame(gameName);
    }

    // service functions

    private int createGame(String gameName)throws DataAccessException{

        // creates all the new variables
        int newGameId = 5; // generate int here
        String black = "";
        String white = "";

        // creates game and adds it to the database
        Game newGame = new Game(newGameId,white,black,gameName);
        GameDAO.create(newGame);

        return newGameId;
    }
}

// complete for now
