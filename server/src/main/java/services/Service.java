package services;

import models.*;
import dataAccess.*;

import java.util.UUID;

public abstract class Service {

    protected static MemoryUserDAO userDAO = new MemoryUserDAO();
    protected static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    protected static MemoryGameDAO gameDAO = new MemoryGameDAO();

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        return authDAO.read(authToken);
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
