package server;

import dataAccess.DataAccessException;
import services.handlers.*;
import server.WebSocketServer.WebSocketHandler;
import spark.*;

import javax.xml.crypto.Data;


public class Server {

    // these are all global variables for each endpoint

    public Server(){

    }

    private ClearApplicationHandler clear;
    private RegisterHandler register;
    private LoginHandler login;
    private LogoutHandler logout;
    private ListGamesHandler allGames;
    private CreateGameHandler createGame;
    private JoinGameHandler joinGame;


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
        closeConnectionToDB();
        Spark.awaitStop();
    }

    // This function creates all the endpoints
    private void installEndPoints(){

        Spark.delete("/db", (request, response) -> { // deletes everything from the database

            clear = new ClearApplicationHandler();
            return clear.clearApplicationHandler(response);

        });

        Spark.post("/user", (request, response) -> { // register a new user

            // TODO: in here you need to respond by telling the user that they cannot connect to the database

            // makes a new handler and sends it to the handler function
            register = new RegisterHandler();
            return register.registerHandler(request,response);

        });


        Spark.post("/session", (request, response) -> { // logs a new user into a game

            // does request and sends it back to the server
            login = new LoginHandler();
            return login.loginHandler(request,response);

        });


        Spark.delete("/session", (request, response) -> { // logout, logs out a user from the chessgame

            logout = new LogoutHandler();
            return logout.logoutHandler(request,response);

        });


        Spark.get("/game", (request, response) -> { // gives you a list of all games

            allGames = new ListGamesHandler();
            return allGames.listGamesHandler(request,response);

        });


        Spark.post("/game", (request, response) -> { // create game method

            createGame = new CreateGameHandler();
            return createGame.createGameHandler(request,response);

        });


        Spark.put("/game", (request, response) -> {
            // this allows the user to join the game
            joinGame = new JoinGameHandler();
            return joinGame.joinGameHandler(request,response);

        });
        Spark.notFound("{Invalid Page}");
    }

    private void installWebSocket(){
        Spark.webSocket("/connect", WebSocketHandler.class);
    }

    private void closeConnectionToDB(){
        try{
            register.closeConnection();
        }catch(DataAccessException error){
            System.err.println("An error has occurred while closing");
        }
    }
}
