package services;

import models.*;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.util.Random;


public class CreateGameService extends Service{

    public CreateGameService(){}

    public gamePackage completeJob(String authToken, String gameName)throws DataAccessException{

        checkAuthToken(authToken); // throws exception in case if can't find authToken

        // returns gameId
        int newGameID = createGame(gameName);

        // finally returns the new object
        return new gamePackage(newGameID);
    }

    // service functions

    private int createGame(String gameName)throws DataAccessException{

        // this generates the random number generator
        Random random = new Random();

        // creates all the new variables
        int newGameId = random.nextInt(10,100000);
        String black = null;
        String white = null;

        // creates game and adds it to the database
        Game newGame = new Game(newGameId,white,black,gameName);
        GameDAO.create(newGame);

        return newGameId;
    }

    public static class gamePackage{
        int gameID;
        public gamePackage(int gameID){
            this.gameID = gameID;
        }
    }

}
