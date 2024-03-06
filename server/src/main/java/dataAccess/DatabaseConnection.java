package dataAccess;

import java.io.*;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Stream;
import static java.lang.System.exit;

public class DatabaseConnection {

    private static String UserDAO;
    private static String AuthDAO;
    private static String GameDAO;


    // sets up for the different string
    static {

        try (FileReader in = new FileReader("server/src/main/java/dataAccess/DAOstring")) {

            // So what this code does is break this down into a compact ArrayList we can use
            BufferedReader reader = new BufferedReader(in);
            Stream<String> file = reader.lines();
            Stream<String> filtFile = file.filter(n -> !Objects.equals(n, "\n"));
            List<String> inputList = filtFile.toList();
            ArrayList<String> input = new ArrayList<>(inputList);

            UserDAO = parseInput(input);
            AuthDAO = parseInput(input);
            GameDAO = parseInput(input);


        } catch (Exception error) {
            System.err.println("ERROR: DAOstring does not exist");
            System.err.println("Exiting");
            exit(0); // not permanent, but seriously though, need to have that file
        }

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
        var userStatement = connection.prepareStatement(UserDAO);
        userStatement.execute();
        var authStatement = connection.prepareStatement(AuthDAO);
        authStatement.execute();
        var gameStatement = connection.prepareStatement(GameDAO);
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
