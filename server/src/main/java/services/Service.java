package services;

import models.*;
import dataAccess.*;

import javax.xml.crypto.Data;
import java.util.UUID;

public abstract class Service {

    protected static UserDAO UserDAO = new UserDAO();
    protected static AuthDAO AuthDAO = new AuthDAO();
    protected static GameDAO GameDAO = new GameDAO();

    protected AuthData getAuthData(String authToken) throws DataAccessException{

        return AuthDAO.read(authToken);
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

    // checks to make sure you entered in the correct authToken
    protected User checkAuthToken(String authToken)throws DataAccessException{

        // will throw an [404] not found exception if it can't find it
        try {
            AuthData checkData = AuthDAO.read(authToken);

            // grabs current user with that authToken and returns user
            return UserDAO.read(checkData.getUsername());

        }catch(DataAccessException invalid){ // throws an unauthorized execption in case it can't find it
            throw new DataAccessException("[401](unauthorized) not valid authToken");
        }
    }


    // Generates AuthTokens and adds them to the Database. used in multiple services
    protected AuthData createAuthData(String username)throws DataAccessException{

        // checks to see if there is already an authToken made
        // if(AuthDAO.findAuth(username)){
            // throw new DataAccessException("[403](Used Auth)(Service.java) already logged in");
        // }

        // otherwise it makes an authToken and puts it in the database
        String authToken = UUID.randomUUID().toString(); // generate token here
        AuthData newAuth = new AuthData(authToken,username);
        AuthDAO.create(newAuth);
        return newAuth;
    }

}
