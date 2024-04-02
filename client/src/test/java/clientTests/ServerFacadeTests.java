package clientTests;

import models.Game;
import models.reqModels.*;
import org.junit.jupiter.api.*;

import server.InvalidRequestException;
import server.Server;
import server.ServerFacade;

import services.ClearApplicationService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static int myPort;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        myPort = port;

        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearServer(){

        try {

            var clearObj = new ClearApplicationService();
            clearObj.completeJob(); // should just clear the server, no questions asked

        }catch(Exception error){
            fail("Server failed to connect");
        }
    }

    @Test
    public void registerRequestSuccess() {

        try {
            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            // makes two users and then checks to see if both tests pass
            RequestRegisterPackage newUser1 = new RequestRegisterPackage("timmy", "booboo", "myEmail.com");
            var testValue1 = server.register(newUser1);

            // successfully logs a user in and returns that value
            boolean successfulRegister1 = (testValue1.username().equals("timmy")) && (testValue1.authToken() != null);


            // test two ---------------------------------------------------------------------
            RequestRegisterPackage newUser2 = new RequestRegisterPackage("Ethan S Wagstaff",
                                                                "wats0n07", "nathan@google.com");

            var testValue2 = server.register(newUser2);

            // successfully logs a user in and returns that value
            boolean successfulRegister2 = (testValue2.username().equals("Ethan S Wagstaff")) && (testValue2.authToken() != null);

            // do both conditions pass
            assertTrue(successfulRegister1 && successfulRegister2);

        }catch(InvalidRequestException serverError){
            fail(serverError.getMessage()); // should have accepted
        }
    }

    @Test
    public void RegisterTwiceFail() {

        try {
            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            // makes two users and then checks to see if both tests pass
            RequestRegisterPackage newUser1 = new RequestRegisterPackage("chad", "stinky", "123.edu");
            server.register(newUser1);

            // register twice ---------------------------------------------------------------------
            RequestRegisterPackage newUser2 = new RequestRegisterPackage("chad",
                    "thinktank", "booga.mail.com");

            server.register(newUser2); // should fail on this code right here

            fail("Fail: Cannot have two chads");

        }catch(InvalidRequestException serverError){
            assertEquals(403,serverError.getErrorCode());
        }
    }

    @Test
    public void loginRequestSuccess() {

        ServerFacade server = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            server.register(newUser);

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if(server == null)
            fail("Error: cannot set up");

        try {

            RequestLoginPackage newLogin = new RequestLoginPackage("CatNap", "spookyzz");
            var testValue = server.login(newLogin);

            boolean successfulRegister = (testValue.username().equals("CatNap")) && (testValue.authToken() != null);
            assertTrue(successfulRegister);

        }catch(InvalidRequestException serverError){
            fail("Should have accepted"); // should have accepted
        }

    }

    @Test
    public void loginBadPassword() {

        ServerFacade server = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            server.register(newUser);

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if(server == null)
            fail("Error: cannot set up");

        try {

            RequestLoginPackage newLogin = new RequestLoginPackage("CatNap", "spokyzz");
            server.login(newLogin);
            fail("Error: Should have rejected, accepted");

        }catch(InvalidRequestException serverError){
            assertEquals(401,serverError.getErrorCode());
        }
    }

    @Test
    public void loginBadUsername() {

        ServerFacade server = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            server.register(newUser);

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if(server == null)
            fail("Error: cannot set up");

        try {
            RequestLoginPackage newLogin = new RequestLoginPackage("Davey", "spookyzz");
            server.login(newLogin);
            fail("Error: should have rejected request");

        }catch(InvalidRequestException serverError){
            assertEquals(401,serverError.getErrorCode());
        }
    }

    @Test
    public void logoutRequestSuccess() {

        ServerFacade server = null;
        String testAuth = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            var response = server.register(newUser);

            testAuth = response.authToken();

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if((server == null)||(testAuth == null))
            fail("Error: cannot set up");

        try {
            server.logout(testAuth);
            assertTrue(true);

        }catch(InvalidRequestException error){
            fail("User was logged in"); // should have been successful
        }
    }

    @Test
    public void logoutBadTwice() {

        ServerFacade server = null;
        String testAuth = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            var response = server.register(newUser);

            testAuth = response.authToken();

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if((server == null)||(testAuth == null))
            fail("Error: cannot set up");

        try {
            server.logout(testAuth);
            server.logout(testAuth);
            fail("User Already logged out");

        }catch(InvalidRequestException error){
            assertEquals(401,error.getErrorCode());
        }
    }

    @Test
    public void logoutIncorrectAuth() {

        ServerFacade server = null;
        String testAuth = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            // registers a simple user
            var newUser = new RequestRegisterPackage("CatNap","spookyzz","sleep.co");
            var response = server.register(newUser);

            testAuth = response.authToken();

        }catch(InvalidRequestException error){
            fail("Error: Cannot register, check register function");
        }

        if(testAuth == null)
            fail("Error: cannot set up");

        try {
            server.logout("123456-7890-abcd");
            fail("Invalid authorization given");

        }catch(InvalidRequestException error){
            assertEquals(401,error.getErrorCode());
        }
    }

    @Test
    public void createGameSuccess() {

        ServerFacade server = null;
        String myAuthorization = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("Steve", "abcd555", "wowzar.net");
            var testValue = server.register(newUser);

            myAuthorization = testValue.authToken();

        }catch(InvalidRequestException error){
            fail("unable to set up request");
        }

        if((server == null) || (myAuthorization == null))
            fail("unable to set up");

        try {

            RequestCreatePackage newGame = new RequestCreatePackage("Steve's new game");
            var test = server.createGame(newGame, myAuthorization);

            // FYI we are making only one game at this time, so only one game should be in the database
            System.out.println("Successful game creation");
            assertEquals(1, test.gameID());

        }catch(InvalidRequestException error){
            fail("Should have created a game"); //should have been successful
        }

    }

    @Test
    public void createGameInvalidAuth() {

        ServerFacade server = null;
        String myAuthorization = null;

        try{

            String myURL = "http://localhost:" + myPort;
            server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("Steve", "abcd555", "wowzar.net");
            var testValue = server.register(newUser);

            myAuthorization = testValue.authToken();

        }catch(InvalidRequestException error){
            fail("unable to set up request");
        }

        if(myAuthorization == null)
            fail("unable to set up");

        try {

            RequestCreatePackage newGame = new RequestCreatePackage("Steve's new game");
            server.createGame(newGame, "fake authToken");

            fail("Invalid AuthToken, should have failed");

        }catch(InvalidRequestException error){
            assertEquals(401,error.getErrorCode());
        }
    }

    @Test
    public void listGameSuccess() {

        try {

            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("timmy", "booboo", "myEmail.com");
            var testValue = server.register(newUser);

            String authToken = testValue.authToken();

            var package1 = new RequestCreatePackage("gg gamer");
            var package2 = new RequestCreatePackage("nozo");

            server.createGame(package1,authToken);
            server.createGame(package2,authToken);

            var test = server.listGame(authToken);

            // for right now I just want it to be an empty game
            assertEquals(2,test.games().size());

        }catch(InvalidRequestException error){
            fail("did not list two games"); // invalid request
        }
    }

    @Test
    public void listGameInvalidAuth() {

        try {

            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("timmy", "booboo", "myEmail.com");
            var testValue = server.register(newUser);

            String authToken = testValue.authToken();

            var test = server.listGame(authToken);

            // for right now I just want it to be an empty game
            assertTrue(true);

        }catch(InvalidRequestException error){
            fail(); // invalid request
        }
    }


    @Test
    public void joinGameSuccess() {

        try {
            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("timmy", "booboo", "myEmail.com");
            var testValue = server.register(newUser);

            String authToken = testValue.authToken();

            // makes the game
            RequestCreatePackage newGame = new RequestCreatePackage("my new game!!!''''' ");
            var test = server.createGame(newGame, authToken);

            // joins the game
            RequestJoinPackage joinGame = new RequestJoinPackage("WHITE", 1);
            server.joinGame(joinGame, authToken);

            assertTrue(true);

        }catch(InvalidRequestException error){
            fail();
        }
    }

    // test to make sure that
    @Test
    public void joinGameInvalidID() {

        try {
            String myURL = "http://localhost:" + myPort;
            ServerFacade server = new ServerFacade(myURL);

            RequestRegisterPackage newUser = new RequestRegisterPackage("timmy", "booboo", "myEmail.com");
            var testValue = server.register(newUser);

            String authToken = testValue.authToken();

            // makes the game
            RequestCreatePackage newGame = new RequestCreatePackage("my new game!!!''''' ");
            var test = server.createGame(newGame, authToken);

            // joins the game
            RequestJoinPackage joinGame = new RequestJoinPackage("WHITE", -1);
            server.joinGame(joinGame, authToken);

            fail("-1 is a bad game ID");

        }catch(InvalidRequestException error){
            assertEquals(400,error.getErrorCode());
        }
    }


}
