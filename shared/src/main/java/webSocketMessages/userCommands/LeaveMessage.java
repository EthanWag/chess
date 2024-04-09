package webSocketMessages.userCommands;

import java.util.Objects;

public class LeaveMessage extends UserGameCommand{

    public LeaveMessage(String myAuthToken, int myGameId){
        super(CommandType.LEAVE,myAuthToken,myGameId);
    }
}
