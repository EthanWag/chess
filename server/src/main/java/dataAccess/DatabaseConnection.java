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

        userDAOBuilder.append("CREATE TABLE IF NOT EXISTS UserDAO");
        userDAOBuilder.append("(");
        userDAOBuilder.append("username VARCHAR(50) PRIMARY KEY UNIQUE,");
        userDAOBuilder.append("password VARCHAR(80) NOT NULL,");
        userDAOBuilder.append("email VARCHAR(40) NOT NULL");
        userDAOBuilder.append(");");

        userDAO = userDAOBuilder.toString();

        authDAOBuilder.append("CREATE TABLE IF NOT EXISTS AuthDAO");
        authDAOBuilder.append("(");
        authDAOBuilder.append("authId INT PRIMARY KEY AUTO_INCREMENT,");
        authDAOBuilder.append("authToken VARCHAR(40) NOT NULL UNIQUE,");
        authDAOBuilder.append("username VARCHAR(50) NOT NULL");
        authDAOBuilder.append(");");

        authDAO = authDAOBuilder.toString();

        gameDAOBuilder.append("CREATE TABLE IF NOT EXISTS GameDAO");
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
    public static Connection connectToDb() throws DataAccessException {

        // creates a database, tables and returns a connection if it set up properly
        // otherwise it throws an error
        DatabaseManager.createDatabase();
        Connection connection = DatabaseManager.getConnection();
        generateTables(connection);

        return connection;

    }

    // helper functions for these classes
    private static void generateTables(Connection connection) throws DataAccessException{
        // if you pass in a connection, it will generate the tables for you if you don't have them

        try {
            var userStatement = connection.prepareStatement(userDAO);
            userStatement.execute();
            var authStatement = connection.prepareStatement(authDAO);
            authStatement.execute();
            var gameStatement = connection.prepareStatement(gameDAO);
            gameStatement.execute();
        }catch(SQLException genError){
            throw new DataAccessException("ERROR: Failure to generate tables",500);
        }

    }

    public static void commit(Connection connection)throws DataAccessException{
        try {
            connection.commit();
            closeConnection(connection);

        }catch(SQLException error){
            throw new DataAccessException("ERROR: Unable to connect to database",500);
        }
    }

    public static void closeConnection(Connection connection) throws DataAccessException{
        try{
            if(!connection.isClosed()){
                connection.close();
            }
        }catch(SQLException error){
            throw new DataAccessException("ERROR: Unable to close", 501);
        }
    }

}
