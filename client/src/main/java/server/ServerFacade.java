package server;

import static java.lang.System.exit;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import ClientValues.*;
import models.resModels.*;
import models.reqModels.*;
import models.Game;

public class ServerFacade {

    private final String serverUrl;

    private static final GsonConverter GSON_CONVERTER = new GsonConverter();

    public ServerFacade(String myUrl){
        serverUrl = myUrl;
    }

    // methods for all of the end points

    public ResponseRegisterPackage register(RequestRegisterPackage registerUser){
        return requestServer(MethodType.POST,"/user",registerUser, ResponseRegisterPackage.class,
                  false,"");
    }

    public ResponseLoginPackage login(RequestLoginPackage loginUser){
        return requestServer(MethodType.POST,"/session",loginUser, ResponseLoginPackage.class,false,"");
    }

    public void logout(String authToken){
        requestServer(MethodType.DELETE,"/session",null,null,true,authToken);
    }
    public ResponseListPackage listGame(String authToken){
        return requestServer(MethodType.GET,"/game",null,ResponseListPackage.class,true,authToken);
    }

    public ResponseGamePackage createGame(RequestCreatePackage createGame, String authToken){
        return requestServer(MethodType.POST,"/game",createGame,ResponseGamePackage.class,true,authToken);
    }

    public void joinGame(RequestJoinPackage joinGame, String authToken){
        requestServer(MethodType.PUT,"/game",joinGame,null,true,authToken);
    }


    // methods that analyze and build http request are called by the endpoint methods

    private <T>T requestServer(MethodType method, String path, Object requestBody, Class<T> responseClass,
                               boolean authNeeded, String authToken){

        // TODO: So you need to be able to put stuff into the request and response classes so you can send data over the
        // internet

        String strUrl = serverUrl + path;

        try{
            // first sets up the url object
            URL myUrl = (new URI(strUrl)).toURL();

            // then makes the connection on the internet, also casts it
            HttpURLConnection httpConnect = (HttpURLConnection) myUrl.openConnection();
            httpConnect.setRequestMethod(typeToString(method));
            httpConnect.setDoOutput(true);

            // fills in the
            requestAuth(authNeeded,authToken,httpConnect); // puts auth in header
            writeRequestBody(requestBody,httpConnect); // this adds a body to our request

            // right here, he makes a write body function, that fill in a body given a x amount of objects
            httpConnect.setConnectTimeout(5000); // 5 seconds before giving put
            httpConnect.connect();

            return readRequestBody(responseClass,httpConnect);

        }catch(Exception error){

            //FIXME: you need to fix this code so that you return an error, it catches all codes which makes it seem like it
            //FIXME: is failing

            System.err.println("Did not work");
            exit(0); // just exits program if it didn't work
            return null;
        }
    }

    // These two functions write and read the body of requests
    private static void writeRequestBody(Object request, HttpURLConnection connection)throws IOException {
        if(request != null){

            connection.addRequestProperty("Content-Type","appliation/json");
            String requestJsonString = GSON_CONVERTER.objToJson(request);

            // tries to write to the url connection, if unsuccessful, then throws an error
            try(OutputStream httpBody = connection.getOutputStream()){
                httpBody.write(requestJsonString.getBytes());
            }
        }
    }

    // given a http value,will read the contents of its body
    private static <T> T readRequestBody(Class<T> structure, HttpURLConnection connection) throws IOException{
        T response = null; // start off with returned object

        // if there is contents, then return them into what we need
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){

            var streamBody = reader.lines();
            String strBody = streamToString(streamBody);

            if(!strBody.equals("{}")) {
                response = (T) GSON_CONVERTER.jsonToObj(strBody, structure); //FIXME: make code better here
            }
        }
        return response;
    }

    // adds authToken to the http request
    private void requestAuth(boolean authNeeded, String authToken, HttpURLConnection connection){
        if(authNeeded){
            connection.setRequestProperty("authorization",authToken);
        }
    }


    // helper functions that will convert an enum to a string
    private String typeToString(MethodType type){
        return switch (type) {
            case GET -> "GET";
            case PUT -> "PUT";
            case DELETE -> "DELETE";
            case POST -> "POST";
        };
    }

    // simply given a stream in strings, will covert them into one string that you can use
    //TODO: debug this code, not sure if is working 100% of the time
    private static String streamToString(Stream<String> inputStream){

        StringBuilder builder = new StringBuilder();

        // converts the inputStream into a list and then converts that into a linked lis
        List<String> list = inputStream.toList();
        LinkedList<String> strList = new LinkedList<>(list);

        // goes through each node in the linked list and converts it to a string
        while(!strList.isEmpty()){
            String currentLine = strList.pop();
            builder.append(currentLine);
        }
        return builder.toString();
    }
}
