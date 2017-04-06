package model.games.tictactoe;

import model.games.GameModel;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jouke on 3-4-2017.
 */
public class Tictactoe implements GameModel {

    private ArrayList<String> grid;
    private int gridSize;
    private String humanPlayer = "X";
    private String aiPlayer = "O";
    private String currentPlayer;
    private int depth;

    public Tictactoe() {
        gridSize = 9; //3*3
        initGrid();
        startCommandLine();
    }

    private void startCommandLine() {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("Positie? (0 t/m 8): ");
            int pos = Integer.parseInt(scanner.nextLine());
            printGrid(pos, humanPlayer);

            Move bestMove = minimax(grid, aiPlayer, new Move(0));
            printGrid(bestMove.getIndex(), aiPlayer);
            System.out.println(getValidMoves());
        }
    }

    /**
     * A check if the current player (person or AI) has won with the current state of the board.
     *
     * @param grid the gameboard
     * @param player
     * @return boolean
     */
    @Override
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

    // @todo?
    public ArrayList<String> getGrid(){
        return grid;
    }

    // @todo?
    @Override
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    // @todo?
    @Override
    public void switchPlayer() {
        if (currentPlayer.equals(aiPlayer)) {
            currentPlayer = humanPlayer;
        } else if (currentPlayer.equals(humanPlayer)) {
            currentPlayer = aiPlayer;
        }
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> result = new ArrayList<>();

        // Reuse the emptySpots(ArrayList<String> gb) method
        ArrayList<String> temp = emptySpots(grid);

        for (int i = 0; i < temp.size(); i++) {
            result.add(Integer.parseInt(temp.get(i)));
        }

        return result;
    }

    // @todo?
    @Override
    public void move(int move) {
        grid.set(move, getCurrentPlayer());
        switchPlayer();
    }

    @Override
    public void initGrid() {
        grid = new ArrayList();

        for(int cell = 0; cell < gridSize; cell++){
            grid.add(Integer.toString(cell));
            System.out.println(cell);
        }
    }

    public void printGrid(int pos, String player) {
        grid.remove(pos);
        grid.add(pos, player);
        for (int i = 0; i < gridSize; i++) {
            if (i % 3 != 0) {
                System.out.print(" " + grid.get(i) + " ");
            } else {
                System.out.println();
                System.out.print(" " + grid.get(i) + " ");
            }
        }
        System.out.println();
    }

    /**
     * Return an array with the index and value of empty spots
     *
     * @param gb
     */
    private ArrayList<String> emptySpots(ArrayList<String> gb) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < gb.size(); i++) {
            if (gb.get(i) != "X" && gb.get(i) != "O") {
                result.add(gb.get(i));
            }
        }

        return result;
    }

    private Move minimax(ArrayList<String> newBoard, String player, Move m) {
        // Increment the depth (or function call) by 1
        this.depth++;
        //if (m == null) m = new Move(0);
        // Get the empty spots
        ArrayList<String> availableSpots = emptySpots(newBoard);

        // Check who has won. If the availableSpots are 0, the game is a TIE.
        if (hasWon(newBoard, humanPlayer)) {
            m.setScore(-10);
            return m;
        } else if (hasWon(newBoard, aiPlayer)) {
            m.setScore(10);
            return m;
        } else if (availableSpots.size() == 0) {
            m.setScore(-1);
            return m;
        }

        // Create an ArrayList with Move objects
        ArrayList<Move> moves = new ArrayList<>();

        // Loop over the available spots, skip the null values
        for (int i = 0; i < availableSpots.size(); i++) {

            // Create a new Move object and set the available spot as their index
            Move move = new Move(Integer.parseInt(newBoard.get(Integer.parseInt(availableSpots.get(i)))));

            // Since the moves with index are saved, we can overwrite the spot with the current player
            newBoard.remove(Integer.parseInt(availableSpots.get(i)));
            newBoard.add(Integer.parseInt(availableSpots.get(i)), player);

            // Depending on the current player, call minimax() recursively
            // And save the score in every possible move
            if (player.equals(aiPlayer)) {
                Move result = minimax(newBoard, humanPlayer, move);
                move.setScore(result.getScore());
            } else {
                Move result = minimax(newBoard, aiPlayer, move);
                move.setScore(result.getScore());
            }

            // Reset the board
            newBoard.remove(Integer.parseInt(availableSpots.get(i)));
            newBoard.add(Integer.parseInt(availableSpots.get(i)), Integer.toString(move.getIndex()));

            // Add the move to the ArrayList
            moves.add(move);
        }

        int bestMove = 0;
        if (player.equals(aiPlayer)) {
            int bestScore = -1000;
            for(int i = 0; i < moves.size(); i++){
                if (moves.get(i).getScore() > bestScore) {
                    bestScore = moves.get(i).getScore();
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 1000;
            for(int i = 0; i < moves.size(); i++){
                if (moves.get(i).getScore() < bestScore) {
                    bestScore = moves.get(i).getScore();
                    bestMove = i;
                }
            }
        }

        return moves.get(bestMove);
    }
}
