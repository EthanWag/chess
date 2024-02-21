package dataAccess;

import java.util.HashMap;

import models.User;


public class UserDAO{

    // string is username and User is an actual user object
    HashMap<String,User> Users;

    public UserDAO(){
        Users = new HashMap<>();
    }

    public void create(User newUser) {
        String username = newUser.getUsername();
        // possibly make a new an execption if you find it in the

        Users.put(username,newUser);
    }

    public User read(String username) throws DataAccessException {

        if(!Users.containsKey(username)){throw new DataAccessException("[400] Invalid username");}

        // returns the user if can find it
        return Users.get(username);

    }

    public void update(User updateUser,String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[400] Invalid username");}

        Users.put(username,updateUser);
    }

    public void delete(String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[400] Invalid username");}

        // removes the user double check
        Users.remove(username);
    }

    public void deleteAll(){
        Users.clear();
    }
}
