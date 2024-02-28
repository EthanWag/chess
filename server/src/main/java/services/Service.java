package services;

import models.*;
import dataAccess.*;

import java.util.UUID;

public abstract class Service {

    protected static UserDAO userDAO = new UserDAO();
    protected static AuthDAO authDAO = new AuthDAO();
    protected static GameDAO gameDAO = new GameDAO();

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        return authDAO.read(authToken);
    }

    // given a username, will return a user object, if not found will print a message
    protected User getUser(String username) throws DataAccessException{

        try{
            return userDAO.read(username);

        }catch(DataAccessException error){
            throw error;
            // for right now, just keep try catch block in case you want to handle it in the future
        }
    }

    // checks to make sure you entered in the correct authToken
    protected User checkAuthToken(String authToken)throws DataAccessException{

        // will throw an [404] not found exception if it can't find it
        try {
            AuthData checkData = authDAO.read(authToken);

            // grabs current user with that authToken and returns user
            return userDAO.read(checkData.getUsername());

        }catch(DataAccessException invalid){ // throws an unauthorized execption in case it can't find it
            throw new DataAccessException("[401](unauthorized) not valid authToken");
        }
    }


    // Generates AuthTokens and adds them to the Database. used in multiple services
    protected AuthData createAuthData(String username)throws DataAccessException{

        // otherwise it makes an authToken and puts it in the database
        String authToken = UUID.randomUUID().toString(); // generate token here
        AuthData newAuth = new AuthData(authToken,username);
        authDAO.create(newAuth);
        return newAuth;
    }

}
