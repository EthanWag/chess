package dataAccess;

import java.util.HashMap;

import models.User;


public class MemoryUserDAO implements UserDAO {

    // string is username and User is an actual user object
    HashMap<String,User> users;

    public MemoryUserDAO(){
        users = new HashMap<>();
    }

    public void create(User newUser) throws DataAccessException {
        String username = newUser.getUsername();
        // throws an exception if it is already taken
        if(users.containsKey(username)){ throw new DataAccessException("[403](Used User)(UserDAO) User already taken");}

        users.put(username,newUser);
    }

    public User read(String username) throws DataAccessException {

        if(!users.containsKey(username)){throw new DataAccessException("[401](Unauthorized)(UserDAO) Invalid username");}

        // returns the user if can find it
        return users.get(username);

    }

    public void deleteAll(){
        users.clear();
    }
}
