package services.handlers;

import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    private static JoinGameService service = new JoinGameService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public JoinGameHandler(){}

    public String joinGameHandler(Request request,Response response){
        try {

            // finds the authorization token and puts it into the completeJob function
            String authToken = request.headers("authorization");

            // next grabs all the information from body
            var newObj = GsonConverter.RequestToObj(request, JoinGameHandler.joinGame.class);
            JoinGameHandler.joinGame newGame = (JoinGameHandler.joinGame) newObj;

            // then runs the program through the services and returns the newly created authToken
            service.completeJob(authToken, newGame.playerColor, newGame.gameID);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson("Success!");

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }

    private static class joinGame{
        public String playerColor;
        public int gameID;
        public joinGame(String playerColor,int gameID){
            this.playerColor = playerColor;
            this.gameID = gameID;
        }
    }
}
