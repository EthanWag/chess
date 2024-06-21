package server;

import dataAccess.DataAccessException;
import services.handlers.*;
import server.WebSocketServer.WebSocketHandler;
import spark.*;
import static spark.Spark.*;
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
        installWebSocket();

        Spark.staticFiles.location("web");

        // Sets up CORS filter
        before((request,response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        // Handle OPTIONS requests
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // Register your endpoints and handle exceptions here.
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
            response.type("application/json");
            return clear.clearApplicationHandler(response);
        });

        Spark.post("/user", (request, response) -> { // register a new user
            response.type("application/json");
            return register.registerHandler(request,response);
        });

        Spark.post("/session", (request, response) -> { // logs a new user into a game
            response.type("application/json");
            return login.loginHandler(request,response);
        });

        Spark.delete("/session", (request, response) -> { // logout, logs out a user from the chessgame
            response.type("application/json");
            return logout.logoutHandler(request,response);
        });

        Spark.get("/game", (request, response) -> { // gives you a list of all games
            response.type("application/json");
            return allGames.listGamesHandler(request,response);
        });

        Spark.post("/game", (request, response) -> { // create game method
            response.type("application/json");
            return createGame.createGameHandler(request,response);
        });

        Spark.put("/game", (request, response) -> {
            response.type("application/json");
            return joinGame.joinGameHandler(request,response);
        });
        Spark.notFound("{Invalid Page}");
    }

    private void installWebSocket(){
        Spark.webSocket("/connect", WebSocketHandler.class);
    }
}
