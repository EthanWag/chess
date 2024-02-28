package services;

import models.*;
import dataAccess.DataAccessException;

import java.util.Collection;

public class ListGamesService extends Service{

    public ListGamesService() {}

    public GamesPackage completeJob(String authtoken)throws DataAccessException{

        // Note: there is a return value of User but we ignore it because it is not important
        checkAuthToken(authtoken); // throws an exception in case it can't find it

        Collection<Game> allGames = getAllGames();

        // returns all the new games
        return new GamesPackage(allGames);
    }

    // service functions

    private Collection<Game> getAllGames(){
        return gameDAO.getAll();
    }


    public static class GamesPackage {
        public Collection<Game> games;
        public GamesPackage(Collection<Game> games){
            this.games = games;
        }
    }
}
