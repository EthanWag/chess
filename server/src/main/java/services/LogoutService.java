package services;

import dataAccess.DataAccessException;
import models.*;

import javax.xml.crypto.Data;

public class LogoutService extends Service{

    public LogoutService() {}

    // returns if it is successful
    public boolean completeJob(String Authtoken){
        try{

            AuthData userAuthToken = getAuthData(Authtoken);

            // deletes AuthData and lets the user know whether or not it was successful
            return deleteAuthData(userAuthToken);

        }catch(DataAccessException error){

            // Note: subject to change
            System.err.println("Invalid user data");
            System.err.println("Please try again");
            return false;

        }
    }


    // service functions

    // getAuthData is in Service class

    private boolean deleteAuthData(AuthData delAuthData){

        return AuthDAO.delete(delAuthData);

        // put some code in here that tells the user that they gave an invald account name
    }
}

// complete for now