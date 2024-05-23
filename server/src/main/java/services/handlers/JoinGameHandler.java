package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.JoinGameService;
import services.ListGamesService;
import spark.Request;
import spark.Response;

public class JoinGameHandler{

    private static JoinGameService service;
    private static GsonConverterReq gsonConverter;
    private static ExceptionHandler exceptionHandler;

    public JoinGameHandler() throws DataAccessException{
        gsonConverter = new GsonConverterReq();
        exceptionHandler = new ExceptionHandler();

        try{
            service = new JoinGameService();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Cannot connect to the database",500);
        }
    }

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

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err) {
            return exceptionHandler.handleException(err,response);
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
