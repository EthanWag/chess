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
        try{
            myConnection = DatabaseConnection.connectToDb();
            myConnection.setAutoCommit(false);
        }catch(Exception connError){ // can be SQL or dataAccess exceptions
            connectionDestroyedError();
        }
    }

    public int create(Game newGame) throws DataAccessException{

        // converts game object to a string
        String gameStr = serializer.objToJson(newGame.getGame());

        // build the command
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO GameDAO(whiteUsername,blackUsername,gameName,game,whiteTaken,blackTaken)\n");
        strBuilder.append("VALUES(?,?,?,?,?,?);");
        String sqlCreate = strBuilder.toString();

        // then execute the command
        try {

            var statement = myConnection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);

            // add on the parameters here
            statement.setString(1,newGame.getWhiteUsername());
            statement.setString(2,newGame.getBlackUsername());
            statement.setString(3, newGame.getGameName());
            statement.setString(4,gameStr);
            statement.setBoolean(5,false);
            statement.setBoolean(6,false);


            statement.executeUpdate();
            var key = statement.getGeneratedKeys();

            if(key.first()) {
                int gameId = key.getInt(1);
                newGame.setGameID(gameId);
                return gameId;
            }else{
                throw new DataAccessException("[501](auto-generate failed)your game failed generate an id");
            }

        }catch(Exception error){ // because of auto increment, we will never get an invalid entry. Always [500]
            connectionDestroyedError();
            return 0;
        }

    }
    public Game read(int gameId) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM GameDAO WHERE gameId=?");
        String sqlRead = strBuilder.toString();

        try{

            // grabs the connection and then tries the execute the program
            var statement = myConnection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                    ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1,gameId);
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){
                // returns the object if it found it
                return convertGame(resultItems);

            }else{
                // tells the user it could not find it, if it comes to that
                throw new DataAccessException("[400](Game Not Found)(GameDAO) Not Found");
            }

        }catch(Exception error){ // this tackles more the case that the connection was bad
            connectionDestroyedError();
            return null;
        }
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

    public void updatePlayer(int gameId,String username, String color) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE GameDAO ");

        if(color.equals("WHITE")){
            strBuilder.append("SET whiteUsername = ?, whiteTaken = true ");
        }else{
            strBuilder.append("SET blackUsername = ?, blackTaken = true ");
        }

        strBuilder.append("WHERE gameId = ?");
        String sqlUpdate = strBuilder.toString();

        try{
            // grabs the connection and then tries the execute the program
            var statement = myConnection.prepareStatement(sqlUpdate);
            statement.setString(1,username);
            statement.setInt(2,gameId);
            statement.executeUpdate();


        }catch(SQLException error){ // this tackles more the case that the connection was bad
            connectionDestroyedError();
        }

    }

    public void commit()throws DataAccessException{
        try {
            myConnection.commit();
        }catch(SQLException error){
            connectionDestroyedError();
        }
    }

    // private functions that help with the collection of objects
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
    private void connectionDestroyedError() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }

}
