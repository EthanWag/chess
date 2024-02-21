package dataAccess;

import java.util.ArrayList;

import models.AuthData;

public class AuthDAO{

    ArrayList<AuthData> AllAuths;

    public AuthDAO() {
        AllAuths = new ArrayList<>();
    }

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
