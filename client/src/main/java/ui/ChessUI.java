package ui;

import java.util.Scanner;

import server.ServerFacade;

public abstract class ChessUI {

    protected ServerFacade server = new ServerFacade("http://localhost:8080");

    protected static Scanner userInput = new Scanner(System.in);

    public abstract void run();

    protected void printPrompt(){
        System.out.print(">>> ");
    }

}

