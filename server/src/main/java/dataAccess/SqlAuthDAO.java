package dataAccess;

import models.AuthData;
import java.sql.*;


public class SqlAuthDAO implements AuthDAO{

    public SqlAuthDAO(){
    }
    public void create(AuthData newAuthData) throws DataAccessException{

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO AuthDAO(authToken,username)\n");
        strBuilder.append("VALUES(?,?);");
        String sqlCreate = strBuilder.toString();

        Connection connection = open();

        try {

            var statement = connection.prepareStatement(sqlCreate);

            // fills in parameters and executes the code
            statement.setString(1,newAuthData.authToken());
            statement.setString(2,newAuthData.username());

            statement.executeUpdate();
            close(connection);

        }catch(SQLException sqlErr){
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public AuthData read(String authToken) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM AuthDAO WHERE authToken= ?");
        String sqlRead = strBuilder.toString();

        Connection connection = open();

        try{
            var statement = connection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            statement.setString(1,authToken);
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the new user if it found it
                String foundAuthToken = resultItems.getString("authToken");
                String foundUsername = resultItems.getString("username");

                close(connection);

                return new AuthData(foundAuthToken,foundUsername);

            }else{
                errClose(connection);
                throw new DataAccessException("ERROR: User not found",401);
            }

        }catch(SQLException sqlErr){
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void delete(String authToken) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("DELETE FROM AuthDAO WHERE authToken= ?");
        String sqlRemove = strBuilder.toString();

        Connection connection = open();

        try{

            var statement = connection.prepareStatement(sqlRemove);
            statement.setString(1,authToken);
            var result = statement.executeUpdate();

            if(result == 0){
                errClose(connection);
                throw new DataAccessException("ERROR: Invalid delete",401);
            }

            close(connection);

        }catch(SQLException sqlErr){
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public void deleteAll(){ // come back here and fix this statement
        try {
            Connection connection = open();

            var statement = connection.prepareStatement("TRUNCATE TABLE AuthDAO");
            statement.execute();

            close(connection);

        } catch (SQLException | DataAccessException error){
            System.err.println("error");
        }

    }

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
}
