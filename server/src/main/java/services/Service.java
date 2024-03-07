package services;

import models.*;
import dataAccess.*;

import java.util.UUID;

public abstract class Service {

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        AuthDAO authAccess = new SqlAuthDAO();
        return authAccess.read(authToken);
    }

    // checks to make sure you entered in the correct authToken
    protected User checkAuthToken(String authToken)throws DataAccessException{

        // will throw an [404] not found exception if it can't find it
        try {

            var authAccess = new SqlAuthDAO();
            AuthData checkData = authAccess.read(authToken);

            // grabs current user with that authToken and returns user
            var userAccess = new SqlUserDAO();
            return userAccess.read(checkData.getUsername());

        }catch(DataAccessException invalid){ // throws an unauthorized execption in case it can't find it
            throw new DataAccessException("[401](unauthorized) not valid authToken");
        }
    }


    // Generates AuthTokens and adds them to the Database. used in multiple services
    protected AuthData createAuthData(String username)throws DataAccessException{

        // checks to see if an authToken has already been created
        var authAccess = new SqlAuthDAO();
        if(authAccess.authCreated(username)){
            throw new DataAccessException("[400] bad request");
        } // FIXME: new

        // otherwise it makes an authToken and puts it in the database
        String authToken = UUID.randomUUID().toString(); // generate token here
        AuthData newAuth = new AuthData(authToken,username);

        // makes the data access object and commits
        authAccess.create(newAuth);
        authAccess.commit();

        return newAuth;
    }
}
