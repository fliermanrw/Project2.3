package model.games.othello;

import java.util.*;

/**
 * Created by Cyriel on 30-3-2017.
 */

public class othelloBoard {
    boardCell[][] othelloBoard;
    public ArrayList<boardCell> cellsOnBoard = new ArrayList<>();
    int size = 8;

    public othelloBoard(boardCell[][] board) {
        othelloBoard = board;
        refreshBoardList();
    }

    public boardCell[][] getBoard(){
        return othelloBoard;
    }

    public ArrayList<boardCell> getBoardAsList(){
        return cellsOnBoard;
    }

    public void setOthelloBoard(boardCell[][] othelloBoard) {
        this.othelloBoard = othelloBoard;
    }

    public void refreshBoardList() {
        cellsOnBoard.clear();
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                cellsOnBoard.add(new boardCell(i, b, getCellOnBoard(i, b).getCharacterInCell()));
            }
        }
    }

    public void printBoard() {
        for (boardCell[] a : othelloBoard) {
            for (boardCell cell : a) {
                System.out.print(cell.getCharacterInCell() + "  ");
            }
            System.out.print("\n");
        }
    }

    public boardCell getCellOnBoard(int row, int col) {
        for (boardCell b : othelloBoard[row]) {
            if (b.getRow() == row && b.getCol() == col) {
                return b;
            }
        }
        return null;
    }
}
