package services;

import dataAccess.DataAccessException;
import models.*;

public class RegisterService extends Service{


    public RegisterService() {}


    public String completeJob(String username, String password, String email){

        try{
            // this should fail, if it doesn't, there already is a user with that name
            User invalid = getUser(username);

            return invalid.getUsername() + " is already being used\n" + "Please chose another Username\n";

        }catch(DataAccessException validUser){

            createUser(username,password,email);

            AuthData newAuth = createAuthData(username);
            return newAuth.getAuthToken(); // A string, for now
        }
    }

    // service functions

    // simply just creates a new user and puts it in the database
    private User createUser(String username, String password, String email){

        User newUser = new User(username,password,email);
        UserDAO.create(newUser);
        return newUser;
    }
}
