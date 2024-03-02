package dataAccess;

import java.sql.*;
public abstract class DatabaseConnection {

    // simply just preps the database so other functions can use it
    protected static Connection connectToDb() throws Exception {

        DatabaseManager.createDatabase();

        Connection dbConn = DatabaseManager.getConnection();

        return dbConn;

    }


    // this is just for testing purposes

    public static void main(String [] args){


        try(Connection connDb = connectToDb()){

            System.out.println("it worked!");

        }catch(Exception e){

            System.out.println(e.getMessage());

        }
    }
}
