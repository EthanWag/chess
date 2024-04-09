package webSocketMessages.userCommands;

import java.util.Objects;

public class JoinObserverMessage extends UserGameCommand{

    public JoinObserverMessage(String myAuthToken, int myGameId){
        super(CommandType.JOIN_OBSERVER,myAuthToken,myGameId);
    }
}
