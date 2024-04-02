package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.ListGamesService;
import services.ListGamesService.GamesPackage;

import spark.Request;
import spark.Response;

public class ListGamesHandler {
    private static ListGamesService service = new ListGamesService();
    private static GsonConverterReq gsonConverter = new GsonConverterReq();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public ListGamesHandler(){}

    public String listGamesHandler(Request request, Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // then runs the program through the services and returns the newly created authToken
            GamesPackage newPackage = service.completeJob(authToken);

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
