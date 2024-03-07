package dataAccess;

import models.User;

import java.sql.*;

public class SqlUserDAO implements UserDAO{

    private Connection myConnection;

    public SqlUserDAO() throws DataAccessException{
        try{
            myConnection = DatabaseConnection.connectToDb();

            // System.out.println("open");

            myConnection.setAutoCommit(false);
        }catch(Exception connError){ // can be SQL or dataAccess exceptions
            connectionDestroyedError();
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

        }catch(SQLException sqlException){
            // grabs the error code of the sqlException
            int statusCode = sqlException.getErrorCode();

            if(statusCode == 1062){ // 1062 is the error code for already taken, handles accordingly
                closeConnection();
                throw new DataAccessException("[403](Used User)(UserDAO) User already taken");
            }else{ // this is if the connection broke
                connectionDestroyedError();
            }
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

            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the object if it found it

                String foundUsername = resultItems.getString("username");
                String foundPassword = resultItems.getString("password");
                String foundEmail = resultItems.getString("email");

                return new User(foundUsername,foundPassword,foundEmail);

            }else{
                // tells the user it could not find it, if it comes to that
                closeConnection();
                throw new DataAccessException("[401](Game Not Found)(GameDAO) Not Found");
            }

        }catch(SQLException error){
            connectionDestroyedError();
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
                myConnection.close();
                // System.out.println("closed");
            }
        }catch(SQLException error){
            throw new DataAccessException("[503] unable to close");
        }
    }

    // private helper functions that are used with the database
    private void connectionDestroyedError() throws DataAccessException{
        closeConnection();
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }

}
