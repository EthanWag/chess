package dataAccess;

import models.Game;
import models.User;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

public class GameDAO{

    // Data structures: that keeps track of the user and all of their games
    HashMap<User,ArrayList<Game>> dbGames;


    public GameDAO() {
        dbGames = new HashMap<>();
    }

    public void create(User cur_user, Game newGame){
        return; // add the game passed in and add it to the database
    }

    public Game read(User cur_user, int gameId) throws DataAccessException{
        return null;
    }

    public void update(User cur_user, Game updateGame, Game OldGame) throws DataAccessException {
        return;
    }

    public boolean delete(User cur_user,Game delGame) throws DataAccessException {
        return true;
    }

    public Collection<Game> getAll(User cur_user){
        return null; // returns null for right now but in the future, will return all games in the database from that user
    }

    public void deleteAll(){return;}
}
