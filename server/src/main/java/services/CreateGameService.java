package services;

import dataAccess.SqlGameDAO;
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

        // creates all the new variables
        String black = null;
        String white = null;

        // creates game and adds it to the database
        Game newGame = new Game(-1,white,black,gameName,false,false);
        // FIXME: in the future your going to want to put -1 in the myGameId slot

        // creates game and then commits changes
        var gameAccess = new SqlGameDAO();
        gameAccess.create(newGame);
        gameAccess.commit();

        return newGame.getGameID();
    }

    public static class GamePackage {
        public int gameID;
        public GamePackage(int gameID){
            this.gameID = gameID;
        }
    }
}
