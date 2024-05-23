package services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import models.Game;

// simple class that handles requests made by the websocket, non accessible from the user

public class WebSocketService extends Service{

    private SqlGameDAO gameAccess;

    public WebSocketService()throws DataAccessException{
        try{
            gameAccess = new SqlGameDAO();
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Unable to connect to database",500);
        }
    }

    public String getAuthUsername(String authToken) throws DataAccessException {
        var user = getAuthData(authToken);
        return user.username();
    }

    public void updateGame(int gameId, Game game)throws DataAccessException{
        try {
            gameAccess.updateGame(gameId, game.getGame());
        }catch(DataAccessException error){
            throw new DataAccessException("unable to update board",500);
        }
    }

    public Game getGameDb(int gameId) throws DataAccessException{
        try {
            return super.getGame(gameId);
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: game could not be found",404);
        }
    }

    public void leaveGame(int gameId,ChessGame.TeamColor color)throws DataAccessException{
        try {
            if ((color == ChessGame.TeamColor.WHITE) || (color == ChessGame.TeamColor.BLACK)) {
                gameAccess.updatePlayer(gameId,null,false,color);
            }
        }catch(DataAccessException error){
            throw new DataAccessException("ERROR: Failure to leave game",500);
        }
    }
}
