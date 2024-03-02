package dataAccess;

import models.Game;

import java.util.Collection;
import java.lang.Integer;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    // Data structures: that keeps track of the user and all of their games
    HashMap<Integer,Game> dbGames;


    public MemoryGameDAO() {
        dbGames = new HashMap<>();
    }

    public void create(Game newGame) throws DataAccessException {
        int newGameId = newGame.getGameID();
        // checks for if it is already taken
        if(dbGames.containsKey(newGameId)){ throw new DataAccessException("[403](Used Game)(GameDAO) gameId already taken");}

        dbGames.put(newGameId,newGame);
    }

    public Game read(int gameId) throws DataAccessException{

        if(!dbGames.containsKey(gameId)){ throw new DataAccessException("[400](Game Not Found)(GameDAO) Not Found");}

        return  dbGames.get(gameId);
    }

    public Collection<Game> getAll(){
        // simply just returns all the games
        return dbGames.values();
    }

    public void deleteAll(){
        dbGames.clear();
    }

}
