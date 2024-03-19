package server;

public class InvalidRequestException extends Exception{

    private final int errorCode;

    public InvalidRequestException(String message, int myErrorCode){
        super(message);
        errorCode = myErrorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }
}
