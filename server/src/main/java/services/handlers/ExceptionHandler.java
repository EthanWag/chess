package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import spark.Response;

import java.util.Map;

public class ExceptionHandler {

    private static GsonConverterReq gsonConverter = new GsonConverterReq();

    public ExceptionHandler() {}

    public String handleException(DataAccessException error, Response response){

        String message;

        try {

            switch (error.getErrorCode()) {

                case 404:

                    message = "Error: data not found";
                    response.status(404);
                    return gsonConverter.objToJson(Map.of("message", message));

                case 403:

                    message = "Error: already taken";
                    response.status(403);
                    return gsonConverter.objToJson(Map.of("message", message));

                case 401:

                    message = "Error: unauthorized";
                    response.status(401);
                    return gsonConverter.objToJson(Map.of("message", message));

                case 400:

                    message = "Error: bad request";
                    response.status(400);
                    return gsonConverter.objToJson(Map.of("message", message));

                case 500:
                    message = "Error: Database access error";
                    response.status(500);
                    return gsonConverter.objToJson(Map.of("message", message));

                default:
                    // should never get here but just in case
                    System.out.println(error.getMessage());
                    System.out.println("unknown error");

                    message = "Error: Unknown error";
                    response.status(500);
                    return gsonConverter.objToJson(Map.of("message", message)); // returns 400 if it didn't work
            }

        }catch(JsonSyntaxException jsonErr){

            return jsonException(response);


        }catch(Exception err){

            response.status(500);
            return "{ Error: Unknown Server error }";

        }
    }

    // handles JsonSyntaxException errors
    public String jsonException(Response response){

        String message = "Error: bad request";
        response.status(400);
        return gsonConverter.objToJson(Map.of("message", message));

    }

}
