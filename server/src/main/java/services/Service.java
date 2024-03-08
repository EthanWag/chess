package services;

import models.*;
import dataAccess.*;

import java.util.UUID;

public abstract class Service {

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        var authAccess = new SqlAuthDAO();
        AuthData foundAuth = authAccess.read(authToken);

        authAccess.closeConnection();
        return foundAuth;
    }

    // checks to make sure you entered in the correct authToken
    protected User checkAuthToken(String authToken)throws DataAccessException{

        try {

            var authAccess = new SqlAuthDAO();
            AuthData checkData = authAccess.read(authToken);
            authAccess.closeConnection();

            // grabs current user with that authToken and returns user
            var userAccess = new SqlUserDAO();
            User foundUser = userAccess.read(checkData.getUsername());
            userAccess.closeConnection();

            return foundUser;

        }catch(DataAccessException invalid){ // throws an unauthorized execption in case it can't find it
            throw new DataAccessException("[401](unauthorized) not valid authToken");
        }
    }


    // Generates AuthTokens and adds them to the Database. used in multiple services
    protected AuthData createAuthData(String username)throws DataAccessException {

        var authAccess = new SqlAuthDAO();



        // otherwise it makes an authToken and puts it in the database
        String authToken = UUID.randomUUID().toString(); // generate token here
        AuthData newAuth = new AuthData(authToken, username);

        // makes the data access object and commits
        authAccess.create(newAuth);
        authAccess.commit();

        return newAuth;
    }
}
