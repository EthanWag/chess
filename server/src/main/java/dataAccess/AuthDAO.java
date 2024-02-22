package dataAccess;

import java.util.HashMap;

import models.AuthData;

public class AuthDAO{

    HashMap<String,AuthData> allAuths;

    public AuthDAO() {
        allAuths = new HashMap<>();
    }

    public void create(AuthData newAuthData) throws DataAccessException {

        String authToken = newAuthData.getAuthToken();
        // maybe put some sort of code here to catch errors

        // makes sure that you already haven't made that token
        if(allAuths.containsKey(authToken)){ throw new DataAccessException("[403] Already Taken");}

        allAuths.put(authToken,newAuthData);
    }

    public AuthData read(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[404] Not Found");}

        return allAuths.get(authToken);
    }

    public void update(String authToken, AuthData updateAuthData) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[404] Not Found");}

        allAuths.put(authToken,updateAuthData);
    }

    public void delete(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[404] Not Found");}

        allAuths.remove(authToken);
        // code in here that removes it from the database
    }

    public void deleteAll(){
        allAuths.clear();
    }
}
