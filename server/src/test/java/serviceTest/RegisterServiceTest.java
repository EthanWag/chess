package serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.*;
import models.resModels.ResponseRegisterPackage;
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

        try {
            clearService.completeJob();
        }catch(Exception error){
            System.err.println("error in building tests");
        }

    }

    @Test
    public void completeJobSuccess() {

        String testUsername = "brad";
        String testPassword = "123456";
        String testEmail = "email.com";

        try {

            ResponseRegisterPackage testPackage = testRegister.completeJob(testUsername, testPassword, testEmail);

            String testAuth = testPackage.newAuthToken();
            ResponseRegisterPackage expectedPackage = new ResponseRegisterPackage(testUsername,testAuth);


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

            ResponseRegisterPackage testPackage = testRegister.completeJob(testUsername, testPassword, testEmail);

            String testAuth = testPackage.newAuthToken();
            ResponseRegisterPackage expectedPackage = new ResponseRegisterPackage(testUsername,testAuth);

            // should not be able to do that, should throw an error
            fail();

        }catch(DataAccessException error){

            assertEquals(400,error.getErrorCode());

        }
    }
}