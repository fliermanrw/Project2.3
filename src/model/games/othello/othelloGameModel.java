package model.games.othello;

import model.games.GameModel;
import model.games.tictactoe.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel extends othelloLogic implements GameModel {
    private othelloBoard othelloBoard;
    private char turn;

    private int size = 8;

    public othelloGameModel(char turn) {
        this.turn = turn;
        // Make a new board and assign the colour for the bot.
        // 'B' if bot plays first 'W' if bot plays second
    }

    public othelloBoard getOthelloBoard() {
        synchronized (othelloBoard) {
            return othelloBoard;
        }
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return Character.toString(turn);
    }

    @Override
    public void switchPlayer() {
        swapTurn();
    }

    public char getTurn(){
        return turn;
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
        synchronized (othelloBoard){
            for (boardCell cell : fetchValidMovesAsCell(turn, othelloBoard)) {
                listOfMoves.add(rowColToInt(cell.getRow(), cell.getCol()));
            }
            return listOfMoves;
        }
    }

    @Override
    public void move(int move) {
        int y = move % 8;
        int x = 0;
        for (int j = 0; j < move + 1; j++) {
            if (j % 8 == 0) {
                x++;
            }
        }
        synchronized (othelloBoard){
            applyMove(othelloBoard, othelloBoard.getCellOnBoard(x - 1, y), turn);
            othelloBoard.refreshBoardList();
        }
    }

    @Override
    public void initGrid() {
        boardCell[][] newBoard = new boardCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                if (i == 3 && b == 3 || i == 4 && b == 4) {
                    // white
                    newBoard[i][b] = new boardCell(i, b, 'W');
                } else if ((i == 3 && b == 4) || (i == 4 && b == 3)) {
                    // black
                    newBoard[i][b] = new boardCell(i, b, 'B');
                } else {
                    // empty
                    newBoard[i][b] = new boardCell(i, b, '#');
                }
            }
        }
            // Generate a board object.
            othelloBoard = new othelloBoard(newBoard);
        synchronized (othelloBoard){
            // Make a list based on the new board.
            othelloBoard.refreshBoardList();
        }
    }

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }

    public ArrayList<boardCell> convertBoardToArrayListOfCells(boardCell[][] list) {
        ArrayList<boardCell> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int b = 0; b < 8; b++) {
                result.add(new boardCell(i, b, list[i][b].getCharacterInCell()));
            }
        }
        return result;
    }

    public int getMiniMaxMove(othelloBoard board, char turn) {
        System.out.println("board send to minimax : ");
        board.printBoard();
        return new othelloMiniMax(board, turn, 10).calculateBestMove();
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

    public HashMap<String, Integer> getCurrentPoints() {
        int whitePoints = 0;
        int blackPoints = 0;
        synchronized (othelloBoard) {
            for (boardCell[] a : othelloBoard.getBoard()) {
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
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("W", whitePoints);
        map.put("B", blackPoints);

        return map;
    }

    public void swapTurn() {
        if (turn == 'B') {
            turn = 'W';
        } else {
            turn = 'B';
        }
        //System.out.println("Current turn is : " + turn);
    }

    public boardCell[][] getBoard() {
        synchronized (othelloBoard) {
            return othelloBoard.getBoard();
        }
    }

    public ArrayList<boardCell> getBoardAsList() {
        synchronized (othelloBoard) {
            return othelloBoard.getBoardAsList();
        }
    }

}
