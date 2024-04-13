package services;

import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import models.Game;

// simple class that handles requests made by the websocket, non accessible from the user

public class WebSocketService extends Service{




    public WebSocketService(){}

    public String getAuthUsername(String authToken) throws DataAccessException {
        var user = getAuthData(authToken);
        return user.username();
    }

    public void updateGame(int gameId, Game game)throws DataAccessException{
        try {
            SqlGameDAO update = new SqlGameDAO();
            update.updateGame(gameId, game);
            update.commit();
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
}
