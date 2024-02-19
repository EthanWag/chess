package services;

import dataAccess.DataAccessException;
import models.*;

public class LoginService extends Service{

    public LoginService() {}

    public AuthData CompleteJob(String username, String password){
        try{

            User loginUser = UserDAO.read(username);

            // compares the two passwords
            if(comparePasswords(password,loginUser.getPassword())){
                return createAuthData(username);
            }else{
                System.err.println("Incorrect password");
                System.err.println("Try again");
                return null;
            }

        }catch(DataAccessException error){

            System.err.println("Invalid Username");
            System.err.println("Please re-enter");
            return null;

        }
    }

    // service functions

    private boolean comparePasswords(String enteredPassword,String userPassword){
        return enteredPassword.equals(userPassword);
    }
}

// complete for now
