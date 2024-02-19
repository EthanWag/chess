package services;

import models.*;
import dataAccess.*;

import javax.xml.crypto.Data;

public abstract class Service {

    protected static UserDAO UserDAO = new UserDAO();
    protected static AuthDAO AuthDAO = new AuthDAO();
    protected static GameDAO GameDAO = new GameDAO();

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        try{
            return AuthDAO.read(authToken);

        }catch(DataAccessException error){
            throw error;
        }

    }

    // given a username, will return a user object, if not found will print a message
    protected User getUser(String username) throws DataAccessException{

        try{
            return UserDAO.read(username);

        }catch(DataAccessException error){
            throw error;
            // for right now, just keep try catch block in case you want to handle it in the future
        }
    }


    // Generates AuthTokens and adds them to the Database
    protected AuthData createAuthData(String username){

        String authToken = "banana"; // generate token here
        AuthData newAuth = new AuthData(authToken,username);
        AuthDAO.create(newAuth);
        return newAuth;
    }

}
