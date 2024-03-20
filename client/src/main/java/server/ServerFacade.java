package server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import ClientValues.*;
import models.resModels.*;
import models.reqModels.*;

import server.Server;

public class ServerFacade {

    private final String serverUrl;

    private static final GsonConverter GSON_CONVERTER = new GsonConverter();

    public ServerFacade(String myUrl){
        serverUrl = myUrl;

    }

    // methods for all of the end points
    public ResponseRegisterPackage register(RequestRegisterPackage registerUser) throws InvalidRequestException{
        return requestServer(MethodType.POST,"/user",registerUser, ResponseRegisterPackage.class,
                  false,"");
    }
    public ResponseLoginPackage login(RequestLoginPackage loginUser) throws InvalidRequestException{
        return requestServer(MethodType.POST,"/session",loginUser, ResponseLoginPackage.class,false,"");
    }
    public void logout(String authToken) throws InvalidRequestException {
        requestServer(MethodType.DELETE,"/session",null,null,true,authToken);
    }
    public ResponseListPackage listGame(String authToken) throws InvalidRequestException {
        return requestServer(MethodType.GET,"/game",null,ResponseListPackage.class,true,authToken);
    }
    public ResponseGamePackage createGame(RequestCreatePackage createGame, String authToken) throws InvalidRequestException {
        return requestServer(MethodType.POST,"/game",createGame,ResponseGamePackage.class,true,authToken);
    }
    public void joinGame(RequestJoinPackage joinGame, String authToken) throws InvalidRequestException {
        requestServer(MethodType.PUT,"/game",joinGame,null,true,authToken);
    }

    // methods that analyze and build http request are called by the endpoint methods
    private <T>T requestServer(MethodType method, String path, Object requestBody, Class<T> responseClass,
                               boolean authNeeded, String authToken) throws InvalidRequestException{

        String strUrl = serverUrl + path;

        try {
            // first sets up the url object
            URL myUrl = (new URI(strUrl)).toURL();

            // then makes the connection on the internet, also casts it
            HttpURLConnection httpConnect = (HttpURLConnection) myUrl.openConnection();
            httpConnect.setRequestMethod(typeToString(method));
            httpConnect.setDoOutput(true);

            // fills in the
            requestAuth(authNeeded, authToken, httpConnect); // puts auth in header
            writeRequestBody(requestBody, httpConnect); // this adds a body to our request

            // right here, he makes a write body function, that fill in a body given a x amount of objects
            httpConnect.setConnectTimeout(5000); // 5 seconds before giving put
            httpConnect.connect();

            // finds error codes and returns what error happened because of the server
            int httpCode = httpConnect.getResponseCode();

            return switch (httpCode) {
                case 400 -> throw new InvalidRequestException("Bad Request", 400);
                case 401 -> throw new InvalidRequestException("Unauthorized", 401);
                case 403 -> throw new InvalidRequestException("Already Taken", 403);
                case 404 -> throw new InvalidRequestException("Not Found", 404);
                case 500 -> throw new InvalidRequestException("Server Error", 500);
                case 501 -> throw new InvalidRequestException("Randomization fail", 501);
                case 200 -> readResponseBody(responseClass, httpConnect);
                default -> throw new IOException("unaccounted error");
            };
        }catch(IOException ioErr){ // error when translating ResponseBody
            throw new InvalidRequestException("Server failure", 500);
        }catch(URISyntaxException urlError){
            throw new InvalidRequestException("Server failure", 500);
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
    private static <T> T readResponseBody(Class<T> structure, HttpURLConnection connection) throws IOException{
        T response = null; // start off with returned object

        // if there is contents, then return them into what we need
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){

            var streamBody = reader.lines();
            String strBody = streamToString(streamBody);

            if(!strBody.equals("{}")) {
                response = (T) GSON_CONVERTER.jsonToObj(strBody, structure); // Note: this code works if you write it correctly
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
