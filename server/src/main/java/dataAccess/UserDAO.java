package dataAccess;

import java.util.HashMap;

import models.User;


public class UserDAO{

    HashMap<User,String> Users;

    public UserDAO(){}

    public void create(User newUser) {
        return;
    }

    public User read(String username) throws DataAccessException {
        return null;
    }

    public void update(User updateUser,String AuthToken) throws DataAccessException{}

    public boolean delete(User delUser) throws DataAccessException{
        return true;
    }

    public void deleteAll(){return;}

}
