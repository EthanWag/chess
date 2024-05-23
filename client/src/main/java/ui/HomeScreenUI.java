package ui;

import java.util.HashMap;

import WebSocketClient.WebSocketFacade;
import chess.ChessGame;
import models.Game;
import models.reqModels.*;
import models.resModels.*;
import server.InvalidRequestException;

import java.util.Scanner;

import static java.lang.System.exit;

public class HomeScreenUI extends ChessUI{

    private boolean runtimeUser = true;
    private final String authorization;
    private final String username;

    private final HashMap<String,Game> gameList = new HashMap<>();
    public HomeScreenUI(String newAuthToken, String myUsername){
        authorization = newAuthToken;
        username = myUsername;
        // put some code here to clear the terminal
    }

    public void run(){
        String input;

        intro();

        do{
            // gets simple input from the user
            printPrompt();
            input =  userInput.nextLine();
            input = input.toLowerCase();

            switch(input){
                case "list games":
                    try{
                        loadGames(authorization);
                        listGames();

                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }

                    break;
                case "new game":

                    var newGame = createGame();

                    try{
                        server.createGame(newGame,authorization);
                    }catch(InvalidRequestException error){
                        handleError(error.getErrorCode());
                    }
                    break;

                case "join":

                    RequestJoinPackage joinPackage;

                    while(true) {
                        try {
                            joinPackage = joinGame();
                            break;
                        } catch (InvalidRequestException error) {
                            System.out.println("Invalid game ID, please enter again");
                        }
                    }

                    try{
                        server.joinGame(joinPackage,authorization);
                        System.out.println("Joining Game...");

                        // this should be the id of the game we want to join, this will be important for tons if different things
                        int gameID = joinPackage.gameID();
                        boolean player;

                        player = !joinPackage.playerColor().equalsIgnoreCase("watch");

                        GameScreenUI playChess = new GameScreenUI(authorization,null,
                                                findColor(joinPackage.playerColor()),gameID,player);


                        WebSocketFacade newConnection = new WebSocketFacade(url,playChess);
                        playChess.setFacade(newConnection);
                        playChess.run();

                    }catch(InvalidRequestException error){
                        if(error.getErrorCode() == 400) {
                            System.out.println("\nInvalid ID or Invalid team color, try again");
                        }else if(error.getErrorCode() == 403){
                            System.out.println("\nColor already taken, try again");
                        }else{
                            handleError(error.getErrorCode());
                        }
                    }

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

        System.out.println("\n Please login to play Chess!");
    }

    private RequestCreatePackage createGame(){
        Scanner createScanner = new Scanner(System.in);

        System.out.print("Enter a new game name: ");
        String gameName = createScanner.nextLine();

        // calls again until they enter valid info
        if(gameList.containsKey(gameName)){
            System.out.println("Game already named " + gameName);
            System.out.println(".Please enter a new game");
            return createGame();
        }

        return new RequestCreatePackage(gameName);
    }

    private RequestJoinPackage joinGame() throws InvalidRequestException{

        // first loads all changes
        loadGames(authorization);

        Scanner joinGameScanner = new Scanner(System.in);

        System.out.print("Please enter a game name: ");
        String gameName = joinGameScanner.nextLine();


        // checks to make sure that that gave even exsists
        int myGameId = -1;
        if(gameList.get(gameName) != null){
            myGameId = gameList.get(gameName).getGameID();
        }

        System.out.print("Please enter team color: ");
        String teamColor = joinGameScanner.nextLine();

        if (teamColor.equalsIgnoreCase("RESIGN")) {
            exit(0);
        }

        teamColor = teamColor.toUpperCase();
        return new RequestJoinPackage(teamColor,myGameId);
    }

    private void listGames(){

        if(gameList.isEmpty()){
            System.out.println("\nNo games available!!");
            return;
        }

        System.out.println("Games you can currently play\n");
        for(String gameName : gameList.keySet()){

            // gets the game that we are going to use
            var oneGame = gameList.get(gameName);

            StringBuilder gameInfo = new StringBuilder();

            gameInfo.append("\tChess Game: ");
            gameInfo.append(gameName);

            // writes different players and their usernames
            gameInfo.append("\n\t  - White Player: ");

            // prints white player
            String whitePlayer = oneGame.getWhiteUsername();
            if((whitePlayer == null) || (whitePlayer.isEmpty())){
                gameInfo.append("Empty Player");
            }else{
                gameInfo.append(whitePlayer);
            }

            gameInfo.append("\n\t  - Black Player: ");

            // prints black player
            String blackPlayer = oneGame.getBlackUsername();
            if((blackPlayer == null) || (blackPlayer.isEmpty())){
                gameInfo.append("Empty Player");
            }else{
                gameInfo.append(blackPlayer);
            }

            gameInfo.append("\n");

            System.out.println(gameInfo);
        }
    }

    private void intro(){
        clearScreen();
        System.out.println("\nWelcome " + username + "!");
        System.out.println("Please select a game you would like to join or create a new game");
    }

    // loads all the games the are currently on the database
    private void loadGames(String authToken)throws InvalidRequestException{
        var allGames = server.listGame(authToken);

        for (Game oneGame : allGames.games()){
            gameList.put(oneGame.getGameName(),oneGame);
        }
    }

    // helper functions that help tell the user what they need to do to navigate this website

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
        System.out.println("\t - join (join that game with that ID)");
        System.out.println("\t - list games (lists all possible games to can join)");
        System.out.println("\t - new game (create a new game)");
        System.out.println("\t - logout (logout of your account)\n");
    }

    private boolean findColor(String color){
        if(color.equalsIgnoreCase("black")){
            return false;
        }else{
            return true;
        }
    }
}
