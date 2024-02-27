package services;

import dataAccess.DataAccessException;
import models.*;

public class RegisterService extends Service{

    public RegisterService() {}


    public RegisterPackage completeJob(String username, String password, String email)throws DataAccessException{


        // create user and adds it to the database
        createUser(username,password,email);

        // create an authToken and return it
        AuthData newAuth = createAuthData(username);

        // grabs AuthToken and adds it to the package
        String newAuthToken = newAuth.getAuthToken();
        return new RegisterPackage(username,newAuthToken);

    }

    // service functions

    // simply just creates a new user and puts it in the database
    private void createUser(String username, String password, String email)throws DataAccessException{
        User newUser = new User(username,password,email);
        UserDAO.create(newUser);
    }

    public static class RegisterPackage{
        public String username,authToken;
        public RegisterPackage(String username, String authToken){
            this.username = username;
            this.authToken = authToken;
        }
    }

}
