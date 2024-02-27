package server;

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
            String result = registerUser.registerHandler(request);

            response.status(200);
            return result;

        });
        Spark.post("/session", (request, response) -> { // logs a new user into a game
            return "login method\n";
        });
        Spark.delete("/session", (request, response) -> { // logout, logs out a user from the chessgame
            return "logout method\n";
        });
        Spark.get("/game", (request, response) -> { // gives you a list of all games
            return "list games method\n";
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
