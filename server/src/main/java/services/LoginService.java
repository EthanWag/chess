package services;

import dataAccess.DataAccessException;
import dataAccess.SqlUserDAO;
import models.*;
import models.resModels.ResponseLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginService extends Service{

    private final SqlUserDAO userAccess;

    public LoginService()throws DataAccessException{
        try {
            userAccess = new SqlUserDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("Error: Unable to connect to the database",500);
        }
    }

    public ResponseLoginPackage completeJob(String username, String password) throws DataAccessException {

        User loginUser = userAccess.read(username);

        // compares the two passwords
        if(comparePasswords(password,loginUser.password())){

            // creates a authToken using the username
            AuthData newAuthData = createAuthData(loginUser.username());

            return new ResponseLoginPackage(newAuthData.username(), newAuthData.authToken());
        }else{
            throw new DataAccessException("ERROR: Invalid Password", 401);
        }
    }

    private boolean comparePasswords(String enteredPassword,String userPassword){
        BCryptPasswordEncoder decoder = new BCryptPasswordEncoder();
        return decoder.matches(enteredPassword,userPassword);
    }
}
