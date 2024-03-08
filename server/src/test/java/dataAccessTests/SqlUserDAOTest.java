package dataAccessTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.SqlGameDAO;
import dataAccess.SqlUserDAO;
import models.Game;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlUserDAOTest {

    @BeforeEach
    void setUp() {

        try {

            SqlUserDAO sqlSetup = new SqlUserDAO();
            sqlSetup.deleteAll();
            sqlSetup.commit();

        }catch(Exception error){
            fail("Error: Cannot set up");
        }

        // creates games for tests to use to use. Note should be index 1
        User newUser = new User("Ethan", "123password", "123email.com");

        try {

            SqlUserDAO sqlSetup = new SqlUserDAO();
            sqlSetup.create(newUser);
            sqlSetup.commit();

        }catch(Exception error){
            fail("Error: failure to set up");
        }


    }

    @Test
    void createSuccessfully() {

        try{

            User newUser = new User("Nathan","lime", "pumpkin.buzzfeed.com");

            SqlUserDAO testUser = new SqlUserDAO();
            testUser.create(newUser);
            testUser.commit();

            // successfully logged in user
            assertTrue(true);

        }catch(Exception error){
            fail("Error: failure to create user");
        }

    }

    // this tests if you try to make two identical users
    @Test
    void twoUserNames() {

        try{
            User newUser = new User("Ethan","funhouse", "doesNotExsist.com");

            SqlUserDAO testUser = new SqlUserDAO();
            testUser.create(newUser);
            testUser.commit();

            // successfully logged in user
            fail("Error: this user already exists");

        }catch(Exception error){
            System.out.println("Duplicate detected!");
            assertTrue(true);
        }
    }

    @Test
    void normalRead() {

        try {
            SqlUserDAO testRead = new SqlUserDAO();
            User myUser = testRead.read("Ethan");
            testRead.commit();

            assertTrue(myUser.getEmail().equals("123email.com"));

        }catch(Exception error){
            fail("Error: userExists!");
        }

    }

    // test to see if you read a user that does not exsist
    @Test
    void badRead() {

        try {
            SqlUserDAO testRead = new SqlUserDAO();
            User myUser = testRead.read("Ethann");
            testRead.commit();

            fail("Error: user does not exist");

        }catch(Exception error){
            System.out.println("invalid user detected!");
            assertTrue(true);
        }

    }

    @Test
    void deleteAll() {

        try {
            SqlUserDAO testDel = new SqlUserDAO();
            testDel.deleteAll();
            testDel.commit();

        }catch(Exception error){
            fail("Error: cannot clear");
        }

    }
}