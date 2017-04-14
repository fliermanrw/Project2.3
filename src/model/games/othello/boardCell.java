package model.games.othello;

/**
 * Created by Cyriel on 21-3-2017.
 */
public class boardCell implements Cloneable{

    private final int row;
    private final int col;
    private char characterInCell;

    boardCell(int row, int col, char characterInCell) {
        this.row = row;
        this.col = col;
        this.characterInCell = characterInCell;
    }

    protected Object clone() throws CloneNotSupportedException {
        boardCell clone=(boardCell)super.clone();
        return clone;

    }

    public void setCharacterInCell (char c){
        characterInCell = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getCharacterInCell() {
        return characterInCell;
    }

    public int getIndexForCell() {
        int index = (row * 8) + col;
        return index;
    }

    @Override
    public String toString() {
        String result = "row: [" + this.row + "] col: [" + this.col + "] value: " + this.characterInCell;
        return result;
    }
}