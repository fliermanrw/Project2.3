package model.minimax_los_tijdelijk;
/* Board
 X | O | X
 ---------
 O | O | X
 ---------
 6 | X | 8
 */

import java.util.ArrayList;
import java.util.Scanner;


public class Board {

    private ArrayList<String> board;
    private String humanPlayer;
    private String aiPlayer;
    private int depth; // depth (or function calls)

    public Board() {
        board = new ArrayList<>();
        humanPlayer = "X";
        aiPlayer = "O";
        depth = 0;

        board.add("0");
        board.add("1");
        board.add("2");
        board.add("3");
        board.add("4");
        board.add("5");
        board.add("6");
        board.add("7");
        board.add("8");

        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("Positie? (0 t/m 8): ");
            int pos = Integer.parseInt(scanner.nextLine());
            generateView(pos, humanPlayer);

            //Move bestMove = minimax(board, aiPlayer, new Move(0));
            //generateView(bestMove.getIndex(), aiPlayer);
        }

//        Move bestMove = minimax(board, aiPlayer, new Move(0));
//
//        System.out.println("Best Move: " + bestMove.getIndex());
//        System.out.println("Depth: " + depth);
    }

    private void generateView(int pos, String player) {
        if (pos == 10) {
            System.out.println("U heeft verloren");
            return;
        } else if (pos == -10) {
            System.out.println("U heeft gewonnen");
            return;
        } else if (pos == -1) {
            System.out.println("Gelijkspel");
            return;
        }

        board.remove(pos);
        board.add(pos, player);
        for(int i = 0; i < board.size(); i++) {
            if (i % 3 != 0) {
                System.out.print(" " + board.get(i) + " ");
            } else {
                System.out.println();
                System.out.print(" " + board.get(i) + " ");
            }
        }
        System.out.println();
    }

    /**
     * A check if the current player (person or AI) has won with the current state of the board.
     *
     * @param gb the gameboard
     * @param player
     * @return boolean
     */
    private boolean hasWon(ArrayList<String> gb, String player) {
        if ((gb.get(0) == player && gb.get(1) == player && gb.get(2) == player) ||
            (gb.get(3) == player && gb.get(4) == player && gb.get(5) == player) ||
            (gb.get(6) == player && gb.get(7) == player && gb.get(8) == player) ||
            (gb.get(0) == player && gb.get(3) == player && gb.get(6) == player) ||
            (gb.get(1) == player && gb.get(4) == player && gb.get(7) == player) ||
            (gb.get(2) == player && gb.get(5) == player && gb.get(8) == player) ||
            (gb.get(0) == player && gb.get(4) == player && gb.get(8) == player) ||
            (gb.get(2) == player && gb.get(4) == player && gb.get(6) == player)) {
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
    private ArrayList<String> emptySpots(ArrayList<String> gb) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < gb.size(); i++) {
            if (gb.get(i) != "X" && gb.get(i) != "O") {
                result.add(gb.get(i));
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
//    private Move minimax(ArrayList<String> newBoard, String player, Move m) {
//
//        // Increment the depth (or function call) by 1
//        this.depth++;
//        //if (m == null) m = new Move(0);
//        // Get the empty spots
//        ArrayList<String> availableSpots = emptySpots(newBoard);
//
//        // Check who has won. If the availableSpots are 0, the game is a TIE.
//        if (hasWon(newBoard, humanPlayer)) {
//            m.setScore(-10);
//            return m;
//        } else if (hasWon(newBoard, aiPlayer)) {
//            m.setScore(10);
//            return m;
//        } else if (availableSpots.size() == 0) {
//            m.setScore(-1);
//            return m;
//        }
//
//        // Create an ArrayList with Move objects
//         ArrayList<Move> moves = new ArrayList<>();
//
//        // Loop over the available spots, skip the null values
//        for (int i = 0; i < availableSpots.size(); i++) {
//
//            // Create a new Move object and set the available spot as their index
//            Move move = new Move(Integer.parseInt(newBoard.get(Integer.parseInt(availableSpots.get(i)))));
//
//            // Since the moves with index are saved, we can overwrite the spot with the current player
//            newBoard.remove(Integer.parseInt(availableSpots.get(i)));
//            newBoard.add(Integer.parseInt(availableSpots.get(i)), player);
//
//            // Depending on the current player, call minimax() recursively
//            // And save the score in every possible move
//            if (player.equals(aiPlayer)) {
//                Move result = minimax(newBoard, humanPlayer, move);
//                move.setScore(result.getScore());
//            } else {
//                Move result = minimax(newBoard, aiPlayer, move);
//                move.setScore(result.getScore());
//            }
//
//            // Reset the board
//            newBoard.remove(Integer.parseInt(availableSpots.get(i)));
//            newBoard.add(Integer.parseInt(availableSpots.get(i)), Integer.toString(move.getIndex()));
//
//            // Add the move to the ArrayList
//            moves.add(move);
//        }
//
//        int bestMove = 0;
//        if (player.equals(aiPlayer)) {
//            int bestScore = -1000;
//            for(int i = 0; i < moves.size(); i++){
//                if (moves.get(i).getScore() > bestScore) {
//                    bestScore = moves.get(i).getScore();
//                    bestMove = i;
//                }
//            }
//        } else {
//            int bestScore = 1000;
//            for(int i = 0; i < moves.size(); i++){
//                if (moves.get(i).getScore() < bestScore) {
//                    bestScore = moves.get(i).getScore();
//                    bestMove = i;
//                }
//            }
//        }
//
//        return moves.get(bestMove);
//    }

}


