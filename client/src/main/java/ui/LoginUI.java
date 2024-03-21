package ui;

import models.reqModels.*;
import server.InvalidRequestException;
import server.ServerFacade;

import java.util.Scanner;

public class LoginUI extends ChessUI{

    private boolean runtimeUser = true;

    public LoginUI() {

    }

    // basic starter page for the user to use
    public void run(){
        String input;

        intro();

        do{
            // gets simple input from the user
            printPrompt();
            input = userInput.nextLine();
            input = input.toLowerCase();

            switch(input){

                case "register":

                    var newUser = registerUser();

                    // a simple check to make sure they are not entering new lines as username or password
                    if(!checkPackage(newUser)){
                        System.out.println("\nBad username or password, please enter something else");
                        continue;
                    }

                    try{
                        var user = server.register(newUser);

                        // you would create next ui and everything here
                        HomeScreenUI home = new HomeScreenUI(user.authToken(),user.username());
                        home.run();

                    }catch(InvalidRequestException error){

                        if(error.getErrorCode() == 403) {
                            System.out.println("\nUsername is already taken, please try again");
                        }else{
                            handleError(error.getErrorCode());
                        }
                    }

                    break;
                case "login":

                    var loginUser = loginUser();

                    try{
                        var user = server.login(loginUser);

                        // you would create next ui and everything here
                        HomeScreenUI home = new HomeScreenUI(user.authToken(),user.username());
                        home.run();

                    }catch(InvalidRequestException error){

                        if(error.getErrorCode() == 401){
                            System.out.println("\nInvalid username or password");
                        }else {
                            handleError(error.getErrorCode());
                        }
                    }
                    break;
                case "help":
                    help();
                    break;
                case "quit":
                    runtimeUser = false;
                    break;
                default:
                    options();
                    break;

            }
        }while(runtimeUser);

        // closes scanner and logs the user out
        userInput.close();
        System.out.println("Exiting Chess");
    }

    // main functions that allow you to go to other pages
    private RequestRegisterPackage registerUser(){
        Scanner regScanner = new Scanner(System.in);

        System.out.println("creating new account");
        System.out.print("Please enter your username: ");
        String username = regScanner.nextLine();

        System.out.print("Please enter a new password: ");
        String password = regScanner.nextLine();

        System.out.print("Please enter a email: ");
        String email = regScanner.nextLine();

        return new RequestRegisterPackage(username,password,email);

    }

    private RequestLoginPackage loginUser(){
        Scanner regScanner = new Scanner(System.in);

        System.out.print("Please enter your username: ");
        String username = regScanner.nextLine();

        System.out.print("Please enter your password: ");
        String password = regScanner.nextLine();

        return new RequestLoginPackage(username,password);
    }

    private boolean checkPackage(RequestRegisterPackage registerPackage){
        return !(registerPackage.username().isEmpty() || registerPackage.password().isEmpty());
    }

    // helper functions just for printing

    private void intro(){
        clearScreen();
        System.out.println("Welcome to Chess!");
        System.out.println("Please login or register a new account\n");
        System.out.println("Type options for more help");
    }

    private static void help(){
        System.out.println("This is the 240 chess login page!\n");
        System.out.println("To log in, type \"login\" and click enter. At");
        System.out.println("it will then ask for your username and password.\n");

        System.out.println("If you don't have an account, type \"register\" where");
        System.out.println("it will prompt you to enter username, password");
        System.out.println("and email. Typing \"help\" brings this menu and typing");
        System.out.println("\"options\" brings up a short-hand list of commands you can");
        System.out.println("use.\n");

        System.out.println("Finally, typing \"quit\" exits the program for you\n");
    }

    private static void options(){
        System.out.println("options:");
        System.out.println("\t - help (description of website)");
        System.out.println("\t - options (quick description)");
        System.out.println("\t - register (create new account)");
        System.out.println("\t - login (log into your account)");
        System.out.println("\t - quit (quit program)\n");
    }

}
