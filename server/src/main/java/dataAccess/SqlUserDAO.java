package dataAccess;

import models.User;
import java.sql.*;

public class SqlUserDAO implements UserDAO{

    public SqlUserDAO() throws DataAccessException{

    }
    public void create(User newUser) throws DataAccessException{

        // build the command
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO UserDAO(username,password,email)\n");
        strBuilder.append("VALUES(?,?,?);");

        String sqlCreate = strBuilder.toString();

        Connection connection = open();

        try {
            var statement = connection.prepareStatement(sqlCreate);

            // fills in parameters and executes the code
            statement.setString(1, newUser.username());
            statement.setString(2, newUser.password());
            statement.setString(3, newUser.email());
            statement.executeUpdate();
            close(connection);

        }catch(SQLException sqlErr){

            errClose(connection);
            // grabs the error code of the sqlException
            int statusCode = sqlErr.getErrorCode();
            if(statusCode == 1062){ // 1062 is the error code for already taken, handles accordingly
                throw new DataAccessException("ERROR: Username is Taken",403);
            }else{ // this is if the connection broke
                throw new DataAccessException("ERROR: Database connection lost",500);
            }
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Unable to connect",500);
        }
    }

    public User read(String username) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM UserDAO WHERE username= ?");
        String sqlRead = strBuilder.toString();

        Connection connection = open();

        try{

            var statement = connection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            statement.setString(1,username);

            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){

                String foundUsername = resultItems.getString("username");
                String foundPassword = resultItems.getString("password");
                String foundEmail = resultItems.getString("email");

                close(connection);

                return new User(foundUsername,foundPassword,foundEmail);

            }else{
                errClose(connection);
                throw new DataAccessException("ERROR: Person not found",401);
            }

        }catch(SQLException sqlErr){
            errClose(connection); // just closes the connection when it is done
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void deleteAll(){ // come here and add some code
        try {
            Connection connection = open();
            var statement = connection.prepareStatement("TRUNCATE TABLE UserDAO");
            statement.execute();
            close(connection);

        } catch (Exception error){
            // FIXME: temp solution, make database connection pool to really solve some of these problems
            System.err.println(error.getMessage());
        }
    }

    // For now we will handle database connections manually but in the future, be sure to implement a database connection pool
    private void close(Connection connection) throws DataAccessException {
        DatabaseConnection.commit(connection);
        DatabaseConnection.closeConnection(connection);
    }

    private void errClose(Connection connection)throws DataAccessException{
        DatabaseConnection.closeConnection(connection);
    }
    private Connection open() throws DataAccessException{
        try{
            Connection connection = DatabaseConnection.connectToDb();
            connection.setAutoCommit(false);
            return connection;

        }catch(DataAccessException | SQLException error){
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    // come back to in the future to deal with concurrency
    /*
    private void commit(Connection connection) throws DataAccessException {
        DatabaseConnection.commit(connection);
    }
    */


}
