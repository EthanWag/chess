package dataAccess;

import models.AuthData;

public class AuthDAO{

    public AuthDAO() {}

    public void create(AuthData newAuthData) {
        return;
    }

    public AuthData read(String authToken) throws DataAccessException {
        return null;
    }

    public void update() throws DataAccessException {}

    public boolean delete(AuthData delAuthData){

        return false;
        // code in here that removes it from the database
    }

    public void deleteAll(){return;}
}
