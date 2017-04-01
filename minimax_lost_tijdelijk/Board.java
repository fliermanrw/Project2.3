
/* the original board
 O |   | X
 ---------
 X |   | X
 ---------
   | O | O
 */

import java.util.ArrayList;

public class Board {

    private String[] board;
    private String humanPlayer;
    private String aiPlayer;
    private int depth; // depth (or function calls)

    public Board() {
        board = new String[]{"X", "1", "2", "X", "4", "5", "6", "7", "8"};
        humanPlayer = "X";
        aiPlayer = "O";
        depth = 0;

        int bestMove = minimax(board, humanPlayer);
    }

    /**
     * A check if the current player (person or AI) has won with the current state of the board.
     *
     * @param gb the gameboard
     * @param player
     * @return boolean
     */
    private boolean hasWon(String[] gb, String player) {
        if ((gb[0] == player && gb[1] == player && gb[2] == player) ||
            (gb[3] == player && gb[4] == player && gb[5] == player) ||
            (gb[6] == player && gb[7] == player && gb[8] == player) ||
            (gb[0] == player && gb[3] == player && gb[6] == player) ||
            (gb[1] == player && gb[4] == player && gb[7] == player) ||
            (gb[2] == player && gb[5] == player && gb[8] == player) ||
            (gb[0] == player && gb[4] == player && gb[8] == player) ||
            (gb[2] == player && gb[4] == player && gb[6] == player)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return an array with the index and value of empty spots
     *
     * @param gb
     */
    private String[] emptySpots(String[] gb) {
        String[] result = new String[9];

        for (int i = 0; i < gb.length; i++) {
            if (gb[i] != "X" && gb[i] != "O") {
                result[i] = gb[i];
            }
        }

        return result;
    }

    /**
     * The minimax algorithm
     *
     * @param newBoard
     * @param player
     * @return
     */
    private int minimax(String[] newBoard, String player) {

        // Increment the depth (or function call) by 1
        this.depth++;

        // Get the empty spots
        String[] availableSpots = emptySpots(newBoard);

        // Check who has won. If the availableSpots are 0, the game is a TIE.
        if (hasWon(newBoard, humanPlayer)) {
            return -10;
        } else if (hasWon(newBoard, aiPlayer)) {
            return 10;
        } else if (availableSpots.length == 0) {
            return 0;
        }

        // Create an array with Move objects
        Move[] moves = new Move[availableSpots.length];
        // ArrayList<Move> moves = new ArrayList<>(); // Also possible

        // Loop over the available spots, skip the null values
        for (int i = 0; i < availableSpots.length; i++) {
            if (availableSpots[i] != null) {

                // Create a new Move object and set the available spot as their index
                Move move = new Move(Integer.parseInt(newBoard[Integer.parseInt(availableSpots[i])]));

                // Since the moves with index are saved, we can overwrite the spot with the current player
                newBoard[Integer.parseInt(availableSpots[i])] = player;

                // Depending on the current player, call minimax() recursively
                // And save the score in every possible move
                if (player.equals(aiPlayer)) {
                    int result = minimax(newBoard, humanPlayer);
                    move.setScore(result);
                } else {
                    int result = minimax(newBoard, aiPlayer);
                    move.setScore(result);
                }

                // Reset the board
                newBoard[Integer.parseInt(availableSpots[i])] = Integer.toString(move.getIndex());

                // Add the move to the ArrayList
                moves[i] = move;
            }
        }

        int bestMove = 0;
        if (player.equals(aiPlayer)) {
            int bestScore = -1000;
            for(int i = 0; i < moves.length; i++){
                if (moves[i] != null) {
                    if (moves[i].getScore() > bestScore) {
                        bestScore = moves[i].getScore();
                        bestMove = i;
                    }
                }
            }
        } else {
            int bestScore = 1000;
            for(int i = 0; i < moves.length; i++){
                if (moves[i] != null) {
                    if (moves[i].getScore() < bestScore) {
                        bestScore = moves[i].getScore();
                        bestMove = i;
                    }
                }
            }
        }

        // Fallback return
        return moves[bestMove].getIndex();

    }

}


