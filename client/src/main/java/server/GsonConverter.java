package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonConverter {

    private final Gson serializer = new Gson();

    public GsonConverter(){}

    public Object jsonToObj(String str, Class<?> structure) throws JsonSyntaxException {

        try {
            return serializer.fromJson(str, structure);

        }catch(JsonSyntaxException gsonErr){
            throw new JsonSyntaxException("ERROR:Invalid entry");
        }
    }

    public String objToJson(Object data) throws JsonSyntaxException{

        try {
            return serializer.toJson(data);

        }catch(JsonSyntaxException gsonErr){
            throw new JsonSyntaxException("ERROR:Invalid entry");
        }
    }

}
