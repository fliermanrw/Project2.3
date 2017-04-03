package model;

import java.util.ArrayList;

/**
 * Created by jouke on 3-4-2017.
 */
public class Tictactoe implements GameModel {
    ArrayList<String> grid;
    int gridsize;
    String player = "O";

    public Tictactoe(){
        gridsize = 9; //3*3
        initGrid();
    }

    /**
     * A check if the current player (person or AI) has won with the current state of the board.
     *
     * @param grid the gameboard
     * @param player
     * @return boolean
     */
    public boolean hasWon(ArrayList<String> grid, String player) {
        if ((grid.get(0) == player && grid.get(1) == player && grid.get(2) == player) ||
                (grid.get(3) == player && grid.get(4) == player && grid.get(5) == player) ||
                (grid.get(6) == player && grid.get(7) == player && grid.get(8) == player) ||
                (grid.get(0) == player && grid.get(3) == player && grid.get(6) == player) ||
                (grid.get(1) == player && grid.get(4) == player && grid.get(7) == player) ||
                (grid.get(2) == player && grid.get(5) == player && grid.get(8) == player) ||
                (grid.get(0) == player && grid.get(4) == player && grid.get(8) == player) ||
                (grid.get(2) == player && grid.get(4) == player && grid.get(6) == player)) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getGrid(){
        return grid;
    }

    @Override
    public String getCurrentPlayer() {
        return player;
    }

    @Override
    public void switchPlayer() {
        if(player.equals("O")){
            player = "X";
        }else{
            player = "O";
        }
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> validMoves = new ArrayList<>();

        for(int i=0; i<gridsize; i++){
            if(grid.get(i).equals(".")){
                validMoves.add(i);
            }
        }
        return validMoves;
    }

    @Override
    public void move(int move) {
        grid.set(move, getCurrentPlayer());
        switchPlayer();
    }

    @Override
    public void initGrid() {
        grid = new ArrayList();

        for(int cell=0; cell<gridsize; cell++){
          grid.add(".");
        }
    }

    public void printGrid(){
        for(int i=0; i<gridsize; i++){
            if (i % 3 != 0) {
                System.out.print(" " + grid.get(i) + " ");
            } else {
                System.out.println();
                System.out.print(" " + grid.get(i) + " ");
            }
        }
    }
}
