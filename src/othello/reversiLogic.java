package othello;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Cyriel on 2-4-2017.
 */
public class reversiLogic {

    boardCell[][] reversiBoard;
    public final int size;

    public reversiLogic(boardCell[][] board, int size) {
        reversiBoard = board;
        this.size = size;
    }

    private boardCell getBoardCell(int row, int col) {
        return reversiBoard[row][col];
    }

    public ArrayList<String> fetchValidMoves(ArrayList<boardCell> cellsOnBoard, char turn) {
        ArrayList<String> listOfValidMoves = new ArrayList<>();
        for (boardCell b : cellsOnBoard) {
            if (b.getCharacterInCell() == turn) {
                ArrayList<boardCell> cells = getMoves(b, turn);
                for (boardCell cell : cells) {
                    listOfValidMoves.add(cell.getRow() + "," + cell.getCol());
                }
//                for (boardCell a : getMoves(b, turn)) {
//                    System.out.print("(" + a.getRow() + "," + a.getCol() + ")");
//                }
            }
        }
        return listOfValidMoves;
    }

    public boardCell[][] applyMove(boardCell[][] board, boardCell b, char turn) {
        reversiBoard = board;
        ArrayList<boardCell> cellsToFlip = new ArrayList();

        char otherCell;
        if (turn == 'B') {
            otherCell = 'W';
        } else {
            otherCell = 'B';
        }

        // Apply turn
        reversiBoard[b.getRow()][b.getCol()].setCharacterInCell(turn);

        int row = b.getRow();
        int col = b.getCol();

        boolean u, d, r, l;
        u = row - 1 >= 0;
        d = row + 1 < size;
        r = col + 1 < size;
        l = col - 1 >= 0;


        ArrayList<boardCell> templist = new ArrayList<>();

        //up
        if (u) {
            int num = 1;
            if (getBoardCell(row - num, col).getCharacterInCell() == otherCell) {
                while (getBoardCell(row - num, col).getCharacterInCell() == otherCell && inBounds(row - num, col)) {
                    templist.add(getBoardCell(row - num, col));
                    num++;
                }
                if (getBoardCell(row - num, col).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }

        //down
        if (d) {
            int num = 1;
            if (getBoardCell(row + num, col).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col).getCharacterInCell() == otherCell && inBounds(row + num, col)) {
                    templist.add(getBoardCell(row + num, col));
                    num++;
                }
                if (getBoardCell(row + num, col).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }

        //right
        if (r) {
            int num = 1;
            if (getBoardCell(row, col + num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row, col + num).getCharacterInCell() == otherCell && inBounds(row, col + num)) {
                    templist.add(getBoardCell(row, col + num));
                    num++;
                }
                if (getBoardCell(row, col + num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }
        //left
        if (l) {
            int num = 1;
            if (getBoardCell(row, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row, col - num).getCharacterInCell() == otherCell && inBounds(row, col - num)) {
                    templist.add(getBoardCell(row, col - num));
                    num++;
                }
                if (getBoardCell(row, col - num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }
        //upleft
        if (u && l) {
            int num = 1;
            if (getBoardCell(row - num, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row - num, col - num).getCharacterInCell() == otherCell && inBounds(row - num, col - num)) {
                    templist.add(getBoardCell(row - num, col - num));
                    num++;
                }
                if (getBoardCell(row - num, col - num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }
        //upright
        if (u && r) {
            int num = 1;
            if (getBoardCell(row - num, col + num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row - num, col + num).getCharacterInCell() == otherCell && inBounds(row - num, col + num)) {
                    templist.add(getBoardCell(row - num, col + num));
                    num++;
                }
                if (getBoardCell(row - num, col + num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }
        //downleft
        if (d && l) {
            int num = 1;
            if (getBoardCell(row + num, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col - num).getCharacterInCell() == otherCell && inBounds(row + num, col - num)) {
                    templist.add(getBoardCell(row + num, col - num));
                    num++;
                }
                if (getBoardCell(row + num, col - num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }
        //down right
        if (d && r) {
            int num = 1;
            if (getBoardCell(row + num, col + num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col + num).getCharacterInCell() == otherCell && inBounds(row + num, col + num)) {
                    templist.add(getBoardCell(row + num, col + num));
                    num++;
                }
                if (getBoardCell(row + num, col + num).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                } else {
                    templist.clear();
                }
            }
        }

        // Flip cells
        for (boardCell cell : cellsToFlip) {
            if (reversiBoard[cell.getRow()][cell.getCol()].getCharacterInCell() == 'B') {
                reversiBoard[cell.getRow()][cell.getCol()].setCharacterInCell('W');
            } else {
                reversiBoard[cell.getRow()][cell.getCol()].setCharacterInCell('B');
            }
        }
        return reversiBoard;
    }


    private boolean inBounds(int row, int col) {
        return (row >= 0) && (row < size) && (col >= 0) && (col < size);
    }

    public ArrayList<boardCell> getMoves(boardCell root, char turn) {
        ArrayList<boardCell> PossibleMoves = new ArrayList<>();
        char otherCell;
        if (turn == 'B') {
            otherCell = 'W';
        } else {
            otherCell = 'B';
        }

        int row = root.getRow();
        int col = root.getCol();

        int num = 1;

        boolean u, d, r, l;
        u = row - num >= 0;
        d = row + num < size;
        r = col + num < size;
        l = col - num >= 0;

        //up
        if (u) {
            num = 1;
            if (getBoardCell(row - num, col).getCharacterInCell() == otherCell) {
                while (getBoardCell(row - num, col).getCharacterInCell() == otherCell && inBounds(row - num, col)) {
                    num++;
                }
                if (getBoardCell(row - num, col).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row - num, col));
                }
            }
            //PossibleMoves.add(getBoardCell(row - 1, col));
        }

        //down
        if (d) {
            num = 1;
            if (getBoardCell(row + num, col).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col).getCharacterInCell() == otherCell && inBounds(row + num, col)) {
                    num++;
                }
                if (getBoardCell(row + num, col).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row + num, col));
                }
            }
            //    PossibleMoves.add(getBoardCell(row + 1, col));
        }

        //right
        if (r) {
            num = 1;
            if (getBoardCell(row, col + num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row, col + num).getCharacterInCell() == otherCell && inBounds(row, col + num)) {
                    num++;
                }
                if (getBoardCell(row, col + num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row, col + num));
                }
            }
//            PossibleMoves.add(getBoardCell(row, col + 1));
        }

        //left
        if (l) {
            num = 1;
            if (getBoardCell(row, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row, col - num).getCharacterInCell() == otherCell && inBounds(row, col - num)) {
                    num++;
                }
                if (getBoardCell(row, col - num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row, col - num));
                }
            }
            //PossibleMoves.add(getBoardCell(row, col - 1));
        }

        //upleft
        if (u && l) {
            num = 1;
            if (getBoardCell(row - num, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row - num, col - num).getCharacterInCell() == otherCell && inBounds(row - num, col + num)) {
                    num++;
                }
                if (getBoardCell(row - num, col - num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row - num, col - num));
                }
            }
            //PossibleMoves.add(getBoardCell(row - 1, col - 1));
        }

        //upright
        if (u && r) {
            num = 1;
            if (getBoardCell(row - num, col + num).getCharacterInCell() == otherCell) {
                while (inBounds(row - num, col + num)) {
                    if (getBoardCell(row - num, col + num).getCharacterInCell() == otherCell) {
                        num++;
                    } else {
                        break;
                    }
                }
                if (getBoardCell(row - num, col + num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row - num, col + num));
                }
            }
            //PossibleMoves.add(getBoardCell(row - 1, col + 1));
        }

        //downleft
        if (d && l) {
            num = 1;
            if (getBoardCell(row + num, col - num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col - num).getCharacterInCell() == otherCell && inBounds(row + num, col - num)) {
                    num++;
                }
                if (getBoardCell(row + num, col - num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row + num, col - num));
                }
            }
            //PossibleMoves.add(getBoardCell(row + 1, col - 1));
        }

        //down right
        if (d && r) {
            num = 1;
            if (getBoardCell(row + num, col + num).getCharacterInCell() == otherCell) {
                while (getBoardCell(row + num, col + num).getCharacterInCell() == otherCell && inBounds(row + num, col + num)) {
                    num++;
                }
                if (getBoardCell(row + num, col + num).getCharacterInCell() == '#') {
                    PossibleMoves.add(getBoardCell(row + num, col + num));
                }
            }
            //PossibleMoves.add(getBoardCell(row + 1, col + 1));
        }
        return PossibleMoves;
    }
}