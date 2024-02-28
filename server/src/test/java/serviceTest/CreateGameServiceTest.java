package serviceTest;

import dataAccess.DataAccessException;
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

        clearService.completeJob();

        try {

            String testUsername = "brad";
            String testPassword = "chimmysmmmmooo";
            String testEmail = "email.com";

            RegisterService.RegisterPackage testPackage = registerService.completeJob(testUsername, testPassword, testEmail);
            authToken = testPackage.authToken;

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
}