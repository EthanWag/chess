package dataAccess;

import models.Game;
import chess.ChessGame;
import services.handlers.GsonConverterReq;

import java.sql.*;
import java.util.Collection;
import java.util.ArrayList;

public class SqlGameDAO implements GameDAO{

    private static final GsonConverterReq serializer = new GsonConverterReq();

    public SqlGameDAO() throws DataAccessException{
    }

    public int create(Game newGame) throws DataAccessException{

        // converts game object to a string
        String gameStr = serializer.objToJson(newGame.getGame());

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("INSERT INTO GameDAO(whiteUsername,blackUsername,gameName,game,whiteTaken,blackTaken)\n");
        strBuilder.append("VALUES(?,?,?,?,?,?);");
        String sqlCreate = strBuilder.toString();

        Connection connection = open();

        try {
            var statement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);

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

                int myKey = key.getInt(1);
                close(connection);
                return myKey;
            }else{
                errClose(connection);
                throw new DataAccessException("ERROR:Auto generation failed",500);
            }

        }catch(SQLException sqlErr){
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public Game read(int gameId) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT * FROM GameDAO WHERE gameId=?");
        String sqlRead = strBuilder.toString();

        Connection connection = open();

        try{
            // grabs the connection and then tries the execute the program
            var statement = connection.prepareStatement(sqlRead,ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                    ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1,gameId);
            ResultSet resultItems = statement.executeQuery();

            // if the statement returns true, then it finds all info about this object and returns a new one
            if(resultItems.first()){

                var myGame = convertGame(resultItems);
                close(connection);

                return myGame;
            }else{
                errClose(connection);
                throw new DataAccessException("ERROR:Bad Request",400);
            }

        }catch(SQLException error){ // this tackles more the case that the connection was bad
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }
    public Collection<Game> getAll() throws DataAccessException{

        Collection<Game> allGames = new ArrayList<>();

        Connection connection = open();

        try {

            var statement = connection.prepareStatement("SELECT * FROM GameDAO");
            ResultSet resultItems = statement.executeQuery();

            while(resultItems.next()){
                Game listGame = convertGame(resultItems);
                allGames.add(listGame);
            }
            close(connection);
            return allGames;

        }catch(SQLException error){
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public void updatePlayer(int gameId,String username,boolean join, ChessGame.TeamColor color) throws DataAccessException{

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE GameDAO ");

        if(color == ChessGame.TeamColor.WHITE){
            strBuilder.append("SET whiteUsername = ?, whiteTaken = ? ");
        }else if(color == ChessGame.TeamColor.BLACK){
            strBuilder.append("SET blackUsername = ?, blackTaken = ? ");
        }else{
            throw new DataAccessException("ERROR: Not a Color",400);
        }

        strBuilder.append("WHERE gameId = ?");
        String sqlUpdate = strBuilder.toString();

        Connection connection = open();

        try{

            // grabs the connection and then tries the execute the program
            var statement = connection.prepareStatement(sqlUpdate);
            statement.setString(1,username);

            statement.setBoolean(2,join);

            statement.setInt(3,gameId);
            statement.executeUpdate();
            close(connection);

        }catch(SQLException sqlErr){ // this tackles more the case that the connection was bad
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public void updateGame(int gameId, ChessGame updateGame)throws DataAccessException{

        String strGame = serializer.objToJson(updateGame);

        // first builds the string we are going to send to the database
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE GameDAO ");

        strBuilder.append("SET game = ?");

        strBuilder.append("WHERE gameId = ?");
        String sqlUpdate = strBuilder.toString();

        Connection connection = open();

        try{

            // grabs the connection and then tries the execute the program
            var statement = connection.prepareStatement(sqlUpdate);


            statement.setString(1,strGame);
            statement.setInt(2,gameId);
            statement.executeUpdate();
            close(connection);

        }catch(SQLException sqlErr){ // this tackles more the case that the connection was bad
            errClose(connection);
            throw new DataAccessException("ERROR: Database connection lost",500);
        }
    }

    public void deleteAll(){
        try {
            Connection connection = open();
            var statement = connection.prepareStatement("TRUNCATE TABLE GameDAO");
            statement.execute();
            close(connection);

        } catch (Exception error){
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


/* TODO:
*
* So when you are working on concurrency, make sure to come back and work on making a proper connection manager.
* The one you currently have works because it opens and closes connections very quickly. that works for small applications
* but in the real world you will want something more professional. Having a connection manager will be beneficial as
* as it will handle a log of the back end spesfics you might not want to deal with. In short it's cleaner and will work better
* with concurrency
*
* That being said it's going to need to have a complete re-design of the backend of this project so make sure you do this later
*
 */
