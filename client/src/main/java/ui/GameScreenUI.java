package ui;

import WebSocketClient.WebSocketFacade;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;
import static ui.EscapeSequences.*;


public class GameScreenUI extends ChessUI{

    private boolean isPlayer;
    private boolean isWhite;
    private int gameId;
    private ChessBoard cpyBoard;

    private boolean runtimeUser = true;
    private String authorization;
    private WebSocketFacade server;

    // Board values for coloring
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = "   ";
    private static final int FLIP_VAL = 7;
    private static final int INT_CONVERTER = 96;


    public GameScreenUI(String myAuthorization,WebSocketFacade myServer,boolean myIsWhite, int myGameId,boolean player){

        isPlayer = player;
        isWhite = myIsWhite;
        gameId = myGameId;
        server = myServer;
        authorization = myAuthorization;
    }

    public void setFacade(WebSocketFacade server){
        this.server = server;
    }


    public void run(){

        if(isPlayer){
            server.joinGame(authorization,gameId,isWhite);
        }else{
            server.observeGame(authorization,gameId);
        }

        String input;

        do{
            // gets simple input from the user
            printPrompt();
            input = userInput.nextLine();
            input = input.toLowerCase();

            switch(input){

                case "move":
                    move();
                    break;
                case "hints":
                    hints();
                    break;
                case "leave":
                    server.leave(authorization,gameId);
                    runtimeUser = false;
                    break;

                case "resign":
                    server.resign(authorization,gameId);
                    break;
                default:
                    break;

            }
        }while(runtimeUser);
    }

    public void drawBoard(ChessBoard chessboard){

        try {
            sleep(1000);
        }catch(Exception ignored){}

        // just gives it a copy of the board
        cpyBoard = chessboard;

        clearScreen();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN); // clears screen for me to start using


        drawChessBoard(isWhite,chessboard,out);
        drawBottomHeaders(isWhite,out);

        setBackNormal();
    }

    public void print(String message){
        System.out.println(message);
    }

    private void move(){
        System.out.println("Moving from...");

        System.out.print("enter letter: ");
        String strLetter = userInput.nextLine();
        System.out.println(); // just makes a new line

        /// checks to see if the user even entered anything at all
        if(strLetter.isEmpty()  || strLetter.isBlank()){
            System.out.println("Invalid entry");
            return;
        }

        char letter = strLetter.charAt(0);

        System.out.print("enter number: ");
        String strNumber = userInput.nextLine();
        System.out.println();

        if(!isNumber(strNumber)){
            System.out.println("Invalid entry");
            return;
        }
        int row = Integer.parseInt(strNumber);
        int col;

        try {
            col = convertChar(letter);
        }catch(IndexOutOfBoundsException error){
            return;
        }

        ChessPosition start = new ChessPosition(row,col);

        System.out.println("Moving To...");

        System.out.print("enter letter: ");
        strLetter = userInput.nextLine();
        System.out.println(); // just makes a new line

        /// checks to see if the user even entered anything at all
        if(strLetter.isEmpty()  || strLetter.isBlank()){
            System.out.println("Invalid entry");
            return;
        }

        letter = strLetter.charAt(0);

        System.out.print("enter number: ");
        strNumber = userInput.nextLine();
        System.out.println();

        if(!isNumber(strNumber)){
            System.out.println("Invalid entry");
            return;
        }

        try {
            col = convertChar(letter);
        }catch(IndexOutOfBoundsException error){
            return;
        }

        row = Integer.parseInt(strNumber);

        ChessPosition end = new ChessPosition(row,col);
        try {
            var userMove = finalizeMove(start, end);
            server.makeMove(authorization,userMove,gameId);
        }catch(InvalidMoveException error){
            System.out.println(error.getMessage());
        }
    }

    private ChessMove finalizeMove(ChessPosition start, ChessPosition end) throws InvalidMoveException{

        ChessPiece.PieceType promotion = null;

        if(cpyBoard.getPiece(start) == null){
            throw new InvalidMoveException("Not a piece");
        }

        if(cpyBoard.getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN && isWhite){
            if(end.getRow() == 8){
                System.out.println("Promotion! which piece?");
                String strPiece = userInput.nextLine();
                switch(strPiece.toUpperCase()){
                    case "QUEEN" -> promotion = ChessPiece.PieceType.QUEEN;
                    case "ROOK" -> promotion = ChessPiece.PieceType.ROOK;
                    case "BISHOP" -> promotion = ChessPiece.PieceType.BISHOP;
                    case "KNIGHT" -> promotion = ChessPiece.PieceType.KNIGHT;
                    default -> throw new InvalidMoveException("Not a Piece type");
                }
            }
        }

        if(cpyBoard.getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN && !isWhite){
            if(end.getRow() == 1){
                System.out.println("Promotion! which piece?");
                String strPiece = userInput.nextLine();
                switch(strPiece.toUpperCase()){
                    case "QUEEN" -> promotion = ChessPiece.PieceType.QUEEN;
                    case "ROOK" -> promotion = ChessPiece.PieceType.ROOK;
                    case "BISHOP" -> promotion = ChessPiece.PieceType.BISHOP;
                    case "KNIGHT" -> promotion = ChessPiece.PieceType.KNIGHT;
                    default -> throw new InvalidMoveException("Not a Piece type");
                }
            }
        }

        return new ChessMove(start,end,promotion);
    }

    private boolean isNumber(String str){
        String pattern = "\\d+";
        return str.matches(pattern);
    }

    private int convertChar(char letter) throws IndexOutOfBoundsException{

        if(letter < 'a' || letter > 'h'){
            throw new IndexOutOfBoundsException("out of bounds");
        }

        int val = letter - INT_CONVERTER;

        if (!isWhite) {
            return 9 - val;
        }else{
            return val;
        }

    }

    private void hints(){
        System.out.println("To play chess, type \"move\" and then specify where you want to move");
        System.out.println("You should be prompted on where you want to move to");
    }

    // functions for drawing the board ----------------------------------------------------------------------------

    private static void drawBottomHeaders(boolean isWhite,PrintStream out){
        setWhiteLetters(out);

        char val;
        if(isWhite){
            val = 'a';
        }else{
            val = 'h';
        }

        for(int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
            if(isWhite) {
                out.print("    " + val++ + "    ");
            }else{
                out.print("    " + val-- + "    ");
            }
        }
        out.println();
    }

    private static void drawChessBoard(boolean isWhite,ChessBoard board, PrintStream out){

        for(int row = 0; row < BOARD_SIZE_IN_SQUARES; row++){


            int offset = row % 2;

            int index;
            if(isWhite){
                index = BOARD_SIZE_IN_SQUARES - row;
            }else{
                index = row + 1;
            }

            drawRowOfSquares(isWhite,offset,board,out,row,index);
        }
    }

    private static void drawRowOfSquares(boolean isWhite,int offset,ChessBoard board, PrintStream out,int boardRow,int index){
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                if(isWhite) {
                    if((boardCol + offset) % 2 == 0){
                        setBlackSquare(out);
                    }else{
                        setWhiteSquare(out);
                    }
                }else{
                    if((boardCol + offset) % 2 == 1){
                        setBlackSquare(out);
                    }else{
                        setWhiteSquare(out);
                    }
                }

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    drawChessPieces(isWhite,board,out,boardRow,boardCol);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setWhiteSquare(out);
                    out.print(EMPTY.repeat(0));
                }

                setScreenBlack(out);
            }

            // prints out the numbers for the indexes
            if(squareRow == SQUARE_SIZE_IN_CHARS / 2){
                setWhiteLetters(out);
                out.print(" " + index);

            }
            out.println();
        }
    }

    private static void drawChessPieces(boolean isWhite, ChessBoard board,PrintStream out,int row, int column){

        out.print(SET_TEXT_COLOR_BLACK);

        ChessPiece piece;
        if(isWhite) {
            piece = board.getBoard()[row][column];
        }else{
            piece = board.getBoard()[FLIP_VAL - row][column];
        }

        // first checks to see if it is null, if it is, prints a null space
        if(piece == ChessBoard.EMPTY){
            out.print(EMPTY);
            return;
        }

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){

            var type = piece.getPieceType();
            switch(type){
                case PAWN -> out.print(WHITE_PAWN);
                case ROOK -> out.print(WHITE_ROOK);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case BISHOP -> out.print(WHITE_BISHOP);
                case KING -> out.print(WHITE_KING);
                case QUEEN -> out.print(WHITE_QUEEN);
                default -> out.print(EMPTY);
            }
        }else{

            var type = piece.getPieceType();
            switch(type){
                case PAWN -> out.print(BLACK_PAWN);
                case ROOK -> out.print(BLACK_ROOK);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                case BISHOP -> out.print(BLACK_BISHOP);
                case KING -> out.print(BLACK_KING);
                case QUEEN -> out.print(BLACK_QUEEN);
                default -> out.print(EMPTY);
            }
        }
    }

    private static void setBackNormal(){
        System.out.print("\u001B[0m");
        System.out.print("\u001B[49m");
    }

    private static void setWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setScreenBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setWhiteLetters(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_WHITE);
    }

}

