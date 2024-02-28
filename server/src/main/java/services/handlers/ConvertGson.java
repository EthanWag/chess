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




    // TODO:
    // add some more classes that that tackle more complex objects
    // also if you need to grab users, or need to specify objects, create some more methods useful for that
    // check to see if the http request even made any seanse

    // needs to be a json string which would be created in the handler
    // maybe split code into seprate classes

}
