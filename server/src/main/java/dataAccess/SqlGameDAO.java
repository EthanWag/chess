package dataAccess;

import models.Game;
import services.handlers.ConvertGson;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{

    private Connection myConnection;
    private ConvertGson serializer = new ConvertGson();

    public SqlGameDAO() throws DataAccessException{

        try{
            myConnection = DatabaseConnection.connectToDb();
        }catch(Exception error){
            throwDataAccess();
        }
    }

    public void create(Game newGame) throws DataAccessException{

        // converts game object to a string
        String gameStr = serializer.objToJson(newGame.getGame());

        // build the command
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO GameDAO(whiteUsername,blackUsername,gameName,game,whiteTaken,blackTaken)\n");
        strBuilder.append(String.format("VALUES('%s','%s','%s','%s',%d,%d);",newGame.getWhiteUsername(),newGame.getBlackUsername(),
                                                                    newGame.getGame(),gameStr,0,0)); // should be correct
        String sqlCreate = strBuilder.toString();

        // then execute the command
        try {

            var statement = myConnection.prepareStatement(sqlCreate);
            statement.executeUpdate();

        }catch(Exception error){
            throwDataAccess();
        }

    }
    public Game read(int gameId) throws DataAccessException{
        return null;
    }
    public Collection<Game> getAll(){
        return null;
    }

    public void deleteAll(){

        try {
            var statement = myConnection.prepareStatement("DROP TABLE GameDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }

    }

    private void throwDataAccess() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }









    public static void main(String [] args){

        try {

            Game newGame = new Game(12,"","","davids game");


            SqlGameDAO myData = new SqlGameDAO();
            myData.create(newGame);

        }catch(Exception error){
            System.out.println("error");
        }
    }


}
