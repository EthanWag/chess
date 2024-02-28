package services.handlers;

import com.google.gson.JsonSyntaxException;
import spark.*;

import com.google.gson.Gson;

public class ConvertGson {
    // simple constructor

    private final Gson serializer = new Gson();

    public ConvertGson(){

    }

    // this would need to throw an error encase it couldn't make it
    public Object requestToObj(Request jsonStr, Class<?> structure) throws JsonSyntaxException {

        // throws an exception if it can't create the new object
        try {
            return serializer.fromJson(jsonStr.body(), structure);

        }catch(Exception error){
            throw new JsonSyntaxException("[400](Invalid input)(ConvertGson) invalid entry");
        }
    }

    public String objToJson(Object data){

        // throws an exception if it serializes object
        try {
            return serializer.toJson(data);

        }catch(Exception error){
            throw new JsonSyntaxException("[400](Invalid input)(ConvertGson) invalid string");
        }
    }

}
