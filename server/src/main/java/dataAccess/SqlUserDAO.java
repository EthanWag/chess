package dataAccess;

import models.User;
import java.sql.*;

public class SqlUserDAO implements UserDAO{

    private Connection myConnection;

    public SqlUserDAO() throws DataAccessException{
        try{

            myConnection = DatabaseConnection.connectToDb();
            myConnection.setAutoCommit(false);

        }catch(Exception dbConnection){ // unable to connect for some reason

            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
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
            statement.setString(1,newUser.username());
            statement.setString(2,newUser.password());
            statement.setString(3,newUser.email());

            statement.executeUpdate();

        }catch(SQLException sqlErr){
            // grabs the error code of the sqlException
            int statusCode = sqlErr.getErrorCode();

            if(statusCode == 1062){ // 1062 is the error code for already taken, handles accordingly
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR: Username is Taken",403);
            }else{ // this is if the connection broke
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR: Database connection lost",500);
            }
        }
    }
    public User read(String username) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM UserDAO WHERE username= ?");
        String sqlRead = strBuilder.toString();

        try{

            var statement = myConnection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            statement.setString(1,username);

            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){

                String foundUsername = resultItems.getString("username");
                String foundPassword = resultItems.getString("password");
                String foundEmail = resultItems.getString("email");

                return new User(foundUsername,foundPassword,foundEmail);

            }else{
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR: Game Not Found",401);
            }

        }catch(SQLException sqlErr){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void deleteAll(){ // come here and add some code
        try {
            var statement = myConnection.prepareStatement("TRUNCATE TABLE UserDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println(error.getMessage()); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }

    public void close() throws DataAccessException {
        DatabaseConnection.closeConnection(myConnection);
    }
    public void commit() throws DataAccessException {
        DatabaseConnection.commit(myConnection);
    }

}
