package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import models.*;

public class LogoutService extends Service{

    private final SqlAuthDAO authAccess;

    public LogoutService() throws DataAccessException{
        try{
            authAccess = new SqlAuthDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("Error: Unable to connect to the database",500);
        }
    }

    // returns if it is successful
    public void completeJob(String authToken)throws DataAccessException{

        // checks to see if it is a valid delete
        AuthData userAuthToken;
        try{
            userAuthToken = getAuthData(authToken);
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Invalid authorization",401);
        }
        deleteAuthData(userAuthToken);
    }

    private void deleteAuthData(AuthData delAuthData)throws DataAccessException{
        // grabs authToken from the object
        String authToken = delAuthData.authToken();
        authAccess.delete(authToken);
    }
}