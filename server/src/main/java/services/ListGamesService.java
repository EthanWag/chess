package services;

import dataAccess.SqlGameDAO;
import models.*;
import dataAccess.DataAccessException;
import models.resModels.ResponseListPackage;

import java.util.Collection;

public class ListGamesService extends Service{

    private final SqlGameDAO gameAccess;

    public ListGamesService() throws DataAccessException{
        try{
            gameAccess = new SqlGameDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Unable to connect to database",500);
        }
    }

    public ResponseListPackage completeJob(String authToken)throws DataAccessException{

        try {
            checkAuthToken(authToken); // throws an exception in case it can't find it
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Invalid authorization",401);
        }

        Collection<Game> allGames = getAllGames();
        return new ResponseListPackage(allGames);
    }

    // service functions
    private Collection<Game> getAllGames() throws DataAccessException{
        try {
            return gameAccess.getAll();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Database Connection lost",500);
        }
    }

    // TODO: make sure to get rid of this
    public static class GamesPackage {
        public Collection<Game> games;
        public GamesPackage(Collection<Game> games){
            this.games = games;
        }
    }

}
