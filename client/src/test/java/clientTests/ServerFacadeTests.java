package clientTests;

import models.reqModels.*;
import models.resModels.*;
import org.junit.jupiter.api.*;

import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerRequestTest() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestRegisterPackage newUser = new RequestRegisterPackage("timmy","booboo", "myEmail.com");
        var testValue = server.register(newUser);
        System.out.println("done");
    }

    @Test
    public void loginRequestSuccess() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestLoginPackage newLogin = new RequestLoginPackage("timmy","booboo");
        var testValue = server.login(newLogin);
        System.out.println("working");

    }

    @Test
    public void logoutRequestSuccess() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestRegisterPackage newUser = new RequestRegisterPackage("timmy","booboo", "myEmail.com");
        var testValue = server.register(newUser);

        String authToken = testValue.newAuthToken();

        server.logout(authToken);

        System.out.println("working");

    }

    @Test
    public void createGameSuccess() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestRegisterPackage newUser = new RequestRegisterPackage("timmy","booboo", "myEmail.com");
        var testValue = server.register(newUser);

        String authToken = testValue.newAuthToken();

        RequestCreatePackage newGame = new RequestCreatePackage("my new game!!!''''' ");
        var test = server.createGame(newGame,authToken);

        System.out.println("working");

    }

    @Test
    public void listGameSuccess() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestRegisterPackage newUser = new RequestRegisterPackage("timmy","booboo", "myEmail.com");
        var testValue = server.register(newUser);

        String authToken = testValue.newAuthToken();

        var test = server.listGame(authToken);

        System.out.println("working");

    }


    @Test
    public void joinGameSuccess() {

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        RequestRegisterPackage newUser = new RequestRegisterPackage("timmy","booboo", "myEmail.com");
        var testValue = server.register(newUser);

        String authToken = testValue.newAuthToken();

        // makes the game
        RequestCreatePackage newGame = new RequestCreatePackage("my new game!!!''''' ");
        var test = server.createGame(newGame,authToken);

        // joins the game
        RequestJoinPackage joinGame = new RequestJoinPackage("WHITE",1);
        server.joinGame(joinGame,authToken);

        System.out.println("working");
    }


}
