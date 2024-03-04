package dataAccess;

import models.Game;
import chess.ChessGame;
import services.handlers.ConvertGson;

import java.sql.*;
import java.util.Collection;

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

    public void create(Game newGame) throws DataAccessException{

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
                newGame.setGameID(key.getInt(1));
            }else{
                throw new DataAccessException("[501](auto-generate failed)your game failed generate an id");
            }

        }catch(Exception error){
            throwConnectError(); // throws an error it couldn't connect to the database
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

                int myGameId = resultItems.getInt("gameId");
                String myWhiteUser = resultItems.getString("whiteUsername");
                String myBlackUser = resultItems.getString("blackUsername");
                String myGameName = resultItems.getString("gameName");
                boolean myWhiteCheck = resultItems.getBoolean("whiteTaken");
                boolean myBlackCheck = resultItems.getBoolean("blackTaken");

                // finally gets the game from the database
                String game = resultItems.getString("game");
                Object myGameObj = serializer.strToObj(game,ChessGame.class);

                // should be a game object, so it converts it
                ChessGame myGame = (ChessGame) myGameObj;

                // finally build the object and returns it
                Game foundGame = new Game(myGameId,myWhiteUser,myBlackUser,myGameName,myWhiteCheck,myBlackCheck);
                foundGame.setGame(myGame); // sets the gameboard to the new one

                return foundGame;

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

    private void throwConnectError() throws DataAccessException{
        throw new DataAccessException("[500](Connection) Unable to connect to database");
    }





    public static void main(String [] args){

        try {

            SqlGameDAO myData = new SqlGameDAO();

            Game newGame = new Game(-1,"Timmy","Tommy","CHESS EXTRANGVZA",false,false);

            myData.create(newGame);

            Game getGame = myData.read(1);

            myData.deleteAll();
            System.out.println("Success");




        }catch(Exception error){
            System.out.println(error.getMessage());
        }
    }

}
