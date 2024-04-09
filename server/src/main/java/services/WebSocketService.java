package services;

import dataAccess.DataAccessException;
import models.Game;

// simple class that handles requests made by the websocket, non accessible from the user

public class WebSocketService extends Service{

    public WebSocketService(){}

    public String getAuthUsername(String authToken) throws DataAccessException {
        var user = getAuthData(authToken);
        return user.username();
    }
}
