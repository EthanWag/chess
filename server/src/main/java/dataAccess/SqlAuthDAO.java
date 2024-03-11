package dataAccess;

import models.AuthData;

import java.sql.*;

public class SqlAuthDAO implements AuthDAO{

    private Connection myConnection;

    public SqlAuthDAO() throws DataAccessException{
        try{
            myConnection = DatabaseConnection.connectToDb();
            myConnection.setAutoCommit(false);

        }catch(Exception dbConnection){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void create(AuthData newAuthData) throws DataAccessException{

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO AuthDAO(authToken,username)\n");
        strBuilder.append("VALUES(?,?);");

        String sqlCreate = strBuilder.toString();

        try {
            var statement = myConnection.prepareStatement(sqlCreate);

            // fills in parameters and executes the code
            statement.setString(1,newAuthData.getAuthToken());
            statement.setString(2,newAuthData.getUsername());

            statement.executeUpdate();

        }catch(SQLException sqlErr){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public AuthData read(String authToken) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM AuthDAO WHERE authToken= ?");
        String sqlRead = strBuilder.toString();

        try{
            var statement = myConnection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            statement.setString(1,authToken);
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the new user if it found it
                String foundAuthToken = resultItems.getString("authToken");
                String foundUsername = resultItems.getString("username");

                return new AuthData(foundAuthToken,foundUsername);

            }else{
                // tells the user it could not find it, if it comes to that
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR: User not found",401);
            }

        }catch(SQLException sqlErr){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void delete(String authToken) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("DELETE FROM AuthDAO WHERE authToken= ?");
        String sqlRemove = strBuilder.toString();

        try{
            var statement = myConnection.prepareStatement(sqlRemove);
            statement.setString(1,authToken);

            var result = statement.executeUpdate();

            if(result == 0){
                DatabaseConnection.closeConnection(myConnection); // maybe fix here
                throw new DataAccessException("ERROR: Invalid delete",401);
            }

        }catch(SQLException sqlErr){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void deleteAll(){ // come back here and fix this statement
        try {
            var statement = myConnection.prepareStatement("TRUNCATE TABLE AuthDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }

    public void commit()throws DataAccessException{
        DatabaseConnection.commit(myConnection);
    }

    public void close() throws DataAccessException{
        DatabaseConnection.closeConnection(myConnection);
    }

}
