package webSocketMessages.userCommands;

import java.util.Objects;

public class ResignMessage extends UserGameCommand{

    public ResignMessage(String myAuthToken, int myGameId){
        super(CommandType.RESIGN,myAuthToken,myGameId);
    }
}
