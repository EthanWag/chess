package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.CreateGameService;
import services.CreateGameService.GamePackage;

import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private static CreateGameService service = new CreateGameService();
    private static ConvertGson gsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public CreateGameHandler(){}

    public String createGameHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // next grabs all the information from body
            var newObj = gsonConverter.requestToObj(request, CreateGame.class);
            CreateGame newGame = (CreateGame) newObj;

            // then runs the program through the services and returns the newly created authToken
            GamePackage newPackage = service.completeJob(authToken, newGame.gameName);

            // converts that string in to a response object and returns it
            response.status(200);
            return gsonConverter.objToJson(newPackage);

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err){
            return exceptionHandler.handleException(err,response);
        }
    }

    private static class CreateGame {
        public String gameName;
        public CreateGame(String gameName){
            this.gameName = gameName;
        }
    }
}
