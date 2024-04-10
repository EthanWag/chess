package ui;

import java.util.Scanner;

import server.ServerFacade;

import static ui.EscapeSequences.*;

public abstract class ChessUI {

    protected String url = "http://localhost:8080";
    protected ServerFacade server = new ServerFacade("http://localhost:8080");
    protected static Scanner userInput = new Scanner(System.in);

    public abstract void run();
    protected void printPrompt(){
        System.out.print(">>> ");
    }
    protected void clearScreen() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    protected void handleError(int errorCode){
        switch(errorCode){
            case 400 -> System.out.println("Bad request from user, try again");
            case 401 -> System.out.println("Unauthorized User");
            case 403 -> System.out.println("request token, try again");
            case 404 -> System.out.println("User Not found");
            case 500 -> System.out.println("Server error, please try again");
        }
    }
}

