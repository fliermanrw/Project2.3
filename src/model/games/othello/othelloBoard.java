package model.games.othello;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Cyriel on 30-3-2017.
 */

public class othelloBoard {
    private static int size = 8;
    private char turn;

    private boardCell[][] reversiBoard = new boardCell[size][size];
    private static ArrayList<boardCell> cellsOnBoard = new ArrayList<>();
    private othelloLogic logic = new othelloLogic(reversiBoard, size);

    public othelloBoard(char turn) {
        this.turn = turn;
        // generate a new board as soon as the object is created.
        generateNewBoard();

        // Start the recursive move function.
        recursiveMove();
    }

    public String countPoints() {
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
        if (whitePoints > blackPoints){
            return "White";
        } else if (blackPoints > whitePoints){
            return "Black";
        }
        return "Tie";
    }

    public void recursiveMove() {
        System.out.println("Turn : " + turn);
        refreshBoardList();
        System.out.print("Available moves: ");
        int numberOfMoves = 0;
        for (String a : logic.fetchValidMoves(cellsOnBoard, turn)) {
            System.out.print("(" + a + ")");
            numberOfMoves++;
        }
        System.out.print("\n");
        System.out.print("Number of moves : " + numberOfMoves);
        if (numberOfMoves == 0){
            System.out.println("No more moves for : " + turn);
            System.out.println("Winner is : " + countPoints());
        } else {
            askForNewMove();
        }
    }

    private void askForNewMove(){
        System.out.print("\n");
        System.out.print("Enter your move (Format : row,col): ");
        Scanner scanner1 = new Scanner(System.in);
        String input = scanner1.nextLine();
        if (input.length() != 3){
            System.out.println("Move was invalid.");
            recursiveMove();
        }
        int row = Character.getNumericValue((input.charAt(0)));
        int col = Character.getNumericValue((input.charAt(2)));
        String move = row + "," + col;
        if (logic.fetchValidMoves(cellsOnBoard, turn).contains(move)){
            // valid move
            reversiBoard = logic.applyMove(reversiBoard, getCellOnBoard(row, col), turn);
            swapTurn();
        } else {
            System.out.println("Move was invalid.");
            recursiveMove();
        }
    }

    private void refreshBoardList() {
        cellsOnBoard.clear();
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                cellsOnBoard.add(new boardCell(i, b, getCellOnBoard(i, b).getCharacterInCell()));
            }
        }
    }

    private boardCell getCellOnBoard(int row, int col) {
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
        printBoard();
        recursiveMove();
    }

    public void printBoard() {
        for (boardCell[] a : reversiBoard) {
            for (boardCell cell : a) {
                System.out.print(cell.getCharacterInCell() + "  ");
            }
            System.out.print("\n");
        }
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
        printBoard();
    }
}