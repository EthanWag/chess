package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.SqlUserDAO;
import server.WebSocketServer.WebSocketHandler;

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

        WebSocketHandler webSer = new WebSocketHandler();

        gameAccess.deleteAll();
        authAccess.deleteAll();
        userAccess.deleteAll();
        webSer.clearManager();

        // commits all change
        gameAccess.commit();
        authAccess.commit();
        userAccess.commit();

    }

}
