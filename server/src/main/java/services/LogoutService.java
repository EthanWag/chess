package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import models.*;

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

        // grabs authToken from the object
        String authToken = delAuthData.getAuthToken();

        // selects it from the database and commits changes
        var authAccess = new SqlAuthDAO();
        authAccess.delete(authToken);
        authAccess.commit();
    }
}