package dataAccess;

import models.AuthData;

import java.sql.*;

public class SqlAuthDAO implements AuthDAO{

    private Connection myConnection;

    public SqlAuthDAO() throws DataAccessException{
        try{
            myConnection = DatabaseConnection.connectToDb();
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
                throw new DataAccessException("[401](Auth Not Found)(AuthDAO) Not Found");
            }

        }catch(Exception error){
            connectionDestroyedError();
        }
        return null;

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
            statement.executeUpdate();


        }catch(Exception error){ //FIXME: may cause problems, come back here
            connectionDestroyedError();
        }

    }
    public void deleteAll(){
        try {
            var statement = myConnection.prepareStatement("DROP TABLE IF EXISTS AuthDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }
    public void commit()throws SQLException{
        myConnection.commit();
    }
    private void connectionDestroyedError() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }





    public static void main(String [] args){
        try{

            SqlAuthDAO myData = new SqlAuthDAO();

            // AuthData newAuth = new AuthData("sdajlflksjflksjf;lskdj","Ethan");

            // myData.create(newAuth);

            myData.delete("sdajlflksjflksjf;lskdj");

            System.out.println("success!");

        }catch(Exception error){
            System.out.println(error.getMessage());
            System.out.println("error");
        }
    }

}
