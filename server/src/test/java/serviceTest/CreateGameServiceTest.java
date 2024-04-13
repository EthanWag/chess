package serviceTest;

import dataAccess.DataAccessException;
import models.resModels.ResponseRegisterPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearApplicationService;
import services.CreateGameService;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {


    private ClearApplicationService clearService;
    private RegisterService registerService;
    private CreateGameService testCreateGame;
    private String authToken;

    public CreateGameServiceTest() {

        testCreateGame = new CreateGameService();
        clearService = new ClearApplicationService();
        registerService = new RegisterService();
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

        }catch(DataAccessException error){
            System.out.println("Failure in building test");
        }
    }


    @Test
    void completeJobSuccess() {
        try{
            testCreateGame.completeJob(authToken,"newGame");
            assertTrue(true);
        }catch(Exception error){
            fail();
        }
    }

    @Test
    void completeJobBadToken() {
        try{
            testCreateGame.completeJob("fakeToken","newGame2");
            fail();
        }catch(Exception error){
            assertTrue(true);
        }
    }

    @Test
    public void clearService(){

        try {

            for (int i = 0; i < 40; i++) {
                testCreateGame.completeJob(authToken, "newGame" + i);
            }

            // System.out.println("breakpoint");

            clearService.completeJob();

            // System.out.println("breakpoint");

            assertTrue(true);
        }catch(Exception error){
            System.err.println("failed to load all games");
            fail();
        }
    }
}