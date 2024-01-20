package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int myRow;
    private int myCol;

    public ChessPosition(int row, int col) {
        myRow = row;
        myCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return myRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return myCol;
    }
    // Setter methods
    public void setRow(int newRow){
        myRow = newRow;
    }
    public void setCol(int newCol){
        myCol = newCol;
    }

// methods for simple movements. does move and returns weather that move is within the gameboard limits

    public boolean up(){
        myRow--;
        return checkBounds();
    }
    public boolean down(){
        myRow++;
        return checkBounds();
    }
    public boolean right(){
        myCol++;
        return checkBounds();
    }
    public boolean left(){
        myCol--;
        return checkBounds();
    }
    public boolean upperRight(){
        myRow--;
        myCol++;
        return checkBounds();
    }
    public boolean upperLeft(){
        myRow--;
        myCol--;
        return checkBounds();
    }
    public boolean lowerRight(){
        myRow++;
        myCol++;
        return checkBounds();
    }
    public boolean lowerLeft(){
        myRow++;
        myCol--;
        return checkBounds();
    }
    // checks boundries, returns true if is within boundries and false if is out
    private boolean checkBounds(){
        return !(((myRow > 8)||(myRow < 1)) || ((myCol > 8) || (myCol < 1)));
    }

    // this object us used to make a copy of a piece passed in
    public ChessPosition deepCopy(){
        return new ChessPosition(myRow,myCol);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return myRow == that.myRow && myCol == that.myCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myRow, myCol);
    }
}
