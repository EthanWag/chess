package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import models.resModels.ResponseListPackage;
import services.ListGamesService;

import spark.Request;
import spark.Response;

public class ListGamesHandler {
    private static ListGamesService service;
    private static GsonConverterReq gsonConverter;
    private static ExceptionHandler exceptionHandler;

    public ListGamesHandler()throws DataAccessException{
        gsonConverter = new GsonConverterReq();
        exceptionHandler = new ExceptionHandler();

        try{
            service = new ListGamesService();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Cannot connect to the database",500);
        }
    }

    public String listGamesHandler(Request request, Response response){
        try {
            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // then runs the program through the services and returns the newly created authToken
            ResponseListPackage newPackage = service.completeJob(authToken);

            // converts that string in to a response object and returns it
            response.status(200);
            return gsonConverter.objToJson(newPackage);

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err){
            return exceptionHandler.handleException(err,response);
        }
    }
}
