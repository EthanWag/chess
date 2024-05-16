package services;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.SqlUserDAO;
import server.WebSocketServer.WebSocketHandler;

import static java.lang.System.exit;

public class ClearApplicationService extends Service{

    SqlGameDAO gameAccess;
    SqlAuthDAO authAccess;
    SqlUserDAO userAccess;


    public ClearApplicationService() {

        try{
            gameAccess = new SqlGameDAO();
            authAccess = new SqlAuthDAO();
            userAccess = new SqlUserDAO();

        }catch(DataAccessException error){
            System.err.println("Unsuccessful in clearing database");
            exit(1);
        }

    }

    // service functions

    public void completeJob() throws DataAccessException{
        clearApplication();
    }

    private void clearApplication() throws DataAccessException {

        WebSocketHandler webSer = new WebSocketHandler();

        gameAccess.deleteAll();
        authAccess.deleteAll();
        userAccess.deleteAll();
        webSer.clearManager();

    }

}
