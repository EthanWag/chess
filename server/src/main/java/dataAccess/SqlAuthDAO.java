package dataAccess;

import models.AuthData;

public class SqlAuthDAO implements AuthDAO{


    public void create(AuthData newAuthData) throws DataAccessException{

    }
    public AuthData read(String authToken) throws DataAccessException{
        return null;
    }
    public void delete(String authToken) throws DataAccessException{

    }
    // possibly put an update function here
    public void deleteAll(){

    }


}
