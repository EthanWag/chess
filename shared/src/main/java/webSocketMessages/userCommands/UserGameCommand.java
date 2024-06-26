package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    protected UserGameCommand(CommandType command,String authToken,int gameId) {
        this.commandType = command;
        this.authToken = authToken;
        this.gameID = gameId;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    protected final String authToken;

    protected final int gameID;


    public String getAuthString() {
        return this.authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public int getGameID(){
        return this.gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGameCommand that = (UserGameCommand) o;
        return gameID == that.gameID && commandType == that.commandType && Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, authToken, gameID);
    }
}
