package model.games.othello;

import model.games.GameModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel implements GameModel {

    othelloBoard othello;
    char turnForBot;

    public othelloGameModel(char turnForBot) {
        this.turnForBot = turnForBot;
        // Make a new board and assign the colour for the bot.
        // 'B' if bot plays first 'W' if bot plays second
        othello = new othelloBoard(turnForBot);
        initGrid();
        for (int a : getValidMoves()) {
            System.out.println(a);
        }
        System.out.println();
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return Character.toString(othello.turn);
    }

    @Override
    public void switchPlayer() {
        othello.swapTurn();
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
        for (boardCell cell : othelloBoard.logic.fetchValidMovesAsCell(othelloBoard.cellsOnBoard, othello.turn)) {

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
        othello.logic.applyMove(othello.getShit(), othello.getCellOnBoard(x - 1, y), othello.turn);
        othello.printBoard();
    }

    @Override
    public void initGrid() {
        othello.generateNewBoard();
        othello.refreshBoardList();
    }

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }
}
