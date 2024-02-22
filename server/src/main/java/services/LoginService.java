package services;

import dataAccess.DataAccessException;
import models.*;

public class LoginService extends Service{

    public LoginService() {}

    public AuthData CompleteJob(String username, String password) throws DataAccessException {


        User loginUser = UserDAO.read(username);

        // compares the two passwords
        if(comparePasswords(password,loginUser.getPassword())){
            // creates a authToken using the username
            return createAuthData(loginUser.getUsername());
        }else{
            // means that they entered the wrong password
            throw new DataAccessException("[401] unauthorized (Invalid password)");
        }
    }

    // service functions
    private boolean comparePasswords(String enteredPassword,String userPassword){
        return enteredPassword.equals(userPassword);
    }
}
