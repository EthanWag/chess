package services;

import dataAccess.DataAccessException;
import models.*;

import java.util.Objects;

public class RegisterService extends Service{

    public RegisterService() {}


    public registerPackage completeJob(String username, String password, String email)throws DataAccessException{

        // first checks to see valid input
        checkRegister(username,password,email);

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

    private void checkRegister(String username, String password, String email)throws DataAccessException{
        if((username == null) || (password == null) || (email == null)){
            throw new DataAccessException("[400](bad request)(Register Service) invalid input");
        }
    }

    public static class registerPackage{
        public String username,authToken;
        public registerPackage(String username, String newAuthToken){
            this.username = username;
            this.authToken = newAuthToken;
        }

        // simple getters and setters for testing
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            registerPackage that = (registerPackage) o;
            return Objects.equals(username, that.username) && Objects.equals(authToken, that.authToken);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, authToken);
        }
    }
}
