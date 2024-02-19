package dataAccess;

import models.User;

import javax.xml.crypto.Data;

public class UserDAO{

    public UserDAO(){}

    public void create(User newUser) {
        return;
    }

    public User read(String username) throws DataAccessException {
        return null;
    }

    public void update(User updateUser) throws DataAccessException{}

    public boolean delete() throws DataAccessException{
        return true;
    }

}
