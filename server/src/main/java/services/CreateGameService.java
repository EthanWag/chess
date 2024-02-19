package services;

import models.*;
import dataAccess.DataAccessException;



public class CreateGameService extends Service{

    public CreateGameService(){}

    public int completeJob(String Authtoken, String gameName){

        try{

            getAuthData(Authtoken);
            return createGame(gameName);

        }catch(DataAccessException error){

            System.err.println("Invalid user");
            System.err.println("exiting");
            return -1;

        }
    }

    // service functions

    // getAuthData is in service

    private int createGame(String gameName){

        // creates all the new variables
        int newGameId = 5;
        String black = "name";
        String white = "name";

        // creates game and adds it to the database
        Game newGame = new Game(newGameId,white,black,gameName);
        GameDAO.create(newGame);

        return newGameId;

    }
}

// complete for now
