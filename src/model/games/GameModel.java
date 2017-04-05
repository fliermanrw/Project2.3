package model.games;

import java.util.ArrayList;

/**
 * Created by jouke on 30-3-2017.
 * Interface for the game model
 * Please implement these functions in Boggle & Tic-tac-toe
 */
public interface GameModel {
    boolean hasWon(ArrayList<String> grid, String player);

    String getCurrentPlayer(); //@todo probably changes the return type

    void switchPlayer();

    ArrayList<Integer> getValidMoves();

    void move(int move);

    void initGrid();
}
