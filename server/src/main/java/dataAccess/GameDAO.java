package dataAccess;

import models.Game;

import java.util.Collection;

public class GameDAO{

    public GameDAO() {}

    public void create(Game newGame){
        return; // add the game passed in and add it to the database
    }

    public Game read(int gameId) throws DataAccessException{
        return null;
    }

    public void update(Game updateGame, Game OldGame) throws DataAccessException {
        return;
    }

    public boolean delete() throws DataAccessException {
        return true;
    }

    public Collection<Game> getAll(){
        return null; // returns null for right now but in the future, will return all games in the database from that user
    }

    public void deleteAll(){return;}
}
