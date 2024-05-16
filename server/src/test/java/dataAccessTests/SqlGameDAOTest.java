package dataAccessTests;

import chess.*;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.*;

import java.util.ArrayList;
import java.util.Collection;

class SqlGameDAOTest {

    private static final int GAME_INDEX = 1;

    @BeforeEach
    void setUp() {
        try{
            // sets up database's for tests to use
            SqlGameDAO sqlSetup = new SqlGameDAO();
            sqlSetup.deleteAll();

            // creates games for tests to use to use. Note should be index 1
            Game newGame1 = new Game(-1, "","","fun game",false,false);

            ChessGame myGame = newGame1.getGame();

            // edits the board to make things interesting

            ChessPosition startPos = new ChessPosition(2,1);
            ChessPosition endPos = new ChessPosition(3,1);

            ChessMove newMove = new ChessMove(startPos,endPos,null);

            try {
                myGame.makeMove(newMove);
            }catch(Exception error){
                System.err.println(error.getMessage());
                return;
            }

            // adds game to the database
            SqlGameDAO addTestGame = new SqlGameDAO();
            addTestGame.create(newGame1);


        }catch(Exception error){
            System.err.println(error.getMessage());
            System.err.println("ERROR: Cannot set up");
            fail();
        }
    }

    @Test
    void createSuccessful() {

        Game testGame = new Game(-1, "Useful","nancy","fun game",true,true);

        try{

            SqlGameDAO sqlSetup = new SqlGameDAO();
            sqlSetup.create(testGame);

            assertTrue(true);

        }catch(Exception error){
            fail("Error: should have added game");
        }

    }

    @Test
    void nullValuesCreate() {

        Game testGame = new Game(-1, "user1","user2",null,true,true);

        try{

            SqlGameDAO sqlSetup = new SqlGameDAO();
            sqlSetup.create(testGame);

            fail("Error: NULL value, should have rejected");

        }catch(Exception error){
            System.out.println("Successful rejection!");
            assertTrue(true);
        }

    }

    // checks to see if it spesfically got the correct board
    @Test
    void readSuccessfulFind() {

        Game readGame = null;

        try {
            SqlGameDAO readTest = new SqlGameDAO();
            readGame = readTest.read(GAME_INDEX);

        }catch(Exception error){
            System.out.println(error.getMessage());
            fail("Error: should have found game");
        }

        if(readGame == null){
            fail("ERROR: cannot find game");
        }

        ChessBoard myBoard = readGame.getGame().getBoard();
        ChessPosition pos = new ChessPosition(3,1);
        ChessPiece myPiece = myBoard.getPiece(pos);

        if(myPiece != ChessBoard.EMPTY){ // checks to see if it is the correct type
            assert(myPiece.getPieceType() == ChessPiece.PieceType.PAWN);
        }else {
            fail("ERROR: inncorrect board");
        }
    }

    @Test
    void invalidId() {

        try {
            int badIndex = 2;

            SqlGameDAO readTest = new SqlGameDAO();
            Game failGame = readTest.read(badIndex);

            fail("Error: There is no game at index 2");

        }catch(DataAccessException error){

            assertEquals(400,error.getErrorCode());
        }

    }

    @Test
    void testGetAll() {

        // adds a few more games to it, finds output
        Game testGame1 = new Game(-1, "","Ethan","Ethan's game",false,true);
        Game testGame2 = new Game(-1, "Nate the great","ted","chess tourney",true,true);

        ChessGame myGame = testGame2.getGame();

        // edits the board to make things interesting

        ChessPosition startPos = new ChessPosition(5,1);
        ChessPosition endPos = new ChessPosition(6,1);

        ChessMove newMove = new ChessMove(startPos,endPos,null);

        try {
            myGame.makeMove(newMove);
        }catch(Exception error){
            System.err.println(error.getMessage());
            return;
        }

        try { // makes some games and submits them
            SqlGameDAO sqlSetup = new SqlGameDAO();
            sqlSetup.create(testGame1);
            sqlSetup.create(testGame2);

        }catch(Exception error){
            System.err.println(error.getMessage());
            System.err.println("Error: should have returned 200 for creation");
            fail();
        }


        // tests if there are three games exactly that are listed
        Collection<Game> result = new ArrayList<>();

        try {
            SqlGameDAO test = new SqlGameDAO();
            result = test.getAll();

        }catch(Exception error){
            System.err.println(error.getMessage());
            System.err.println("Error: invalid, should have received");
            fail();
        }

        // test if there are three games
        assert(result.size() == 3);
    }

    // checks to see if they you can clear a game
    @Test
    void clearAll(){

        try {

            SqlGameDAO sqlDel = new SqlGameDAO();
            sqlDel.deleteAll();

            SqlGameDAO sqlSetup = new SqlGameDAO();
            Collection<Game> testGames = sqlSetup.getAll();

            assertEquals(0, testGames.size());

        }catch(Exception error){
            fail("should have cleared");
        }
    }
}