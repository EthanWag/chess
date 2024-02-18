package models;

import chess.ChessGame;

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
}
