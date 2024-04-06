package dataAccess;

import models.Game;
import chess.ChessGame;
import services.handlers.GsonConverterReq;

import java.sql.*;
import java.util.Collection;
import java.util.ArrayList;

public class SqlGameDAO implements GameDAO{

    private Connection myConnection;
    private static final GsonConverterReq serializer = new GsonConverterReq();

    public SqlGameDAO() throws DataAccessException{
        try{
            myConnection = DatabaseConnection.connectToDb();
            myConnection.setAutoCommit(false);

        }catch(Exception dbConnection){ // catches errors in connections
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public int create(Game newGame) throws DataAccessException{

        // converts game object to a string
        String gameStr = serializer.objToJson(newGame.getGame());

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO GameDAO(whiteUsername,blackUsername,gameName,game,whiteTaken,blackTaken)\n");
        strBuilder.append("VALUES(?,?,?,?,?,?);");
        String sqlCreate = strBuilder.toString();

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
                return key.getInt(1);
                //newGame.setGameID(gameId);
                //return gameId;
            }else{
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR:Auto generation failed",500);
            }

        }catch(SQLException sqlErr){
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
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
                return convertGame(resultItems);
            }else{
                DatabaseConnection.closeConnection(myConnection);
                throw new DataAccessException("ERROR:Bad Request",400);
            }

        }catch(SQLException error){ // this tackles more the case that the connection was bad
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
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

            return allGames;

        }catch(Exception error){
            System.out.println("error");
            return allGames; // temp solution, come here to fix
        }
    }

    public void updatePlayer(int gameId,String username,boolean join, String color) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE GameDAO ");

        if(color.equals("WHITE")){
            strBuilder.append("SET whiteUsername = ?, whiteTaken = ? ");
        }else{
            strBuilder.append("SET blackUsername = ?, blackTaken = ? ");
        }

        strBuilder.append("WHERE gameId = ?");
        String sqlUpdate = strBuilder.toString();

        try{
            // grabs the connection and then tries the execute the program
            var statement = myConnection.prepareStatement(sqlUpdate);
            statement.setString(1,username);

            // new additions!!!!
            statement.setBoolean(2,join);

            statement.setInt(3,gameId);
            statement.executeUpdate();

        }catch(SQLException sqlErr){ // this tackles more the case that the connection was bad
            DatabaseConnection.closeConnection(myConnection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }

    }

    public void deleteAll(){
        try {
            var statement = myConnection.prepareStatement("TRUNCATE TABLE GameDAO");
            statement.execute();

        } catch (Exception error){
            System.err.println("error"); // what actually might be worth doing is making it so it calls the exception
            // handler directly
        }
    }

    public void commit()throws DataAccessException{
        DatabaseConnection.commit(myConnection);
    }

    // just simply closes the connection
    public void close() throws DataAccessException{
        DatabaseConnection.commit(myConnection);
    }

    // converts a result set item into a Game
    private Game convertGame(ResultSet sqlSet) throws SQLException{

        int myGameId = sqlSet.getInt("gameId");
        String myWhiteUser = sqlSet.getString("whiteUsername");
        String myBlackUser = sqlSet.getString("blackUsername");
        String myGameName = sqlSet.getString("gameName");
        boolean myWhiteCheck = sqlSet.getBoolean("whiteTaken");
        boolean myBlackCheck = sqlSet.getBoolean("blackTaken");

        // finally gets the game from the database
        String game = sqlSet.getString("game");
        Object myGameObj = serializer.jsonToObj(game,ChessGame.class);

        // should be a game object, so it converts it
        ChessGame myGame = (ChessGame) myGameObj;

        // finally build the object and returns it
        Game foundGame = new Game(myGameId,myWhiteUser,myBlackUser,myGameName,myWhiteCheck,myBlackCheck);
        foundGame.setGame(myGame); // sets the gameboard to the new one

        return foundGame;
    }
}
