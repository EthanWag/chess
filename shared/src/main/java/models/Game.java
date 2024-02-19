package models;

import chess.ChessGame;

import java.util.Objects;

public class Game {

    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private final ChessGame game;

    public Game(int myGameID, String myWhiteUser, String myBlackUser, String myGameName){

        gameID = myGameID;
        whiteUsername = myWhiteUser;
        blackUsername = myBlackUser;
        gameName = myGameName;
        game = new ChessGame(); // makes a new game

    }

    public Game(Game other){
        gameID = other.gameID;
        whiteUsername = other.whiteUsername;
        blackUsername = other.blackUsername;
        gameName = other.gameName;
        game = new ChessGame(other.game);
    }

    // getters and setters
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
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
}
