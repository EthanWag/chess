package services;

import models.*;
import dataAccess.DataAccessException;

import java.util.Collection;

public class ListGamesService extends Service{

    public ListGamesService() {}

    public ListGamesPackage completeJob(String authtoken)throws DataAccessException{

        // Note: there is a return value of User but we ignore it because it is not important
        checkAuthToken(authtoken); // throws an exception in case it can't find it

        return new ListGamesPackage(getAllGames());
    }

    // service functions

    private Collection<Game> getAllGames(){
        return GameDAO.getAll();
    }

    // check this data structure, I think this may cause serialization problems
    public static class ListGamesPackage{
        public Collection<Game> games;

        public ListGamesPackage(Collection<Game> games){
            this.games = games;
        }
    }
}
