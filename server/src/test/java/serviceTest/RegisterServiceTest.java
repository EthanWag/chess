package serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.*;
import models.resModels.ResponseRegisterPackage;
import services.*;


class RegisterServiceTest {


    private RegisterService testRegister;
    private final ClearApplicationService clear = new ClearApplicationService();

    public RegisterServiceTest() {
        try {
            testRegister = new RegisterService();
        }catch(DataAccessException serError){
            fail("unable to setup!");
        }
    }

    @BeforeEach
    public void reset(){
        try {
            clear.completeJob();
        }catch(Exception error){
            System.err.println("Error in completing test");
        }
    }

    @Test
    public void completeJobSuccess() {

        String testUsername = "brad";
        String testPassword = "123456";
        String testEmail = "email.com";

        try {
            ResponseRegisterPackage testPackage = testRegister.completeJob(testUsername, testPassword, testEmail);

            String testAuth = testPackage.authToken();
            ResponseRegisterPackage expectedPackage = new ResponseRegisterPackage(testUsername,testAuth);

            assertEquals(expectedPackage,testPackage);
            System.out.println("One Register passed!");

        }catch(DataAccessException error){
            fail("Failure: unable to successfully register");
        }
    }

    @Test
    public void completeJobMany() {

        // sample users and expected results

        String user1Name = "brad";
        String user1Pass = "123456";
        String user1Email = "email.com";

        String user2Name = "Timmy";
        String user2Pass = "666";
        String user2Email = "420BlazeIt69.edging";

        String user3Name = "Nathan";
        String user3Pass = "171685";
        String user3Email = "jackattack_bye.com";

        // runs the actual test

        try {
            var resUser1 = testRegister.completeJob(user1Name,user1Pass,user1Email);
            var resUser2 = testRegister.completeJob(user2Name,user2Pass,user2Email);
            var resUser3 = testRegister.completeJob(user3Name,user3Pass,user3Email);

            String auth1 = resUser1.authToken();
            ResponseRegisterPackage expected1 = new ResponseRegisterPackage(user1Name,auth1);

            String auth2 = resUser2.authToken();
            ResponseRegisterPackage expected2 = new ResponseRegisterPackage(user2Name,auth2);

            String auth3 = resUser3.authToken();
            ResponseRegisterPackage expected3 = new ResponseRegisterPackage(user3Name,auth3);

            assertEquals(expected1,resUser1);
            assertEquals(expected2,resUser2);
            assertEquals(expected3,resUser3);

            System.out.println("Many Registers passed!");

        }catch(DataAccessException error){
            fail("Failure: not being able to handle multiple results");
        }
    }


    @Test
    public void completeJobInvalidEntry() {

        String testUsername = null;
        String testPassword = "password";
        String testEmail = "yahoo.com";

        try {

            testRegister.completeJob(testUsername, testPassword, testEmail);
            fail("Failure: not all info present, should have failed");

        }catch(DataAccessException error){
            assertEquals(400,error.getErrorCode());
            System.out.println("Incomplete Register info passed!");
        }
    }
}