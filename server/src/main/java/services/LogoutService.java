package services;

import dataAccess.DataAccessException;
import models.*;

import javax.xml.crypto.Data;

public class LogoutService extends Service{

    public LogoutService() {}

    // returns if it is successful
    public void completeJob(String authToken)throws DataAccessException{

        AuthData userAuthToken = getAuthData(authToken);

        // deletes AuthData and lets the user know whether or not it was successful
        deleteAuthData(userAuthToken);

    }


    // service functions

    private void deleteAuthData(AuthData delAuthData)throws DataAccessException{
        String AuthToken = delAuthData.getAuthToken();
        AuthDAO.delete(AuthToken);
    }
}