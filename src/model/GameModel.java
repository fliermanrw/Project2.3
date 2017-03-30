package model;

/**
 * Created by jouke on 30-3-2017.
 * Interface for the game model
 * Please implement these functions in Boggle & Tic-tac-toe
 */
public interface GameModel {
    boolean isFinished();

    void getCurrentPlayer(); //@todo probably changes the return type

    int[][] getValidMoves();

    void move(int y, int x);
}
