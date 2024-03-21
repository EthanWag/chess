package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import models.Game;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.System.exit;
import static ui.EscapeSequences.*;


public class GameScreenUI extends ChessUI{

    private boolean runtimeUser = true;
    private String authorization;
    private Game game;
    private boolean isWhite;

    // Board values for coloring
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = "   ";
    private static final int FLIP_VAL = 7;



    public GameScreenUI(Game myGame,boolean myIsWhite,String myAuthorization){
        game = myGame;
        isWhite = myIsWhite;
        authorization = myAuthorization;
    }


    public void run(){

        clearScreen();
        drawBoard(isWhite,game.getGame().getBoard());
        setBackNormal();

        String input;

        do{
            // gets simple input from the user
            printPrompt();
            input = userInput.nextLine();
            input = input.toLowerCase();

            switch(input){

                case "move":

                    break;
                case "hints":

                    break;
                case "quit":
                    runtimeUser = false;
                    break;
                default:
                    break;

            }
        }while(runtimeUser);
    }

    private static void drawBoard(boolean isWhite, ChessBoard chessboard){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN); // clears screen for me to start using


        drawChessBoard(isWhite,chessboard,out);
        drawBottomHeaders(isWhite,out);


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



    public static void main(String[] args){

        Game newGame = new Game(-1,"user1","user2","game",false,false);

        GameScreenUI newScreen = new GameScreenUI(newGame,false,"123");
        newScreen.run();
    }

}

