package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */

public class DataAccessException extends Exception{

    private final int errorCode;
    public DataAccessException(String message,int myErrorCode) {
        super(message);
        errorCode = myErrorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }

}


