package dataAccess;

import models.Game;

import java.util.Collection;

public interface GameDAO {

    public int create(Game newGame) throws DataAccessException;
    public Game read(int gameId) throws DataAccessException;
    public Collection<Game> getAll() throws DataAccessException;
    public void deleteAll();


}
