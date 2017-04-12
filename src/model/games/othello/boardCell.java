package model.games.othello;

/**
 * Created by Cyriel on 21-3-2017.
 */
public class boardCell {

    private final int row;
    private final int col;
    private char characterInCell;

    boardCell(int row, int col, char characterInCell) {
        this.row = row;
        this.col = col;
        this.characterInCell = characterInCell;
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

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }

    @Override
    public String toString() {
        String result = "row: [" + this.row + "] col: [" + this.col + "] value: " + this.characterInCell;
        return result;
    }
}