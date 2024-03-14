package clientTests;

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
    public void basicRequest() {

        String myURL = "http://localhost:8080";

        ServerFacade server = new ServerFacade(myURL);
        server.register();


    }

}
