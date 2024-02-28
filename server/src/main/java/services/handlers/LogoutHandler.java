package services.handlers;

import services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    private static LogoutService service = new LogoutService();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public String logoutHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // then runs the program through the services and returns the newly created authToken
            service.completeJob(authToken);

            // converts that string in to a response object and returns it
            response.status(200);
            return "{}";

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.handleException(error,response);
        }
    }
}
