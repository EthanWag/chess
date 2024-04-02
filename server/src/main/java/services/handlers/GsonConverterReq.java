package services.handlers;

import ConvertToGson.GsonConverter;
import com.google.gson.JsonSyntaxException;
import spark.*;

public class GsonConverterReq extends GsonConverter {
    // simple constructor

    public GsonConverterReq(){
        super(); // should just initialize the way GsonConverter does
    }

    // this would need to throw an error encase it couldn't make it
    public Object requestToObj(Request jsonStr, Class<?> structure) throws JsonSyntaxException {

        // throws an exception if it can't create the new object
        try {
            return serializer.fromJson(jsonStr.body(), structure);

        }catch(JsonSyntaxException gsonErr){
            throw new JsonSyntaxException("ERROR:Invalid entry");
        }
    }
}
