package services.handlers;

import services.CreateGameService;
import services.CreateGameService.gamePackage;

import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private static CreateGameService service = new CreateGameService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public CreateGameHandler(){}

    public String createGameHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // next grabs all the information from body
            var newObj = GsonConverter.RequestToObj(request, CreateGameHandler.createGame.class);
            CreateGameHandler.createGame newGame = (CreateGameHandler.createGame) newObj;

            // then runs the program through the services and returns the newly created authToken
            CreateGameService.gamePackage newPackage = service.completeJob(authToken, newGame.gameName);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson(newPackage);

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }

    private static class createGame{
        public String gameName;
        public createGame(String gameName){
            this.gameName = gameName;
        }
    }
}
