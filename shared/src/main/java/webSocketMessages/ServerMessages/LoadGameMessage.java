package webSocketMessages.ServerMessages;

import java.util.Objects;

public class LoadGameMessage extends ServerMessage{

    String game;

    public LoadGameMessage(String myGame){
        super(ServerMessageType.LOAD_GAME);
        game = myGame;
    }

    public String getGame(){
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoadGameMessage that = (LoadGameMessage) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),game);
    }
}
