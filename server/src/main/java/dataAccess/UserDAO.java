package dataAccess;

import models.User;

public interface UserDAO {

    public void create(User newUser) throws DataAccessException;
    public User read(String username) throws DataAccessException;
    public void deleteAll();
}
