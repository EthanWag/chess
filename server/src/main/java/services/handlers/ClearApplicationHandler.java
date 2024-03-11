package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.ClearApplicationService;
import spark.Response;

public class ClearApplicationHandler {


    private static ClearApplicationService service = new ClearApplicationService();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public String clearApplicationHandler(Response response){
        try {

            // then runs the program through the services and returns the newly created authToken
            service.completeJob();

            // converts that string in to a response object and returns it
            response.status(200);
            return "{}"; // returns an empty Json object

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err){
            return exceptionHandler.handleException(err,response);
        }
    }
}
