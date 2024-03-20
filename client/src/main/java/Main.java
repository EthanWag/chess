import server.Server;
import ui.LoginUI;

public class Main {
    public static void main(String[] args) {

        // starts the server
        Server server = new Server();
        server.run(8080); // runs server when it starts

        LoginUI start = new LoginUI();
        start.run();

        server.stop();




    }
}