package model.games;

import java.util.ArrayList;

/**
 * Created by jouke on 30-3-2017.
 * Interface for the game model
 *
 */
public interface GameModel {
    boolean hasWon(ArrayList<String> grid, String player);

    String getCurrentPlayer();

    void switchPlayer();

    ArrayList<Integer> getValidMoves();

    void move(int move);

    void initGrid();
}
