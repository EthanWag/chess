package dataAccess;

import java.util.HashMap;

import models.AuthData;

public class AuthDAO{

    HashMap<String,AuthData> allAuths;

    public AuthDAO() {
        allAuths = new HashMap<>();
    }

    public void create(AuthData newAuthData) {

        String AuthToken = newAuthData.getAuthToken();
        // maybe put some sort of code here to catch errors

        allAuths.put(AuthToken,newAuthData);
    }

    public AuthData read(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[400] Invalid AuthToken");}

        return allAuths.get(authToken);
    }

    public void update(String authToken, AuthData updateAuthData) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[400] Invalid AuthToken");}

        allAuths.put(authToken,updateAuthData);
    }

    public void delete(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[400] Invalid AuthToken");}

        allAuths.remove(authToken);
        // code in here that removes it from the database
    }

    public void deleteAll(){
        allAuths.clear();
    }
}
