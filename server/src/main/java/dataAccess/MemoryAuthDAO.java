package dataAccess;

import java.util.HashMap;

import models.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    HashMap<String,AuthData> allAuths;

    public MemoryAuthDAO() {
        allAuths = new HashMap<>();
    }

    public void create(AuthData newAuthData) throws DataAccessException {

        String authToken = newAuthData.getAuthToken();
        // maybe put some sort of code here to catch errors

        // makes sure that you already haven't made that token
        if(allAuths.containsKey(authToken)){ throw new DataAccessException("[403](Used Auth)(AuthDAO) Auth already taken",403);}

        allAuths.put(authToken,newAuthData);
    }

    public AuthData read(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[401](Auth Not Found)(AuthDAO) Not Found",401);}

        return allAuths.get(authToken);
    }

    public void delete(String authToken) throws DataAccessException {

        if(!allAuths.containsKey(authToken)){ throw new DataAccessException("[404](Auth Not Found) Not Found",404);}

        allAuths.remove(authToken);
        // code in here that removes it from the database
    }

    public void deleteAll(){
        allAuths.clear();
    }
}
