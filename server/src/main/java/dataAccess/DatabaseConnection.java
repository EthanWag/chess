package dataAccess;

import java.sql.*;
import java.util.Iterator;
import java.util.ArrayList;

public class DatabaseConnection {

    private static String userDAO;
    private static String authDAO;
    private static String gameDAO;


    // sets up for the different string
    static {

        StringBuilder userDAOBuilder = new StringBuilder();
        StringBuilder authDAOBuilder = new StringBuilder();
        StringBuilder gameDAOBuilder = new StringBuilder();

        userDAOBuilder.append("CREATE TABLE IF NOT EXISTS chess.UserDAO");
        userDAOBuilder.append("(");
        userDAOBuilder.append("username VARCHAR(50) PRIMARY KEY UNIQUE,");
        userDAOBuilder.append("password VARCHAR(40) NOT NULL,");
        userDAOBuilder.append("email VARCHAR(40) NOT NULL");
        userDAOBuilder.append(");");

        userDAO = userDAOBuilder.toString();

        authDAOBuilder.append("CREATE TABLE IF NOT EXISTS chess.AuthDAO");
        authDAOBuilder.append("(");
        authDAOBuilder.append("authId INT PRIMARY KEY AUTO_INCREMENT,");
        authDAOBuilder.append("authToken VARCHAR(40) NOT NULL UNIQUE,");
        authDAOBuilder.append("username VARCHAR(50) NOT NULL,");
        authDAOBuilder.append("FOREIGN KEY (username) REFERENCES chess.UserDAO(username)");
        authDAOBuilder.append(");");

        authDAO = authDAOBuilder.toString();

        gameDAOBuilder.append("CREATE TABLE IF NOT EXISTS chess.GameDAO");
        gameDAOBuilder.append("(");
        gameDAOBuilder.append("gameId INT PRIMARY KEY AUTO_INCREMENT,");
        gameDAOBuilder.append("whiteUsername VARCHAR(50) NULL,");
        gameDAOBuilder.append("blackUsername VARCHAR(50) NULL,");
        gameDAOBuilder.append("gameName VARCHAR(40) NOT NULL,");
        gameDAOBuilder.append("game LONGTEXT,");
        gameDAOBuilder.append("whiteTaken BIT NOT NULL,");
        gameDAOBuilder.append("blackTaken BIT NOT NULL");
        gameDAOBuilder.append(");");

        gameDAO = gameDAOBuilder.toString();

    }

    // simply just preps the database so other functions can use it
    public static Connection connectToDb() throws DataAccessException,SQLException {

        // creates a database, tables and returns a connection if it set up properly
        // otherwise it throws an error
        DatabaseManager.createDatabase();
        Connection connection = DatabaseManager.getConnection();
        generateTables(connection);

        return connection;

    }

    // helper functions for these classes
    private static void generateTables(Connection connection) throws SQLException{
        // if you pass in a connection, it will generate the tables for you if you don't have them
        var userStatement = connection.prepareStatement(userDAO);
        userStatement.execute();
        var authStatement = connection.prepareStatement(authDAO);
        authStatement.execute();
        var gameStatement = connection.prepareStatement(gameDAO);
        gameStatement.execute();
    }

    // given a bunch of SQL input, returns one of the single command, used in initialization
    private static String parseInput(ArrayList<String> input){

        Iterator<String> iterator = input.iterator();
        StringBuilder builder = new StringBuilder();

        while(iterator.hasNext()){

            String line = iterator.next();
            iterator.remove();

            // adds stuff to the output as it runs
            builder.append(line);
            builder.append("\n");
            if(line.equals(");")){break;}

        }
        return builder.toString();
    }

    // this is just for testing purposes

    public static void main(String [] args){


        try(Connection connDb = connectToDb()){


            // just a simple test to see if I can send commands
            String statement = "create table test(test int null);";



        }catch(Exception e){

            System.out.println(e.getMessage());

        }
    }


}
