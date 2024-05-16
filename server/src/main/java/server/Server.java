package server;

import com.mysql.cj.log.Log;
import dataAccess.DataAccessException;
import services.handlers.*;
import server.WebSocketServer.WebSocketHandler;
import spark.*;

import javax.xml.crypto.Data;

import static java.lang.System.exit;


public class Server {

    // these are all global variables for each endpoint


    private ClearApplicationHandler clear;
    private RegisterHandler register;
    private LoginHandler login;
    private LogoutHandler logout;
    private ListGamesHandler allGames;
    private CreateGameHandler createGame;
    private JoinGameHandler joinGame;

    public Server(){
        try{
            clear = new ClearApplicationHandler();
            register = new RegisterHandler();
            login = new LoginHandler();
            logout = new LogoutHandler();
            allGames = new ListGamesHandler();
            createGame = new CreateGameHandler();
            joinGame = new JoinGameHandler();

        }catch(DataAccessException error){
            System.err.println("ERROR: Failure in building server");
            exit(1);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        installWebSocket();
        installEndPoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // This function creates all the endpoints
    private void installEndPoints(){

        Spark.delete("/db", (request, response) -> { // deletes everything from the database
            return clear.clearApplicationHandler(response);
        });

        Spark.post("/user", (request, response) -> { // register a new user
            return register.registerHandler(request,response);
        });

        Spark.post("/session", (request, response) -> { // logs a new user into a game
            return login.loginHandler(request,response);
        });

        Spark.delete("/session", (request, response) -> { // logout, logs out a user from the chessgame
            return logout.logoutHandler(request,response);
        });

        Spark.get("/game", (request, response) -> { // gives you a list of all games
            return allGames.listGamesHandler(request,response);
        });

        Spark.post("/game", (request, response) -> { // create game method
            return createGame.createGameHandler(request,response);
        });

        Spark.put("/game", (request, response) -> {
            return joinGame.joinGameHandler(request,response);
        });
        Spark.notFound("{Invalid Page}");
    }

    private void installWebSocket(){
        Spark.webSocket("/connect", WebSocketHandler.class);
    }
}
