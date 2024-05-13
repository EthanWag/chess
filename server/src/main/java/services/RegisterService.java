package services;

import dataAccess.SqlUserDAO;
import dataAccess.DataAccessException;
import models.*;
import models.resModels.ResponseRegisterPackage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Objects;

public class RegisterService extends Service{

    private final SqlUserDAO userAccess;

    public RegisterService() throws DataAccessException{
        try {
            userAccess = new SqlUserDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("Error: Unable to connect to the database",500);
        }
    }

    public ResponseRegisterPackage completeJob(String username, String password, String email)throws DataAccessException{

        // first checks to see valid input
        if(!checkRegister(username,password,email)){
            throw new DataAccessException("ERROR:Bad request", 400);
        }

        AuthData authorization = createUser(username,password,email);
        return new ResponseRegisterPackage(authorization.username(),authorization.authToken());
    }

    // closes connection to database, should only be called when the server is being closed
    public void closeConnection() throws DataAccessException{
        try {
            userAccess.close();
        }catch(DataAccessException error){
            throw new DataAccessException("Error: Unable to close connection to database",500);
        }
    }


    // Private methods for the program to use
    private AuthData createUser(String username, String password, String email)throws DataAccessException{

        // put password shuffle here
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        // makes user and puts it into the database
        User newUser = new User(username,hashedPassword,email);
        userAccess.create(newUser);

        return createAuthData(username); // returns newly created authToken
    }

    private boolean checkRegister(String username, String password, String email){
        return !((username == null) || (password == null) || (email == null));
    }
}
