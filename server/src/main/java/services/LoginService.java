package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlUserDAO;
import models.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginService extends Service{

    public LoginService() {}

    public LoginPackage completeJob(String username, String password) throws DataAccessException {

        var userAccess = new SqlUserDAO();
        User loginUser = userAccess.read(username);
        userAccess.closeConnection();


        // compares the two passwords
        if(comparePasswords(password,loginUser.getPassword())){

            // creates a authToken using the username
            AuthData newAuthData = createAuthData(loginUser.getUsername());

            // returns the new user and token
            return new LoginPackage(newAuthData.getUsername(), newAuthData.getAuthToken());
        }else{
            // means that they entered the wrong password
            throw new DataAccessException("[401](Unauthorized) Invalid Password");
        }
    }

    // service functions
    private boolean comparePasswords(String enteredPassword,String userPassword){
        BCryptPasswordEncoder decoder = new BCryptPasswordEncoder();
        return decoder.matches(enteredPassword,userPassword);
    }

    public static class LoginPackage {
        public String username,authToken;
        public LoginPackage(String username, String authToken){
            this.username = username;
            this.authToken = authToken;
        }
    }
}
