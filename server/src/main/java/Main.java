import chess.*;

import server.Server;

public class Main {
    public static void main(String[] args) {

        Server ChessServer = new Server();
        ChessServer.run(8080);

    }
}