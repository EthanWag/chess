package serviceTest;

import dataAccess.DataAccessException;
import models.resModels.ResponseRegisterPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearApplicationService;
import services.CreateGameService;
import services.ListGamesService;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

class ListGamesServiceTest {


    private ClearApplicationService clearService;
    private RegisterService registerService;
    private CreateGameService createGameService;
    private ListGamesService testListGames;
    private String authToken;

    public ListGamesServiceTest(){
        clearService = new ClearApplicationService();
        registerService = new RegisterService();
        createGameService = new CreateGameService();
        testListGames = new ListGamesService();
    }


    @BeforeEach
    public void reset(){

        try {

            clearService.completeJob();

            String testUsername = "brad";
            String testPassword = "chimmysmmmmooo";
            String testEmail = "email.com";

            ResponseRegisterPackage testPackage = registerService.completeJob(testUsername, testPassword, testEmail);
            authToken = testPackage.newAuthToken();

            createGameService.completeJob(authToken,"newGame1");
            createGameService.completeJob(authToken,"newGame2");
            createGameService.completeJob(authToken,"newGame3");

        }catch(DataAccessException error){
            System.out.println("Failure in building test");
        }
    }

    @Test
    void ListGamesSuccessful() {
        try{
            testListGames.completeJob(authToken);
            assertTrue(true);

        }catch(Exception error){
            fail();
        }
    }

    @Test
    void ListGamesInvalidAuth() {
        try{
            testListGames.completeJob("nonAuth");
            fail();

        }catch(Exception error){
            assertTrue(true);
        }
    }
}