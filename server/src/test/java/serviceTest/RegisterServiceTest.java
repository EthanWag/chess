package serviceTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.*;
import services.RegisterService.registerPackage;
import services.*;


class RegisterServiceTest {


    private RegisterService testRegister;
    private ClearApplicationService clearService;

    public RegisterServiceTest() {

        testRegister = new RegisterService();
        clearService = new ClearApplicationService();
    }

    @BeforeEach
    public void reset(){

        clearService.completeJob();

    }

    @Test
    public void completeJobSuccess() {

        String testUsername = "brad";
        String testPassword = "123456";
        String testEmail = "email.com";

        try {

            registerPackage testPackage = testRegister.completeJob(testUsername, testPassword, testEmail);

            String testAuth = testPackage.authToken;
            registerPackage expectedPackage = new registerPackage(testUsername,testAuth);


            assertEquals(expectedPackage,testPackage);

        }catch(DataAccessException error){

            fail();

        }
    }

    @Test
    public void completeJobInvalidEntry() {

        String testUsername = null;
        String testPassword = "password";
        String testEmail = "yahoo.com";

        try {

            registerPackage testPackage = testRegister.completeJob(testUsername, testPassword, testEmail);

            String testAuth = testPackage.authToken;
            registerPackage expectedPackage = new registerPackage(testUsername,testAuth);

            // should not be able to do that, should throw an error
            fail();

        }catch(DataAccessException error){

            String errorMessage = "[400](bad request)(Register Service) invalid input";
            assertEquals(errorMessage,error.getMessage());

        }
    }
}