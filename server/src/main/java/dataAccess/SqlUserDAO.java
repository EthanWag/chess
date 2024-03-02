package dataAccess;

import models.User;

public class SqlUserDAO implements UserDAO{

    public void create(User newUser) throws DataAccessException{

    }
    public User read(String username) throws DataAccessException{
        return null;
    }
    public void deleteAll(){

    }

    // I just want this method
    private void prepDatabase() throws DataAccessException{

    }

}
