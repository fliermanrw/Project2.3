package othello;

import java.util.*;

/**
 * Created by Cyriel on 2-4-2017.
 */
public class othelloLogic {

    private boardCell[][] reversiBoard;
    private final int size;

    othelloLogic(boardCell[][] board, int size) {
        reversiBoard = board;
        this.size = size;
    }

    private boardCell getBoardCell(int row, int col) {
        return reversiBoard[row][col];
    }

    private boolean inBounds(int row, int col) {
        return (row >= 0) && (row < size) && (col >= 0) && (col < size);
    }

    public HashSet<String> fetchValidMoves(ArrayList<boardCell> cellsOnBoard, char turn) {
        HashSet<String> listOfValidMoves = new HashSet<>();
        for (boardCell b : cellsOnBoard) {
            if (b.getCharacterInCell() == turn) {
                for (boardCell cell : getMoves(b, turn)) {
                    listOfValidMoves.add(cell.getRow() + "," + cell.getCol());
                }
            }
        }
        return listOfValidMoves;
    }

    public boardCell[][] applyMove(boardCell[][] board, boardCell b, char turn) {
        reversiBoard = board;

        ArrayList<boardCell> templist = new ArrayList<>();
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

        //up
        for (int i = 1; i < size; i++) {
            if (inBounds(row - i, col)) {
                if (getBoardCell(row - i, col).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col));
                } else if (getBoardCell(row -i, col).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }

        //down
        for (int i = 1; i < size; i++) {
            if (inBounds(row + i, col)) {
                if (getBoardCell(row + i, col).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col));
                } else if (getBoardCell(row + i, col ).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            }else {
                templist.clear();
                break;
            }
        }

        //right
        for (int i = 1; i < size; i++) {
            if (inBounds(row, col + i)) {
                if (getBoardCell(row, col + i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row, col + i));
                } else if (getBoardCell(row, col + i).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row, col + i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }

        //left
        for (int i = 1; i < size; i++) {
            if (inBounds(row, col - i)) {
                if (getBoardCell(row, col - i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row, col - i));
                } else if (getBoardCell(row, col - i).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row, col - i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }


        //upleft
        for (int i = 1; i < size; i++) {
            if (inBounds(row - i, col - i)) {
                if (getBoardCell(row - i, col - i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col - i));
                } else if (getBoardCell(row - i, col - i).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col - i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }

        //upright
        for (int i = 1; i < size; i++) {
            if (inBounds(row - i, col + i)) {
                if (getBoardCell(row - i, col + i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col + i));
                } else if (getBoardCell(row - i, col + i).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col + i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }

        //downleft
        for (int i = 1; i < size; i++) {
            if (inBounds(row + i, col - i)) {
                if (getBoardCell(row + i, col - i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col - i));
                } else if (getBoardCell(row + i, col - i).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col - i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
            }
        }

        //down right
        for (int i = 1; i < size; i++) {
            if (inBounds(row + i, col + i)) {
                if (getBoardCell(row + i, col + i).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col + i));
                } else if (getBoardCell(row + i, col + i).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col + i).getCharacterInCell() == turn) {
                    cellsToFlip.addAll(templist);
                    templist.clear();
                    break;
                }
            } else {
                templist.clear();
                break;
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


    private static boolean passedOther = false;

    private boolean recursiveMoveCheck(int row, int col, char otherCell) {
        if (!inBounds(row, col)) {
            return false;
        } else if (getBoardCell(row, col).getCharacterInCell() == otherCell) {
            passedOther = true;
        } else if (getBoardCell(row, col).getCharacterInCell() == '#' && passedOther) {
            passedOther = false;
            return true;
        }
        return false;
    }

    private boolean sameCell(int row, int col, char turn) {
        if (!inBounds(row, col)) {
            passedOther = false;
            return false;
        } else if (getBoardCell(row, col).getCharacterInCell() == turn) {
            passedOther = false;
            return true;
        }
        return false;
    }

    private boolean isDeadCell(int row, int col) {
        if (inBounds(row, col)) {
            return getBoardCell(row, col).getCharacterInCell() == '#';
        }
        return false;
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

        //up
        for (int i = 1; i < size; i++) {
            if (sameCell(row - i, col, turn)) {
                break;
            } else if (recursiveMoveCheck(row - i, col, otherCell)) {
                PossibleMoves.add(getBoardCell(row - i, col));
                break;
            } else if (isDeadCell(row - i, col)) {
                break;
            }
        }


        //down
        for (int i = 1; i < size; i++) {
            if (sameCell(row + i, col, turn)) {
                break;
            } else if (recursiveMoveCheck(row + i, col, otherCell)) {
                PossibleMoves.add(getBoardCell(row + i, col));
                break;
            } else if (isDeadCell(row + i, col)) {
                break;
            }
        }


        //right
        for (int i = 1; i < size; i++) {
            if (sameCell(row, col + i, turn)) {
                break;
            } else if (recursiveMoveCheck(row, col + i, otherCell)) {
                PossibleMoves.add(getBoardCell(row, col + i));
                break;
            } else if (isDeadCell(row, col + i)) {
                break;
            }
        }


        //left
        for (int i = 1; i < size; i++) {
            if (sameCell(row, col - i, turn)) {
                break;
            } else if (recursiveMoveCheck(row, col - i, otherCell)) {
                PossibleMoves.add(getBoardCell(row, col - i));
                break;
            } else if (isDeadCell(row, col - i)) {
                break;
            }
        }

        //up left
        for (int i = 1; i < size; i++) {
            if (sameCell(row - i, col - i, turn)) {
                break;
            } else if (recursiveMoveCheck(row - i, col - i, otherCell)) {
                PossibleMoves.add(getBoardCell(row - i, col - i));
                break;
            } else if (isDeadCell(row - i, col - i)) {
                break;
            }
        }

        //up right
        for (int i = 1; i < size; i++) {
            if (sameCell(row - i, col + i, turn)) {
                break;
            } else if (recursiveMoveCheck(row - i, col + i, otherCell)) {
                PossibleMoves.add(getBoardCell(row - i, col + i));
                break;
            } else if (isDeadCell(row - i, col + i)) {
                break;
            }
        }

        //down left
        for (int i = 1; i < size; i++) {
            if (sameCell(row + i, col - i, turn)) {
                break;
            } else if (recursiveMoveCheck(row + i, col - i, otherCell)) {
                PossibleMoves.add(getBoardCell(row + i, col - i));
                break;
            } else if (isDeadCell(row + i, col - i)) {
                break;
            }
        }

        //down right
        for (int i = 1; i < size; i++) {
            if (sameCell(row + i, col + i, turn)) {
                break;
            } else if (recursiveMoveCheck(row + i, col + i, otherCell)) {
                PossibleMoves.add(getBoardCell(row + i, col + i));
                break;
            } else if (isDeadCell(row + i, col + i)) {
                break;
            }
        }
        return PossibleMoves;
    }
}