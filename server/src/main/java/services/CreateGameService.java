package services;

import models.*;
import dataAccess.DataAccessException;

import java.util.Random;


public class CreateGameService extends Service{

    public CreateGameService(){}


    public GamePackage completeJob(String authToken, String gameName)throws DataAccessException{

        checkAuthToken(authToken); // throws exception in case if can't find authToken

        // returns gameId
        int newGameID = createGame(gameName);

        // finally returns the new object
        return new GamePackage(newGameID);
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
        gameDAO.create(newGame);

        return newGameId;
    }

    public static class GamePackage {
        public int gameID;
        public GamePackage(int gameID){
            this.gameID = gameID;
        }
    }
}
