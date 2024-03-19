package services;

import dataAccess.DataAccessException;
import dataAccess.SqlUserDAO;
import models.*;
import models.resModels.ResponseLoginPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginService extends Service{

    public LoginService() {}

    public ResponseLoginPackage completeJob(String username, String password) throws DataAccessException {

        var userAccess = new SqlUserDAO();
        User loginUser = userAccess.read(username);
        userAccess.close();


        // compares the two passwords
        if(comparePasswords(password,loginUser.password())){

            // creates a authToken using the username
            AuthData newAuthData = createAuthData(loginUser.username());

            // returns the new user and token
            return new ResponseLoginPackage(newAuthData.username(), newAuthData.authToken());
        }else{
            // means that they entered the wrong password
            throw new DataAccessException("ERROR:Invalid Password", 401);
        }
    }

    // service functions
    private boolean comparePasswords(String enteredPassword,String userPassword){
        BCryptPasswordEncoder decoder = new BCryptPasswordEncoder();
        return decoder.matches(enteredPassword,userPassword);
    }
}
