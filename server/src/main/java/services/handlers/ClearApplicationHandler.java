package services.handlers;

import services.ClearApplicationService;
import spark.Request;
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

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }



}
