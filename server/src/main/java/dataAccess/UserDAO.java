package dataAccess;

import java.util.HashMap;

import models.User;


public class UserDAO{

    // string is username and User is an actual user object
    HashMap<String,User> Users;

    public UserDAO(){
        Users = new HashMap<>();
    }

    public void create(User newUser) throws DataAccessException {
        String username = newUser.getUsername();
        // throws an exception if it is already taken
        if(Users.containsKey(username)){ throw new DataAccessException("[403] Already Taken");}

        Users.put(username,newUser);
    }

    public User read(String username) throws DataAccessException {

        if(!Users.containsKey(username)){throw new DataAccessException("[404] Not Found");}

        // returns the user if can find it
        return Users.get(username);

    }

    public void update(User updateUser,String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[404] Not Found");}

        Users.put(username,updateUser);
    }

    public void delete(String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[404] Not Found");}

        // removes the user double check
        Users.remove(username);
    }

    public void deleteAll(){
        Users.clear();
    }
}
