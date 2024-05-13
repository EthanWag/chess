package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import models.resModels.ResponseLoginPackage;
import services.LoginService;

import spark.Request;
import spark.Response;

public class LoginHandler {

    private static LoginService service;
    private static GsonConverterReq gsonConverter;
    private static ExceptionHandler exceptionHandler;

    public LoginHandler() throws DataAccessException{
        gsonConverter = new GsonConverterReq();
        exceptionHandler = new ExceptionHandler();

        // tries to connect the service to the database
        try{
            service = new LoginService();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Cannot connect to the database",500);
        }
    }

    public String loginHandler(Request request,Response response){
        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = gsonConverter.requestToObj(request, Login.class);
            Login newLogin = (Login)newObj;

            // then runs the program through the services and returns the newly created authToken
            ResponseLoginPackage newPackage = service.completeJob(newLogin.username, newLogin.password);

            // converts that string in to a response object and returns it
            response.status(200);
            return gsonConverter.objToJson(newPackage);

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err) {
            return exceptionHandler.handleException(err,response);
        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private static class Login {
        public String username,password;
        public Login(String username, String password){
            this.username = username;
            this.password = password;
        }
    }
}
