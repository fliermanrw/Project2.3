package model.games.othello;

import model.games.GameModel;

import java.util.ArrayList;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel implements GameModel {

    public static void main(String[] args) {
        new othelloGameModel();
    }

    othelloBoard othello;
    public othelloGameModel(){
            // Make a new board and assign your colour.
            othello = new othelloBoard('B');
            System.out.println();
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return null;
    }

    @Override
    public void switchPlayer() {

    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        return null;
    }

    @Override
    public void move(int move) {
            int y = move%8;
            int x = 0;
            for(int j = 0; j<move+1; j++){
                if(j%8 == 0){
                    x++;
                }
            }
            System.out.println(x-1 + ","+ y);
            othello.logic.applyMove(othello.getShit(), othello.getCellOnBoard(x-1, y), 'B');
            othello.printBoard();
    }

    @Override
    public void initGrid() {

    }
}
