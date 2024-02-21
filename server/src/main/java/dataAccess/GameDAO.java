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

    public void create(Game newGame){
        int newGameId = newGame.getGameID();
        dbGames.put(newGameId,newGame); // what happens if you create a new game?

        // come up with some sort of check in case you need to if you already have a game
    }

    public Game read(int gameId) throws DataAccessException{

        if(!dbGames.containsKey(gameId)){
            throw new DataAccessException("[400] Invalild game ID");
        }else{
            return  dbGames.get(gameId);;
        }
    }

    public void update(int gameId, Game updateGame) throws DataAccessException {

        // just throws an object error if it can't find anything
        if(!dbGames.containsKey(gameId)) {throw new DataAccessException("[400] Invalid game Id");}

        dbGames.put(gameId,updateGame);
    }

    public void delete(int gameId) throws DataAccessException {

        if(!dbGames.containsKey(gameId)) {throw new DataAccessException("[400] Invalid game Id");}
        dbGames.remove(gameId);
    }

    public Collection<Game> getAll(){
        // simply just returns all the names
        return dbGames.values();

    }

    public void deleteAll(){
        dbGames.clear();
    }

}
