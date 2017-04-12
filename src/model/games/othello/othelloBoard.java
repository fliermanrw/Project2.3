package model.games.othello;

import java.util.*;

/**
 * Created by Cyriel on 30-3-2017.
 */

public class othelloBoard implements Cloneable {
    private static int size = 8;
    public char turn;

    private static boardCell[][] reversiBoard = new boardCell[size][size];
    public static ArrayList<boardCell> cellsOnBoard = new ArrayList<>();
    public static othelloLogic logic = new othelloLogic(reversiBoard, size);

    public othelloBoard(char turn) {
        this.turn = turn;
    }

    public String findCurrentWinner() {
       HashMap<String, Integer> map = getCurrentPoints();
       int whitePoints = map.get("W");
       int blackPoints = map.get("B");

        if (whitePoints > blackPoints) {
            return "W"; // White wins
        } else if (blackPoints > whitePoints) {
            return "B"; // Black wins
        }
        return "T"; // Tie
    }

    public HashMap<String,Integer> getCurrentPoints(){
        int whitePoints = 0;
        int blackPoints = 0;
        for (boardCell[] a : reversiBoard) {
            for (boardCell b : a) {
                switch (b.getCharacterInCell()) {
                    case 'W':
                        whitePoints++;
                        break;
                    case 'B':
                        blackPoints++;
                        break;
                    default:
                        break;
                }
            }
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("W", whitePoints);
        map.put("B", blackPoints);

        return map;
    }

    public void refreshBoardList() {
        cellsOnBoard.clear();
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                cellsOnBoard.add(new boardCell(i, b, getCellOnBoard(i, b).getCharacterInCell()));
            }
        }
    }

    public boardCell getCellOnBoard(int row, int col) {
        for (boardCell b : reversiBoard[row]) {
            if (b.getRow() == row && b.getCol() == col) {
                return b;
            }
        }
        return null;
    }

    public void swapTurn() {
        if (turn == 'B') {
            turn = 'W';
        } else {
            turn = 'B';
        }
        //System.out.println("Current turn is : " + turn);
    }

    public void printBoard() {
        for (boardCell[] a : reversiBoard) {
            for (boardCell cell : a) {
                System.out.print(cell.getCharacterInCell() + "  ");
            }
            System.out.print("\n");
        }
    }

    public char getTurn() {
        return turn;
    }

    public void generateNewBoard() {
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                if (i == 3 && b == 3 || i == 4 && b == 4) {
                    // white
                    reversiBoard[i][b] = new boardCell(i, b, 'W');
                } else if ((i == 3 && b == 4) || (i == 4 && b == 3)) {
                    // black
                    reversiBoard[i][b] = new boardCell(i, b, 'B');
                } else {
                    // empty
                    reversiBoard[i][b] = new boardCell(i, b, '#');
                }
            }
        }
    }

    public boardCell[][] getBoard() {
        return reversiBoard;
    }

    public ArrayList<boardCell> getBoardList() {
        return cellsOnBoard;
    }

    @Override
    public othelloBoard clone() {
        try {
            return (othelloBoard)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
