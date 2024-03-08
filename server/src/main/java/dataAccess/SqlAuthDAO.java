package dataAccess;

import models.AuthData;

import java.sql.*;

public class SqlAuthDAO implements AuthDAO{

    private Connection myConnection;

    public SqlAuthDAO() throws DataAccessException{
        try{
            myConnection = DatabaseConnection.connectToDb();

            // System.out.println("open");

            myConnection.setAutoCommit(false);
        }catch(Exception connError){ // can be SQL or dataAccess exceptions
            connectionDestroyedError();
        }
    }
    public void create(AuthData newAuthData) throws DataAccessException{
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO AuthDAO(authToken,username)\n");
        strBuilder.append("VALUES(?,?);");

        String sqlCreate = strBuilder.toString();

        // then execute the command
        try {
            var statement = myConnection.prepareStatement(sqlCreate);

            // fills in parameters and executes the code
            statement.setString(1,newAuthData.getAuthToken());
            statement.setString(2,newAuthData.getUsername());

            statement.executeUpdate();

        }catch(Exception error){
            connectionDestroyedError();
        }
    }
    public AuthData read(String authToken) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM AuthDAO WHERE authToken= ?");
        String sqlRead = strBuilder.toString();

        try{
            // preps statement
            var statement = myConnection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            statement.setString(1,authToken);
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the object if it found it

                String foundAuthToken = resultItems.getString("authToken");
                String foundUsername = resultItems.getString("username");

                // returns the new user if it found it
                return new AuthData(foundAuthToken,foundUsername);

            }else{
                // tells the user it could not find it, if it comes to that
                closeConnection();
                throw new DataAccessException("[401](Auth Not Found)(AuthDAO) Not Found");
            }

        }catch(SQLException error){
            connectionDestroyedError();
            return null;
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

            // If this code executes, than that means it was successful in deleteing my authtoken
            var result = statement.executeUpdate();

            if(result == 0){
                throw new DataAccessException("[401] Unauthorized delete");
            }


        }catch(Exception error){
            connectionDestroyedError();
        }

    }
    public void deleteAll(){
        try {
            var statement = myConnection.prepareStatement("TRUNCATE TABLE AuthDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }

    public void commit()throws DataAccessException{
        try {
            myConnection.commit();
            closeConnection();

        }catch(SQLException error){
            connectionDestroyedError();
        }
    }

    public void closeConnection() throws DataAccessException{
        try{
            if(!myConnection.isClosed()){
                // System.out.println("closed");
                myConnection.close();
            }
        }catch(SQLException error){
            throw new DataAccessException("[503] unable to close");
        }
    }

    private void connectionDestroyedError() throws DataAccessException{
        closeConnection();
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }

}
