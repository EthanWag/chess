package services;

import dataAccess.SqlUserDAO;
import dataAccess.DataAccessException;
import models.*;

import java.util.Objects;

public class RegisterService extends Service{

    public RegisterService() {}


    public RegisterPackage completeJob(String username, String password, String email)throws DataAccessException{

        // first checks to see valid input
        if(!checkRegister(username,password,email)){
            throw new DataAccessException("[400] bad request");
        }


        // create user and adds it to the database
        AuthData authorization = createUser(username,password,email);

        // create an authToken and return it
        return new RegisterPackage(authorization.getUsername(),authorization.getAuthToken());
    }

    // service functions

    // creates user and authData, returns the authData
    private AuthData createUser(String username, String password, String email)throws DataAccessException{

        // put password shuffle here




        User newUser = new User(username,password,email);

        // creates data Access object and commits
        var userAccess = new SqlUserDAO();
        userAccess.create(newUser);
        userAccess.commit();

        // creates new authData and returns that object
        return createAuthData(username);
    }

    private boolean checkRegister(String username, String password, String email){
        return !((username == null) || (password == null) || (email == null));
    }

    public static class RegisterPackage {
        public String username,authToken;
        public RegisterPackage(String username, String newAuthToken){
            this.username = username;
            this.authToken = newAuthToken;
        }

        // simple getters and setters for testing
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RegisterPackage that = (RegisterPackage) o;
            return Objects.equals(username, that.username) && Objects.equals(authToken, that.authToken);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, authToken);
        }
    }
}
