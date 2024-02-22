package dataAccess;

import models.Game;

import java.util.Collection;
import java.lang.Integer;
import java.util.HashMap;

public class GameDAO{

    // Data structures: that keeps track of the user and all of their games
    HashMap<Integer,Game> dbGames;


    public GameDAO() {
        dbGames = new HashMap<>();
    }

    public void create(Game newGame) throws DataAccessException {
        int newGameId = newGame.getGameID();
        // checks for if it is already taken
        if(dbGames.containsKey(newGameId)){ throw new DataAccessException("[403] Already Taken");}

        dbGames.put(newGameId,newGame);
    }

    public Game read(int gameId) throws DataAccessException{

        if(!dbGames.containsKey(gameId)){ throw new DataAccessException("[404] Not found");}

        return  dbGames.get(gameId);
    }

    public void update(int gameId, Game updateGame) throws DataAccessException {

        // just throws an object error if it can't find anything
        if(!dbGames.containsKey(gameId)) {throw new DataAccessException("[404] Not found");}

        dbGames.put(gameId,updateGame);
    }

    public void delete(int gameId) throws DataAccessException {

        if(!dbGames.containsKey(gameId)) {throw new DataAccessException("[404] Not found");}

        dbGames.remove(gameId);
    }

    public Collection<Game> getAll(){
        // simply just returns all the games
        return dbGames.values();
    }

    public void deleteAll(){
        dbGames.clear();
    }

}
