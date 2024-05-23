package webSocketMessages.ServerMessages;

import java.util.Objects;

public class ErrorMessage extends ServerMessage{

    String errorMessage;
    int errCode;

    public ErrorMessage(String myMessage, int myErrorCode){
        super(ServerMessageType.ERROR);
        errorMessage = myMessage;
        errCode = myErrorCode;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public int getErrCode(){
        return errCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErrorMessage that = (ErrorMessage) o;
        return errCode == that.errCode && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorMessage, errCode);
    }
}
