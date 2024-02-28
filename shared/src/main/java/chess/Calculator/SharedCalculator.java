package chess.Calculator;

import chess.*;

import java.util.ArrayList;

public abstract class SharedCalculator {


    protected static ArrayList<ChessMove> allowDiagonal(ChessBoard board, ChessGame.TeamColor color, ChessPosition position){

        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        // checks all the different of moves it can make, given the position
        while(copyPos.upperRight()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.lowerRight()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.lowerLeft()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.upperLeft()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }

        return posToMove(newPos,position);
    }


    protected static ArrayList<ChessMove> allowStright(ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
        ;
        ArrayList<ChessPosition> newPos = new ArrayList<>();
        ChessPosition copyPos = position.deepCopy();

        while(copyPos.up()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.right()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.down()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }
        copyPos = position.deepCopy();

        while(copyPos.left()){
            if(finishedPos(board,color,newPos,copyPos)){
                break;
            }
        }

        return posToMove(newPos,position);
    }


    // given a board, a position, and a ArrayList checks to make sure that it is a valid move
    protected static boolean finishedPos(ChessBoard board, ChessGame.TeamColor color, ArrayList<ChessPosition> positionList,
                                   ChessPosition position){

        ChessPiece temp = board.getPiece(position);

        // can it keep moving or should it stop at a given point
        if(temp != ChessBoard.EMPTY){
            if(temp.getTeamColor() != color){
                positionList.add(position.deepCopy());
                return true;
            }
            return true;
        }else{
            positionList.add(position.deepCopy());
            return false;
        }
    }

    // NOTE: Do not use this for pawns! does not consider promotion types
    protected static ArrayList<ChessMove> posToMove(ArrayList<ChessPosition> posList, ChessPosition startPos){

        // creates the new list of ChessMoves
        ArrayList<ChessMove> newMoves = new ArrayList<>();

        // goes through the different, positions you are given and makes a list of ChessMoves
        for(ChessPosition pos : posList){
            ChessMove newMove = new ChessMove(startPos,pos,null);
            newMoves.add(newMove);
        }
        return newMoves;
    }
}
