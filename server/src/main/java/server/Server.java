package server;

import services.LoginService;
import services.handlers.*;


import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        installFilter();
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
            return "Clear application method\n";
        });

        Spark.post("/user", (request, response) -> { // register a new user

            // makes a new handler and sends it to the handler function
            RegisterHandler registerUser = new RegisterHandler();
            return registerUser.registerHandler(request,response);

        });
        Spark.post("/session", (request, response) -> { // logs a new user into a game

            // does request and sends it back to the server
            LoginHandler loginUser = new LoginHandler();
            return loginUser.loginHandler(request,response);

        });
        Spark.delete("/session", (request, response) -> { // logout, logs out a user from the chessgame

            LogoutHandler logoutUser = new LogoutHandler();
            return logoutUser.logoutHandler(request,response);

        });
        Spark.get("/game", (request, response) -> { // gives you a list of all games

            ListGamesHandler allGames = new ListGamesHandler();
            return allGames.ListGamesHandler(request,response);

        });
        Spark.post("/game", (request, response) -> { // create game method
            return "create game method\n";
        });
        Spark.put("/game", (request, response) -> {
            return "join game method\n";
        });
        Spark.notFound("<html><body>hello user, invalid response </body></html>");
    }

    private void installFilter(){
        Spark.before((request,response) -> {
            // you would put code here to check for valid Id's
        });
    }

}
