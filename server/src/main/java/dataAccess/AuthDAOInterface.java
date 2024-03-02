package dataAccess;

import models.AuthData;


public interface AuthDAOInterface {

    // public methods
    public void create(AuthData newAuthData) throws DataAccessException;
    public AuthData read(String authToken) throws DataAccessException;
    public void delete(String authToken) throws DataAccessException;
    // possibly put an update function here
    public void deleteAll();

}
