package services.handlers;

import spark.Response;

public class ExceptionHandler {

    private static ConvertGson GsonConverter = new ConvertGson();

    public ExceptionHandler() {}

    public String ExceptionHandler(Exception error, Response response){

        String errorType = error.getClass().getName();
        String message;

        switch(errorType){

            case "dataAccess.DataAccessException":
                // prints a message and lets the user know
                message = "Error: already taken";
                response.status(403);
                return GsonConverter.ObjToJson(message);

            case "com.google.gson.JsonSyntaxException":
                message = "Error: bad request";
                response.status(400);
                return GsonConverter.ObjToJson(message);
            default:
                System.out.println(errorType);
                System.out.println("unknown error");
        }

        return "null";

    }

}
