package model.games.othello;

import java.util.*;

/**
 * Created by Cyriel on 2-4-2017.
 */
public class othelloLogic {

    private boardCell getBoardCell(int row, int col, ArrayList<boardCell> board) {
        int index = row * 8 + col;
        return board.get(index);
    }

    private boolean inBounds(int row, int col) {
        return (row >= 0) && (row < 8) && (col >= 0) && (col < 8);
    }

    public ArrayList<boardCell> fetchValidMovesAsCell(char turn, ArrayList<boardCell> board) {
        ArrayList<boardCell> listOfValidMoves = new ArrayList<>();
        for (boardCell b : board) {
            if (b.getCharacterInCell() == turn) {
                for (boardCell cell : getMoves(b, turn, board)) {
                    if (!listOfValidMoves.contains(cell)) {
                        listOfValidMoves.add(cell);
                    }
                }
            }
        }
        return listOfValidMoves;
    }

    public void applyMove(ArrayList<boardCell> board, boardCell b, char turn) {
        ArrayList<boardCell> templist = new ArrayList<>();
        ArrayList<boardCell> cellsToFlip = new ArrayList();

        char otherCell;
        if (turn == 'B') {
            otherCell = 'W';
        } else {
            otherCell = 'B';
        }

        // Apply turn
        board.get(b.getRow() * 8 + b.getCol()).setCharacterInCell(turn);


        int row = b.getRow();
        int col = b.getCol();

        //up
        for (int i = 1; i < 8; i++) {
            if (inBounds(row - i, col)) {
                if (getBoardCell(row - i, col, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col, board));
                } else if (getBoardCell(row -i, col, board).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row + i, col)) {
                if (getBoardCell(row + i, col, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col, board));
                } else if (getBoardCell(row + i, col , board).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row, col + i)) {
                if (getBoardCell(row, col + i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row, col + i, board));
                } else if (getBoardCell(row, col + i, board).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row, col + i, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row, col - i)) {
                if (getBoardCell(row, col - i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row, col - i, board));
                } else if (getBoardCell(row, col - i, board).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row, col - i, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row - i, col - i)) {
                if (getBoardCell(row - i, col - i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col - i, board));
                } else if (getBoardCell(row - i, col - i, board).getCharacterInCell() == '#') {
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col - i, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row - i, col + i)) {
                if (getBoardCell(row - i, col + i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row - i, col + i, board));
                } else if (getBoardCell(row - i, col + i, board).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row - i, col + i, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row + i, col - i)) {
                if (getBoardCell(row + i, col - i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col - i, board));
                } else if (getBoardCell(row + i, col - i, board).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col - i, board).getCharacterInCell() == turn) {
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
        for (int i = 1; i < 8; i++) {
            if (inBounds(row + i, col + i)) {
                if (getBoardCell(row + i, col + i, board).getCharacterInCell() == otherCell) {
                    templist.add(getBoardCell(row + i, col + i, board));
                } else if (getBoardCell(row + i, col + i, board).getCharacterInCell() == '#'){
                    templist.clear();
                    break;
                } else if (getBoardCell(row + i, col + i, board).getCharacterInCell() == turn) {
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
            if (board.get(cell.getRow() * 8 + cell.getCol()).getCharacterInCell() == 'B') {
                board.get(cell.getRow() * 8 + cell.getCol()).setCharacterInCell('W');
            } else {
                board.get(cell.getRow() * 8 + cell.getCol()).setCharacterInCell('B');
            }
        }
    }

    public void printArraylistBoard(ArrayList<boardCell> board) {
        for (int row = 0; row < 8 ;row++) {
            for (int col = 0; col < 8 ;col++) {
                System.out.print(board.get((row * 8 + col)).getCharacterInCell() + "  ");
            }
            System.out.print("\n");
        }
    }

    private boolean passedOther = false;

    private boolean recursiveMoveCheck(int row, int col, char otherCell, ArrayList<boardCell> board) {
        if (!inBounds(row, col)) {
            return false;
        } else if (getBoardCell(row, col, board).getCharacterInCell() == otherCell) {
            passedOther = true;
        } else if (getBoardCell(row, col, board).getCharacterInCell() == '#' && passedOther) {
            passedOther = false;
            return true;
        }
        return false;
    }

    private boolean sameCell(int row, int col, char turn, ArrayList<boardCell> board) {
        if (!inBounds(row, col)) {
            passedOther = false;
            return false;
        } else if (getBoardCell(row, col, board).getCharacterInCell() == turn) {
            passedOther = false;
            return true;
        }
        return false;
    }

    private boolean isDeadCell(int row, int col, ArrayList<boardCell> board) {
        if (inBounds(row, col)) {
            return getBoardCell(row, col, board).getCharacterInCell() == '#';
        }
        return false;
    }

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }

    public ArrayList<boardCell> getMoves(boardCell root, char turn, ArrayList<boardCell> board) {
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
        for (int i = 1; i < 8; i++) {
            if (sameCell(row - i, col, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row - i, col, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row - i, col, board));
                break;
            } else if (isDeadCell(row - i, col, board)) {
                break;
            }
        }


        //down
        for (int i = 1; i < 8; i++) {
            if (sameCell(row + i, col, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row + i, col, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row + i, col, board));
                break;
            } else if (isDeadCell(row + i, col, board)) {
                break;
            }
        }


        //right
        for (int i = 1; i < 8; i++) {
            if (sameCell(row, col + i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row, col + i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row, col + i, board));
                break;
            } else if (isDeadCell(row, col + i, board)) {
                break;
            }
        }


        //left
        for (int i = 1; i < 8; i++) {
            if (sameCell(row, col - i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row, col - i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row, col - i, board));
                break;
            } else if (isDeadCell(row, col - i, board)) {
                break;
            }
        }

        //up left
        for (int i = 1; i < 8; i++) {
            if (sameCell(row - i, col - i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row - i, col - i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row - i, col - i, board));
                break;
            } else if (isDeadCell(row - i, col - i, board)) {
                break;
            }
        }

        //up right
        for (int i = 1; i < 8; i++) {
            if (sameCell(row - i, col + i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row - i, col + i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row - i, col + i, board));
                break;
            } else if (isDeadCell(row - i, col + i, board)) {
                break;
            }
        }

        //down left
        for (int i = 1; i < 8; i++) {
            if (sameCell(row + i, col - i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row + i, col - i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row + i, col - i, board));
                break;
            } else if (isDeadCell(row + i, col - i, board)) {
                break;
            }
        }

        //down right
        for (int i = 1; i < 8; i++) {
            if (sameCell(row + i, col + i, turn, board)) {
                break;
            } else if (recursiveMoveCheck(row + i, col + i, otherCell, board)) {
                PossibleMoves.add(getBoardCell(row + i, col + i, board));
                break;
            } else if (isDeadCell(row + i, col + i, board)) {
                break;
            }
        }
        return PossibleMoves;
    }
}