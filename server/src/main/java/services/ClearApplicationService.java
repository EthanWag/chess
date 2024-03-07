package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.SqlUserDAO;

public class ClearApplicationService extends Service{

    public ClearApplicationService() {}

    // service functions

    public void completeJob() throws DataAccessException{
        clearApplication();
    }

    private void clearApplication() throws DataAccessException {

        var gameAccess = new SqlGameDAO();
        var authAccess = new SqlAuthDAO();
        var userAccess = new SqlUserDAO();

        gameAccess.deleteAll();
        authAccess.deleteAll();
        userAccess.deleteAll();

        // commits all change
        gameAccess.commit();
        authAccess.commit();
        userAccess.commit();

    }

}
