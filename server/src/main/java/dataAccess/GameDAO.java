package dataAccess;

import models.Game;

import java.util.Collection;

public interface GameDAO {

    public void create(Game newGame) throws DataAccessException;
    public Game read(int gameId) throws DataAccessException;
    public Collection<Game> getAll();
    public void deleteAll();


}
