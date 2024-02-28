package services;

import dataAccess.DataAccessException;
import models.*;

public class LoginService extends Service{

    public LoginService() {}

    public loginPackage completeJob(String username, String password) throws DataAccessException {


        User loginUser = UserDAO.read(username);

        // compares the two passwords
        if(comparePasswords(password,loginUser.getPassword())){
            // creates a authToken using the username
            AuthData newAuthData = createAuthData(loginUser.getUsername());

            // returns the new user and token
            return new loginPackage(newAuthData.getUsername(), newAuthData.getAuthToken());
        }else{
            // means that they entered the wrong password
            throw new DataAccessException("[401](Unauthorized) Invalid Password");
        }
    }

    // service functions
    private boolean comparePasswords(String enteredPassword,String userPassword){
        return enteredPassword.equals(userPassword);
    }

    public static class loginPackage{
        String username,authToken;
        public loginPackage(String username,String authToken){
            this.username = username;
            this.authToken = authToken;
        }
    }
}
