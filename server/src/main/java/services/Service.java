package services;

import models.*;
import dataAccess.*;

import java.util.UUID;

public abstract class Service {

    // gets an authData object, given a authToken
    protected AuthData getAuthData(String authToken) throws DataAccessException{

        var authAccess = new SqlAuthDAO();
        return authAccess.read(authToken);
    }

    // checks to make sure you entered in the correct authToken
    protected User checkAuthToken(String authToken)throws DataAccessException{

        try {
            // checks to see if the authToken is in the database
            var authAccess = new SqlAuthDAO();
            AuthData checkData = authAccess.read(authToken);

            // checks to see if the authToken is assigned to that user
            var userAccess = new SqlUserDAO();
            return userAccess.read(checkData.username());

        }catch(DataAccessException invalid){ // throws an unauthorized execption in case it can't find it
            throw new DataAccessException("ERROR:Invalid authToken",401);
        }
    }

    // Generates AuthTokens and adds them to the Database. Used in multiple services
    protected AuthData createAuthData(String username)throws DataAccessException {

        var authAccess = new SqlAuthDAO();

        // otherwise it makes an authToken and puts it in the database
        String authToken = UUID.randomUUID().toString(); // generate token here
        AuthData newAuth = new AuthData(authToken, username);

        // makes the data access object and commits
        authAccess.create(newAuth);

        return newAuth;
    }

    public Game getGame(int gameID) throws DataAccessException{

        var gameAccess = new SqlGameDAO(); //  this is where there the error happens always for some reason can't seem to find the game and fails the test

        return gameAccess.read(gameID);

    }
}
