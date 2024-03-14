import server.ServerFacade;

public class Main {
    public static void main(String[] args) {

        // we want to grab the correct arguement from here and then pass it to

        String myURL = "http://localhost:8080";
        ServerFacade server = new ServerFacade(myURL);

        server.register();


    }
}