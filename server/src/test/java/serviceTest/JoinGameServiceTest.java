package serviceTest;

import dataAccess.DataAccessException;
import models.resModels.ResponseRegisterPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearApplicationService;
import services.*;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {

    private ClearApplicationService clearService;
    private RegisterService registerService;
    private CreateGameService createGameService;
    private JoinGameService testJoinGame;
    private String authToken;
    private int gameId;

    public JoinGameServiceTest() {

        createGameService = new CreateGameService();
        clearService = new ClearApplicationService();
        registerService = new RegisterService();
        testJoinGame = new JoinGameService();
    }

    @BeforeEach
    public void reset(){

        try {

            clearService.completeJob();

            String testUsername = "brad";
            String testPassword = "chimmysmmmmooo";
            String testEmail = "email.com";

            ResponseRegisterPackage testPackage = registerService.completeJob(testUsername, testPassword, testEmail);
            authToken = testPackage.authToken();

            CreateGameService.GamePackage gamePackage = createGameService.completeJob(authToken,"newGame");
            gameId = gamePackage.gameID;

        }catch(DataAccessException error){
            System.out.println("Failure in building test");
        }
    }

    @Test
    void joinGameSuccess() {
        try{
            testJoinGame.completeJob(authToken,"WHITE", gameId);
            assertTrue(true);
        }catch(Exception error){
            fail();
        }
    }

    @Test
    void joinGameSameColor() {
        try{
            testJoinGame.completeJob(authToken,"BLACK", gameId);
            testJoinGame.completeJob(authToken,"BLACK", gameId);
            fail();
        }catch(Exception error){
            assertTrue(true);
        }
    }
}