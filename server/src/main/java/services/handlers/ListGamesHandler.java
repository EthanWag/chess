package services.handlers;

import services.ListGamesService;
import services.ListGamesService.gamesPackage;

import spark.Request;
import spark.Response;

public class ListGamesHandler {
    private static ListGamesService service = new ListGamesService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public ListGamesHandler(){}

    public String ListGamesHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // then runs the program through the services and returns the newly created authToken
            ListGamesService.gamesPackage newPackage = service.completeJob(authToken);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson(newPackage);

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }










}
