package models;

import chess.ChessGame;

import java.io.IOException;
import java.util.Objects;

public class Game {

    // another option is to put a over value here so that you know easily if the game is over

    private int gameID; // this is a temp value, this will change as they are added to the database
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;
    private boolean whiteTaken;
    private boolean blackTaken;

    public Game(int myGameId, String myWhiteUser, String myBlackUser, String myGameName, boolean myWhiteTaken, boolean myBlackTaken){ // possibly get rid of Id

        gameID = myGameId;
        whiteUsername = myWhiteUser;
        blackUsername = myBlackUser;
        gameName = myGameName;
        whiteTaken = myWhiteTaken;
        blackTaken = myBlackTaken;
        game = new ChessGame(); // makes a new game

    }

    // getters and setters
    public int getGameID() {
        return gameID;
    }
    public void setGameID(int newGameId){
        gameID = newGameId;
    }
    public void setGame(ChessGame newGame){
        game = newGame;
    }
    public ChessGame getGame() {
        return game;
    }

    public String getGameName(){
        return gameName;
    }

    public String getWhiteUsername(){
        return whiteUsername;
    }
    public String getBlackUsername(){
        return blackUsername;
    }

    // getters for checkers

    public boolean isWhiteTaken(){
        return whiteTaken;
    }

    public boolean isBlackTaken(){
        return blackTaken;
    }


    public ChessGame.TeamColor getColor(String username)throws IOException{
        if(whiteTaken && (whiteUsername.equals(username))){
            return ChessGame.TeamColor.WHITE;
        }else if(blackTaken && (blackUsername.equals(username))){
            return ChessGame.TeamColor.BLACK;
        }else{
            throw new IOException("Not a player");
        }
    }


    // override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game1 = (Game) o;
        return gameID == game1.gameID && Objects.equals(whiteUsername, game1.whiteUsername) && Objects.equals(blackUsername, game1.blackUsername) && Objects.equals(gameName, game1.gameName) && Objects.equals(game, game1.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

    // methods for joining games

    public void joinWhiteSide(String username){

        whiteUsername = username;
        whiteTaken = true;
    }

    public void joinBlackSide(String username){

        blackUsername = username;
        blackTaken = true;
    }

}
