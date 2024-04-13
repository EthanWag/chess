package services;

import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import models.*;

public class JoinGameService extends Service{

    public JoinGameService() {}

    public void completeJob(String authToken, String clientColor, int gameId) throws DataAccessException{

        // gets user and game, throws exceptions if it can't find it
        User currentUser = checkAuthToken(authToken);
        Game currentGame = getGame(gameId);

        // gets the username of the player
        String username = currentUser.username();

        // adds the player to the game if they are
        addPlayer(currentGame,username,clientColor);
    }

    // Service functions

    private void addPlayer(Game joinGame, String username, String teamColor)throws DataAccessException{

        switch(teamColor){ //checks to see what team they entered
            case "WHITE":
                if(!joinGame.isWhiteTaken()){ // if white is not taken
                    joinGame.joinWhiteSide(username);

                    var accessGame = new SqlGameDAO();
                    accessGame.updatePlayer(joinGame.getGameID(),username,true,"WHITE");
                    accessGame.commit();


                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case "BLACK":
                if(!joinGame.isBlackTaken()){ // if black is not taken

                    var accessGame = new SqlGameDAO();
                    accessGame.updatePlayer(joinGame.getGameID(),username,true,"BLACK");
                    accessGame.commit();

                }else{
                    throw new DataAccessException("ERROR: User already taken",403);
                }
                break;

            case null:
                break; // they just want to watch the game


            default: // throws an error if they entered in a invalid team color

                // right here you need to add functionality of watching a game
                throw new DataAccessException("ERROR: Bad request",400);
        }
    }

    // for websocket connections


    // lets a player leave the game
    public void deletePlayer(String authToken,String username,int gameId)throws DataAccessException{

        try {
            checkAuthToken(authToken);
            Game currentGame = getGame(gameId);

            // now we need to search and see if the user is even in the game
            if((currentGame.getWhiteUsername().equals(username)) && currentGame.isWhiteTaken()){

                var accessGame = new SqlGameDAO();
                accessGame.updatePlayer(gameId,"",false,"WHITE");
                accessGame.commit();

            }else if((currentGame.getBlackUsername().equals(username)) && currentGame.isBlackTaken()){

                var accessGame = new SqlGameDAO();
                accessGame.updatePlayer(gameId,"",false,"BLACK");
                accessGame.commit();
            }else{
                System.err.println("There is an error here, be sure to figure it out");
            }
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Unauthorized",404);
        }
    }
}