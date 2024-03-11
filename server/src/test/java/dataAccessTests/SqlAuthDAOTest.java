package dataAccessTests;

import dataAccess.SqlAuthDAO;
import dataAccess.SqlUserDAO;
import models.AuthData;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthDAOTest {

    @BeforeEach
    void setUp() {

        try {

            SqlUserDAO sqlSetup = new SqlUserDAO();
            sqlSetup.deleteAll();
            sqlSetup.commit();

            SqlAuthDAO authSetup = new SqlAuthDAO();
            authSetup.deleteAll();
            authSetup.commit();

        }catch(Exception error){
            fail("Error: Cannot set up");
        }

        // creates games for tests to use to use. Note should be index 1
        User newUser1 = new User("Ethan", "123password", "123email.com");
        User newUser2 = new User("Baker49", "minecraft", "ruben.org");

        try {

            SqlUserDAO sqlSetup = new SqlUserDAO();
            sqlSetup.create(newUser1);
            sqlSetup.create(newUser2);
            sqlSetup.commit();

            AuthData newData = new AuthData("testing-auth-token","Ethan");

            SqlAuthDAO authTest = new SqlAuthDAO();
            authTest.create(newData);
            authTest.commit();

        }catch(Exception error){
            fail("Error: failure to set up");
        }

    }

    @Test
    void createSuccessful() {

        AuthData newAuth = new AuthData("Now-you-have-one", "Baker49");

        try{

            SqlAuthDAO authTest = new SqlAuthDAO();
            authTest.create(newAuth);
            authTest.commit();

            System.out.println("creation successful");
            assertTrue(true);

        }catch(Exception error){
            fail("Error: unable to create Auth");
        }

    }

    @Test
    void readNormal() {

        try{

            SqlAuthDAO authTest = new SqlAuthDAO();
            AuthData newData = authTest.read("testing-auth-token");
            authTest.commit();

            assertEquals("Ethan", newData.username());

        }catch(Exception error){
            fail("Error: AuthToken was included");
        }

    }

    @Test
    void normalUserNoAuth() {

        try{
            SqlAuthDAO authTest = new SqlAuthDAO();
            AuthData newData = authTest.read("Baker49");
            authTest.commit();

            fail("Error: Baker49 does not have a token and isn't a token");

        }catch(Exception error){
            System.out.println("Auth does not exsist!");
            assertTrue(true);
        }
    }

    @Test
    void deleteSuccessful() {

        try{
            SqlAuthDAO authTest = new SqlAuthDAO();
            authTest.delete("testing-auth-token");
            authTest.commit();

            System.out.println("successful delete");
            assertTrue(true);

        }catch(Exception error){
            fail("Error: should have deleted");
        }
    }

    @Test
    void delNoExsist() {

        try{
            SqlAuthDAO authTest = new SqlAuthDAO();
            authTest.delete("made-up-token");
            authTest.commit();

            fail("Error: should have failed, no auth");

        }catch(Exception error){

            System.out.println("Auth does not exist");
            assertTrue(true);
        }

    }

    @Test
    void deleteAll() {

        try {

            SqlAuthDAO authTest = new SqlAuthDAO();
            authTest.deleteAll();
            authTest.commit();

            assertTrue(true);
        }catch(Exception error){
            fail("Error: unable to delete all");
        }

    }
}