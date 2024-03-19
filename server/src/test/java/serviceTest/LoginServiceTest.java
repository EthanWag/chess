package serviceTest;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearApplicationService;
import models.resModels.ResponseRegisterPackage;
import services.*;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private LoginService testLoginService;
    private LogoutService logout;
    private ClearApplicationService clearService;
    private RegisterService registerService;
    String testUsername = "";
    String testPassword = "";

    String testToken;
    public LoginServiceTest() {

        logout = new LogoutService();
        clearService = new ClearApplicationService();
        registerService = new RegisterService();
        testLoginService = new LoginService();
    }

    @BeforeEach
    public void reset(){

        try {
            clearService.completeJob();

            String fakeUsername = "chadwin";
            String fakePassword = "mypassword";
            String fakeEmail = "eswagner@outlook.com";

            testUsername = fakeUsername;
            testPassword = fakePassword;


            ResponseRegisterPackage testPackage = registerService.completeJob(fakeUsername, fakePassword, fakeEmail);
            testToken = testPackage.authToken();

            logout.completeJob(testToken);


        }catch(Exception error){
            System.out.println("Failure in building test");
        }
    }

    @Test
    public void successfulLogout() {
        try {
            testLoginService.completeJob(testUsername,testPassword);
            assertTrue(true);
        }catch(DataAccessException error){
            fail();
        }
    }

    @Test
    public void invalidAuthToken() {
        try {
            testLoginService.completeJob("Timmy","Timmy'sCode");
            fail();
        }catch(DataAccessException error){
            assertTrue(true);
        }
    }
}