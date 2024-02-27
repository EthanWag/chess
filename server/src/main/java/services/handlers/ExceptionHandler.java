package services.handlers;

import spark.*;

public class ExceptionHandler {

    private static ConvertGson GsonConverter = new ConvertGson();

    public ExceptionHandler() {}

    public String ExceptionHandler(Exception error, Response response){


        String errorType = error.getClass().getName();
        String errorMessage;

        switch(errorType){

            case "dataAccess.DataAccessException":
                response.status(403);
                errorMessage = "Error: already taken";

                return GsonConverter.ObjToJson(errorMessage);

            case "com.google.gson.JsonSyntaxException":
                response.status(400);
                errorMessage = "Error: bad request";

                return GsonConverter.ObjToJson(errorMessage);

            default:
                response.status(500);
                errorMessage = "Error: Server issues";

                return GsonConverter.ObjToJson(errorMessage);
        }
    }
}
