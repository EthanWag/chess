package services;

import models.*;
import dataAccess.*;

public abstract class Service {

    protected static UserDAO UserDAO = new UserDAO();
    protected static AuthDAO AuthDAO = new AuthDAO();
    protected static GameDAO GameDAO = new GameDAO();

    protected AuthData getAuthData(String authToken) throws DataAccessException{
        return null;
    }

    protected User getUser(String username){
        return null;
    }

}
