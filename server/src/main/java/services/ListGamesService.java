package services;

import models.*;
import dataAccess.DataAccessException;

import java.util.Collection;

public class ListGamesService extends Service{

    public ListGamesService() {}

    public Collection<Game> completeJob(String Authtoken){

        try{

            // don't need token, just check that it exsists, would throw an error if it couldn't find it
            getAuthData(Authtoken);

            return getAllGames();

        }catch(DataAccessException error){

            // subject to change
            System.err.println("Invalid Authorization...");
            System.err.println("exiting");
            return null;

        }
    }

    // service functions

    // getAuthData is found in Services function

    private Collection<Game> getAllGames(){
        return GameDAO.getAll();
    }

}

// done for now
