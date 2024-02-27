package services;

import models.*;
import dataAccess.DataAccessException;

import java.util.Collection;

public class ListGamesService extends Service{

    public ListGamesService() {}

    public gamesPackage completeJob(String authtoken)throws DataAccessException{

        // Note: there is a return value of User but we ignore it because it is not important
        checkAuthToken(authtoken); // throws an exception in case it can't find it

        Collection<Game> allGames = getAllGames();

        // returns all the new games
        return new gamesPackage(allGames);
    }

    // service functions

    private Collection<Game> getAllGames(){
        return GameDAO.getAll();
    }


    public static class gamesPackage{
        public Collection<Game> games;
        public gamesPackage(Collection<Game> games){
            this.games = games;
        }
    }
}
