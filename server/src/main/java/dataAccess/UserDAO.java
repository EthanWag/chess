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
        if(Users.containsKey(username)){ throw new DataAccessException("[403](Used User)(UserDAO) User already taken");}

        Users.put(username,newUser);
    }

    public User read(String username) throws DataAccessException {

        if(!Users.containsKey(username)){throw new DataAccessException("[401](Unauthorized)(UserDAO) Invalid username");}

        // returns the user if can find it
        return Users.get(username);

    }

    public void update(User updateUser,String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[404](User Not Found)(UserDAO) Not Found");}

        Users.put(username,updateUser);
    }

    public void delete(String username) throws DataAccessException{

        if(!Users.containsKey(username)){throw new DataAccessException("[404](User Not Found)(UserDAO) Not Found");}

        // removes the user double check
        Users.remove(username);
    }

    public void deleteAll(){
        Users.clear();
    }
}
