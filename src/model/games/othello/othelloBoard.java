package model.games.othello;

import java.util.*;

/**
 * Created by Cyriel on 30-3-2017.
 */

public class othelloBoard {
    private boardCell[][] othelloBoard1;
    private ArrayList<boardCell> cellsOnBoard = new ArrayList<>();
    private int size = 8;

    public boardCell[][] getBoard(){
        return othelloBoard1;
    }

    public ArrayList<boardCell> getBoardAsList(){
        synchronized (cellsOnBoard) {
            return cellsOnBoard;
        }
    }

    public void setOthelloBoard(boardCell[][] othelloBoard) {
        othelloBoard1 = othelloBoard;
        refreshBoardList();
    }

    public void refreshBoardList() {
        synchronized (cellsOnBoard) {
            cellsOnBoard.clear();
            for (int i = 0; i < size; i++) {
                for (int b = 0; b < size; b++) {
                    cellsOnBoard.add(new boardCell(i, b, getCellOnBoard(i, b).getCharacterInCell()));
                }
            }
        }
    }



    public void printBoard() {
        for (boardCell[] a : othelloBoard1) {
            for (boardCell cell : a) {
                System.out.print(cell.getCharacterInCell() + "  ");
            }
            System.out.print("\n");
        }
    }

    public boardCell getCellOnBoard(int row, int col) {
        for (boardCell b : othelloBoard1[row]) {
            if (b.getRow() == row && b.getCol() == col) {
                return b;
            }
        }
        return null;
    }
}
