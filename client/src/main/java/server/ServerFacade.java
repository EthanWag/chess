package server;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

import ClientValues.*;

import static java.lang.System.exit;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String myUrl){
        serverUrl = myUrl;
    }


    // create Url methods

    // methods for all of the end points

    public void register(){
        requestServer(MethodType.POST,"/user",null,null);
    }

    public void login(){
        requestServer(MethodType.POST,"/session",null,null);
    }

    public void logout(){
        requestServer(MethodType.DELETE,"/session",null,null);
    }

    public void listGame(){
        requestServer(MethodType.GET,"/game",null,null);
    }

    public void createGame(){
        requestServer(MethodType.POST,"/game",null,null);
    }

    public void joinGame(){
        requestServer(MethodType.PUT,"/game",null,null);
    }


    // methods that analyze and build http request are called by the endpoint methods

    private <T>T requestServer(MethodType method, String path, Object request, Class<T> responseClass){

        // TODO: So you need to be able to put stuff into the request and response classes so you can send data over the
        // internet

        // make some testcases for each method

        String strUrl = serverUrl + path;

        try{
            // first sets up the url object
            URL myUrl = (new URI(strUrl)).toURL();

            // then makes the connection on the internet, also casts it
            HttpURLConnection httpConnect = (HttpURLConnection) myUrl.openConnection();
            httpConnect.setRequestMethod(typeToString(method));
            httpConnect.setDoOutput(true);

            // writeBody(request,httpConnect); this is where you would want to turn things into json to the computer
            httpConnect.setConnectTimeout(5000); // 5 seconds before giving put
            httpConnect.connect();



        }catch(Exception error){

            System.err.println("Did not work");
            exit(0); // just exits program if it didn't work
        }

        return null;
    }







    // helper functions that will convert an enum to a string

    // given a client MethodType Value, will return it's string
    private String typeToString(MethodType type)throws Exception{
        return switch (type) {
            case GET -> "GET";
            case PUT -> "PUT";
            case DELETE -> "DELETE";
            case POST -> "POST";
            default -> {
                System.err.println("ERROR: Invalid of function, check code");
                yield "ERROR";
            }
        };

    }
}
