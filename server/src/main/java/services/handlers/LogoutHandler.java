package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.LoginService;
import services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    private static LogoutService service;
    private static ExceptionHandler exceptionHandler;

    public LogoutHandler() throws DataAccessException{
        exceptionHandler = new ExceptionHandler();

        try{
            service = new LogoutService();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Cannot connect to the database",500);
        }
    }

    public String logoutHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // then runs the program through the services and returns the newly created authToken
            service.completeJob(authToken);

            // converts that string in to a response object and returns it
            response.status(200);
            return "{}";

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err) {
            return exceptionHandler.handleException(err,response);
        }
    }
}
