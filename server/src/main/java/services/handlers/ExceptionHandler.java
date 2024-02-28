package services.handlers;

import spark.Response;

import java.util.regex.Pattern;
import java.util.Map;

public class ExceptionHandler {

    private static ConvertGson GsonConverter = new ConvertGson();

    public ExceptionHandler() {}

    public String ExceptionHandler(Exception error, Response response){

        String errorType = error.getClass().getName();
        String message;

        switch(errorType){

            case "dataAccess.DataAccessException":

                if(error.getMessage().contains("404")){

                    message = "Error: data not found";
                    response.status(404);
                    return GsonConverter.ObjToJson(Map.of("message",message));

                }else if(error.getMessage().contains("403")){

                    message = "Error: already taken";
                    response.status(403);
                    return GsonConverter.ObjToJson(Map.of("message",message));

                }else if(error.getMessage().contains("401")){

                    message = "Error: unauthorized";
                    response.status(401);
                    return GsonConverter.ObjToJson(Map.of("message",message));

                }else if(error.getMessage().contains("400")){

                    message = "Error: bad request";
                    response.status(400);
                    return GsonConverter.ObjToJson(Map.of("message",message));

                }

            case "com.google.gson.JsonSyntaxException":

                // you should only be getting 400's here
                message = "Error: bad request";
                response.status(400);
                return GsonConverter.ObjToJson(Map.of("message",message)); // returns 400 if it didn't work

            default:
                // should never get here but just in case
                System.out.println(error.getMessage());
                System.out.println(errorType);
                System.out.println("unknown error");

                message = "Error: Unknown error";
                response.status(405);
                return GsonConverter.ObjToJson(Map.of("message",message)); // returns 400 if it didn't work

        }
    }
}
