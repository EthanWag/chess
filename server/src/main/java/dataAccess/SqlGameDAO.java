package dataAccess;

import models.Game;
import chess.ChessGame;
import services.handlers.ConvertGson;

import java.sql.*;
import java.util.Collection;
import java.util.ArrayList;

public class SqlGameDAO implements GameDAO{

    private Connection myConnection;
    private static final ConvertGson serializer = new ConvertGson();

    public SqlGameDAO() throws DataAccessException{
        // all it tries to do is connect to the database
        try{
            myConnection = DatabaseConnection.connectToDb();
        }catch(Exception error){
            throwConnectError();
        }
    }

    public int create(Game newGame) throws DataAccessException{

        // converts game object to a string
        String gameStr = serializer.objToJson(newGame.getGame());

        // build the command
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO GameDAO(whiteUsername,blackUsername,gameName,game,whiteTaken,blackTaken)\n");
        strBuilder.append(String.format("VALUES('%s','%s','%s','%s',%d,%d);",newGame.getWhiteUsername(),newGame.getBlackUsername(),
                                                                    newGame.getGameName(),gameStr,0,0));
        // NOTE: please note here, when a game is created, it assumes nobody has logged in yet


        String sqlCreate = strBuilder.toString();

        // then execute the command
        try {

            var statement = myConnection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            var key = statement.getGeneratedKeys();

            if(key.first()) {
                int gameId = key.getInt(1);
                newGame.setGameID(gameId);
                return gameId;
            }else{
                throw new DataAccessException("[501](auto-generate failed)your game failed generate an id");
            }

        }catch(Exception error){
            throwConnectError(); // throws an error it couldn't connect to the database
            return -1;
        }

    }
    public Game read(int gameId) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format("SELECT * FROM GameDAO WHERE gameId=%d",gameId));

        String sqlRead = strBuilder.toString();

        try{

            var statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            // double check syntax here
            ResultSet resultItems = statement.executeQuery(sqlRead);

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the object if it found it
                return convertGame(resultItems);

            }else{
                // tells the user it could not find it, if it comes to that
                throw new DataAccessException("[400](Game Not Found)(GameDAO) Not Found");
            }

        }catch(Exception error){
            throwConnectError();
        }
        return null;
    }
    public Collection<Game> getAll(){

        Collection<Game> allGames = new ArrayList<>();

        try {

            var statement = myConnection.prepareStatement("SELECT * FROM GameDAO");
            ResultSet resultItems = statement.executeQuery();

            while(resultItems.next()){
                Game listGame = convertGame(resultItems);
                allGames.add(listGame);
            }

            // returns all the new games that it found
            return allGames;

        }catch(Exception error){
            System.out.println("error");
            return allGames; // temp solution, come here to fix
        }
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

    // private functions that help with the collection of objects
    private void throwConnectError() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }
    private Game convertGame(ResultSet sqlSet) throws SQLException{

        int myGameId = sqlSet.getInt("gameId");
        String myWhiteUser = sqlSet.getString("whiteUsername");
        String myBlackUser = sqlSet.getString("blackUsername");
        String myGameName = sqlSet.getString("gameName");
        boolean myWhiteCheck = sqlSet.getBoolean("whiteTaken");
        boolean myBlackCheck = sqlSet.getBoolean("blackTaken");

        // finally gets the game from the database
        String game = sqlSet.getString("game");
        Object myGameObj = serializer.strToObj(game,ChessGame.class);

        // should be a game object, so it converts it
        ChessGame myGame = (ChessGame) myGameObj;

        // finally build the object and returns it
        Game foundGame = new Game(myGameId,myWhiteUser,myBlackUser,myGameName,myWhiteCheck,myBlackCheck);
        foundGame.setGame(myGame); // sets the gameboard to the new one

        return foundGame;

    }








    public static void main(String [] args){

        try {

            SqlGameDAO myData = new SqlGameDAO();

            Game newGame = new Game(-1,"Jack","","the best game",false,false);

            int index = myData.create(newGame);

            Game foundGame = myData.read(index);


            myData.deleteAll();
            System.out.println("Success");


        }catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

}
