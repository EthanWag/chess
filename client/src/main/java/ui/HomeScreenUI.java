package ui;

import models.Game;
import models.reqModels.*;
import models.resModels.*;
import server.InvalidRequestException;

import java.util.Scanner;

public class HomeScreenUI extends ChessUI{

    private boolean runtimeUser = true;
    private final String authorization;
    private final String username;
    public HomeScreenUI(String newAuthToken, String myUsername){
        authorization = newAuthToken;
        username = myUsername;
        // put some code here to clear the terminal
    }

    public void run(){
        String input;

        do{
            // gets simple input from the user
            printPrompt();
            input =  userInput.nextLine();
            input = input.toLowerCase();

            switch(input){
                case "list games":
                    try{
                        var gamesList = server.listGame(authorization);
                        listGames(gamesList);

                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }

                    break;
                case "new game":

                    var newGame = createGame();

                    try{
                        var game = server.createGame(newGame,authorization);

                        System.out.println("Game name: " + newGame.gameName());
                        System.out.println("Game Id: " + game.gameID());
                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }
                    break;

                case "join":

                    RequestJoinPackage joinPackage;

                    // makes sure they enter a valid number
                    while(true) {
                        try {
                            joinPackage = joinGame();
                            break;
                        } catch (InvalidRequestException error) {
                            System.out.println("Invalid game ID, please enter again");
                        }
                    }
                    System.out.println("Joining Game...");

                    try{
                        server.joinGame(joinPackage,authorization);
                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }

                    // write code here that will join the game

                    break;

                case "help":
                    help();
                    break;
                case "logout":
                    try{
                        server.logout(authorization);
                        runtimeUser = false;
                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }
                    break;
                default:
                    options();
                    break;
            }
        }while(runtimeUser);
    }

    private RequestCreatePackage createGame(){
        Scanner createScanner = new Scanner(System.in);

        System.out.print("Enter a new game name: ");
        String gameName = createScanner.nextLine();

        return new RequestCreatePackage(gameName);
    }

    private RequestJoinPackage joinGame() throws InvalidRequestException{
        Scanner joinGameScanner = new Scanner(System.in);

        System.out.print("Please enter a game ID: ");
        String strInt = joinGameScanner.nextLine();

        int gameID = strToInt(strInt);

        System.out.print("Please enter team color: ");
        String teamColor = joinGameScanner.nextLine();

        teamColor = teamColor.toUpperCase();
        return new RequestJoinPackage(teamColor,gameID);
    }

    private void listGames(ResponseListPackage resList){

        var allGames = resList.games();
        if(allGames.isEmpty()){
            System.out.println("No games available!!");
        }

        System.out.println("Games you can currently play\n");
        for(Game oneGame : allGames){

            StringBuilder gameInfo = new StringBuilder();

            gameInfo.append("\tChess Game: ");
            gameInfo.append(oneGame.getGameName());
            gameInfo.append(" | Game ID: ");
            gameInfo.append(oneGame.getGameID());

            System.out.println(gameInfo);
        }

        System.out.println();
    }

    private int strToInt(String userVal) throws InvalidRequestException{
        try{
            return Integer.parseInt(userVal);
        }catch(NumberFormatException error){
            throw new InvalidRequestException("Not a number", 400);
        }
    }

    // helper functions that help tell the user what they need to do to navigate this website
    private void handleError(int errorCode){
        switch(errorCode){
            case 400 -> System.out.println("400");
            case 401 -> System.out.println("401");
            case 403 -> System.out.println("403");
            case 404 -> System.out.println("404");
            case 500 -> System.out.println("500");
        }
    }

    private void help(){
        System.out.println("Welcome " + username + "!\n");
        System.out.println("This page is used is where you can explore, create");
        System.out.println("or join any new game! type \"options\" to see again a");
        System.out.println("short description of what you can do. Also keep in mind, if");
        System.out.println("you want to join a game, you need to know the game ID which you");
        System.out.println("can find by typing \"list games\" to see all game Id's\n");

        System.out.println("Finally, \"type logout\" to leave.");
    }

    private void options(){
        System.out.println("options:");
        System.out.println("\t - help (description of page)");
        System.out.println("\t - options (quick description)");
        System.out.println("\t - join (gameID) (join that game with that ID)");
        System.out.println("\t - list games (lists all possible games to can join)");
        System.out.println("\t - new game (create a new game)");
        System.out.println("\t - logout (logout of your account)\n");
    }

}
