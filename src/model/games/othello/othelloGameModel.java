package model.games.othello;

import model.games.GameModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel implements GameModel {

    public othelloBoard othelloBoard;
    char turn;

    public othelloGameModel(char turn) {
        this.turn = turn;
        // Make a new board and assign the colour for the bot.
        // 'B' if bot plays first 'W' if bot plays second
        othelloBoard = new othelloBoard(turn);
    }

    public othelloBoard getOthelloBoard(){
        return othelloBoard;
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return Character.toString(othelloBoard.turn);
    }

    @Override
    public void switchPlayer() {
        othelloBoard.swapTurn();
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
        for (boardCell cell : othelloBoard.logic.fetchValidMovesAsCell(othelloBoard.cellsOnBoard, othelloBoard.turn)) {
            listOfMoves.add(rowColToInt(cell.getCol(), cell.getRow()));
        }
        System.out.println(listOfMoves);
        return listOfMoves;
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
        othelloBoard.logic.applyMove(othelloBoard.getBoard(), othelloBoard.getCellOnBoard(x - 1, y), othelloBoard.turn);
        othelloBoard.refreshBoardList();
    }

    public String findCurrentWinner(){
        return othelloBoard.findCurrentWinner();
    }

    @Override
    public void initGrid() {
        othelloBoard.generateNewBoard();
        othelloBoard.refreshBoardList();
    }

    public void printBoard(){
        othelloBoard.printBoard();
    }

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }
}
