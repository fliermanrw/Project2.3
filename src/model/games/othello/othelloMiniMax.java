package model.games.othello;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by arch on 4/12/17.
 */
public class othelloMiniMax extends othelloLogic {

    othelloBoard rootBoard;
    char rootTurn;
    int maxDepth;
    private int depth;

    public othelloMiniMax(othelloBoard rootBoard, char rootTurn, int maxDepth) {
        this.rootBoard = rootBoard;
        this.rootTurn = rootTurn;
        this.maxDepth = maxDepth;

        System.out.println("board according to minimax : " + Arrays.deepToString(rootBoard.getBoard()));
        System.out.println("turn according to minimax : " + rootTurn);
    }

    public int evaluateMove(boardCell[][] board, char turn) {
        int score = 0;

        int[][] weights = {
                {100, 5, 50, 20, 20, 50, 5, 100},
                {5, 1, 50, 15, 15, 50, 1, 5},
                {50, 50, 25, 10, 10, 25, 50, 50},
                {20, 15, 10, 50, 50, 10, 15, 20},
                {20, 15, 10, 50, 50, 10, 15, 20},
                {50, 50, 25, 10, 10, 25, 50, 50},
                {5, 1, 50, 15, 150, 50, 1, 5},
                {100, 5, 50, 20, 20, 50, 5, 100}
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getCharacterInCell() == turn) {
                    score += weights[i][j];
                }
            }
        }
        return score;
    }

    public char getOtherTurn(char currentTurn) {
        if (currentTurn == 'B') {
            return 'W';
        } else {
            return 'B';
        }
    }

    public int recursiveMiniMax(othelloBoard board, char tempTurn) {
            depth++;
        System.out.println("@@@BORD DAT HIJ BINNENKRIJGT@@@"); board.printBoard();
//        if (maxDepth >= depth) {
            System.out.println("depth is " + depth);
            // get the available moves from the current board
            ArrayList<boardCell> listOfMoves = fetchValidMovesAsCell(tempTurn, board);

            for (boardCell b : listOfMoves){
                System.out.println(b.rowColToInt(b.getRow(),b.getCol()));
            }

            for (boardCell a : listOfMoves) {
                boardCell[][] clone = board.getBoard().clone();

                // for each board recursive search a new board with the move
                othelloBoard newBoard = new othelloBoard();

                System.out.println("@@@BORD DAT HIJ MEESTUURT@@@"); newBoard.printBoard();
                applyMove(newBoard, a, tempTurn);
                recursiveMiniMax(newBoard, getOtherTurn(tempTurn));
            }
//        }
        return evaluateMove(board.getBoard(), tempTurn);
    }

    public int calculateBestMove() {

        System.out.println("ROOT BOARD");
        rootBoard.printBoard();

        System.out.println("BOARD 1");
        othelloBoard board1 = new othelloBoard();
        board1.setOthelloBoard(rootBoard.getBoard());
        board1.printBoard();

        System.out.println("BOARD 2");
        othelloBoard board2 = new othelloBoard();
        board2.setOthelloBoard(rootBoard.getBoard());
        board2.printBoard();

        System.out.println("BOARD 2 GEWIJZIGD");
        applyMove(board2, board2.getCellOnBoard(0,0), 'B');
        board2.printBoard();

        System.out.println("BOARD 1 AGAIN");
        board1.printBoard();

        System.out.println("ROOT BOARD AGAIN");
        rootBoard.printBoard();

//        int bestScore = 0;
        int bestMove = 0;
//        // for every valid move calcualate recursive moves.
//        System.out.println("Calculating best move using minimax.");
//
//        for (boardCell a : fetchValidMovesAsCell(rootTurn, rootBoard)) {
//
//            othelloBoard newBoard = new othelloBoard(rootBoard.getBoard());
//            applyMove(newBoard, a, rootTurn);
//            // check recursively what the score of the board is for a move at said depth.
//            int scoreFromMove = recursiveMiniMax(newBoard, getOtherTurn(rootTurn));
//            System.out.println("score for move " + a.rowColToInt(a.getRow(), a.getCol()) + " was " + scoreFromMove);
//            if (scoreFromMove >= bestScore) {
//                bestMove = a.rowColToInt(a.getRow(), a.getCol());
//                // reset score.
//                bestScore = 0;
//            }
//        }
//
//        System.out.println("Best move was : " + bestMove);
        return bestMove;
    }
}
