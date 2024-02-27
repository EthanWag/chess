package services;

import dataAccess.DataAccessException;
import models.*;

public class RegisterService extends Service{

    public RegisterService() {}


    public registerPackage completeJob(String username, String password, String email)throws DataAccessException{


        // create user and adds it to the database
        createUser(username,password,email);

        // create an authToken and return it
        AuthData newAuth = createAuthData(username);

        // grabs user data
        String authUsername = newAuth.getUsername();
        String authToken = newAuth.getAuthToken();

        return new registerPackage(authUsername,authToken);
    }

    // service functions

    // simply just creates a new user and puts it in the database
    private void createUser(String username, String password, String email)throws DataAccessException{
        User newUser = new User(username,password,email);
        UserDAO.create(newUser);
    }

    public static class registerPackage{
        public String username,authToken;
        public registerPackage(String username, String newAuthToken){
            this.username = username;
            this.authToken = newAuthToken;
        }
    }
}
