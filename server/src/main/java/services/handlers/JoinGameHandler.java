package services.handlers;

import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    private static JoinGameService service = new JoinGameService();
    private static ConvertGson gsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public JoinGameHandler(){}

    public String joinGameHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // next grabs all the information from body
            var newObj = gsonConverter.requestToObj(request, JoinGame.class);
            JoinGame newGame = (JoinGame) newObj;

            // then runs the program through the services and returns the newly created authToken
            service.completeJob(authToken, newGame.playerColor, newGame.gameID);

            // converts that string in to a response object and returns it
            response.status(200);
            return "{}";

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.handleException(error,response);
        }
    }

    private static class JoinGame {
        public String playerColor;
        public int gameID;
        public JoinGame(String playerColor, int gameID){
            this.playerColor = playerColor;
            this.gameID = gameID;
        }
    }
}
