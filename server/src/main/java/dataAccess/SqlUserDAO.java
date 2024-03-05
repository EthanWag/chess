package dataAccess;

import models.User;

import java.sql.*;

public class SqlUserDAO implements UserDAO{

    private Connection myConnection;

    public SqlUserDAO() throws DataAccessException{
        try{ // opens a connection if it is successful
            myConnection = DatabaseConnection.connectToDb();
        }catch(Exception error){
            throwConnectError(); // throws errors otherwise
        }
    }

    public void create(User newUser) throws DataAccessException{

        // build the command
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO UserDAO(username,password,email)\n");
        strBuilder.append("VALUES(?,?,?);");

        String sqlCreate = strBuilder.toString();

        // then execute the command
        try {
            var statement = myConnection.prepareStatement(sqlCreate);

            // fills in parameters and executes the code
            statement.setString(1,newUser.getUsername());
            statement.setString(2,newUser.getPassword());
            statement.setString(3,newUser.getEmail());

            statement.executeUpdate();

        }catch(Exception error){
            throwConnectError(); // throws an error it couldn't connect to the database
            // error, what happens if it couldn't connect vs already used username
        }

    }
    public User read(String username) throws DataAccessException{


        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM UserDAO WHERE username= ?");
        String sqlRead = strBuilder.toString();

        try{

            // var statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            var statement = myConnection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            statement.setString(1,username);

            // double check syntax here
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the object if it found it

                String foundUsername = resultItems.getString("username");
                String foundPassword = resultItems.getString("password");
                String foundEmail = resultItems.getString("email");

                // returns the new user if it found it
                return new User(foundUsername,foundPassword,foundEmail);

            }else{
                // tells the user it could not find it, if it comes to that
                throw new DataAccessException("[400](Game Not Found)(GameDAO) Not Found");
            }

        }catch(Exception error){
            throwConnectError();
        }
        return null;

    }
    public void deleteAll(){
        try {
            var statement = myConnection.prepareStatement("DROP TABLE IF EXISTS AuthDAO, UserDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }

    // private helper functions that are used with the database
    private void throwConnectError() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }





    // main just for testing

    public static void main(String [] args){

        try {

            SqlUserDAO myData = new SqlUserDAO();

            User newUser = new User("Ethan","ballz", "ethanwag@outlook.com");

            myData.create(newUser);

            //User readUser = myData.read("Ethan");

            // myData.deleteAll();
            System.out.println("Success");

        }catch(Exception error){
            System.out.println(error.getMessage());
            System.out.println("Error");

        }
    }

}
