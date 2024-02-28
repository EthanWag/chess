package serviceTest;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearApplicationService;
import services.*;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest{
    private LogoutService testLogout;
    private ClearApplicationService clearService;
    private RegisterService registerService;

    String testToken;
    public LogoutServiceTest() {

        testLogout = new LogoutService();
        clearService = new ClearApplicationService();
        registerService = new RegisterService();
    }

    @BeforeEach
    public void reset(){

        clearService.completeJob();

        try {

            String testUsername = "brad";
            String testPassword = "123456";
            String testEmail = "email.com";

            RegisterService.RegisterPackage testPackage = registerService.completeJob(testUsername, testPassword, testEmail);
            testToken = testPackage.authToken;


        }catch(DataAccessException error){
            System.out.println("Failure in building test");
        }
    }

    @Test
    public void successfulLogout() {
        try {
            testLogout.completeJob(testToken);
            assertTrue(true);
        }catch(DataAccessException error){
            fail();
        }
    }

    @Test
    public void invalidAuthToken() {
        try {
            testLogout.completeJob("madeupToken");
            fail();
        }catch(DataAccessException error){
            assertTrue(true);
        }
    }
}