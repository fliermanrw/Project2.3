package model.games.othello;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by arch on 4/12/17.
 */
public class othelloMiniMax {

    boardCell[][] rootBoard;
    char rootTurn;
    int maxDepth;

    public othelloMiniMax(boardCell[][] rootBoard, char rootTurn, int maxDepth) {
        this.rootBoard = rootBoard;
        this.rootTurn = rootTurn;
        this.maxDepth = maxDepth;
        System.out.println("board according to minimax : " + Arrays.deepToString(rootBoard));
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
                {5, 1, 50, 15, 150, 50, 1, 5,},
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

    public int recursiveMiniMax(boardCell[][] tempBoard, char tempTurn, int depth) {
        // if the maxdepth is higher or equal to the currentdepth calulate for a new board
        if (maxDepth >= depth) {
            // convert board to list
            ArrayList<boardCell> resultBoard = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                for (int b = 0; b < 8; b++) {
                    resultBoard.add(new boardCell(i, b, tempBoard[i][b].getCharacterInCell()));
                }
            }
            // get the available moves from the current board
            for (boardCell a : othelloBoard.logic.fetchValidMovesAsCell(resultBoard, tempTurn)) {
                // for each board recursive search a new board with the move
                boardCell[][] newTempBoard = othelloBoard.logic.generateBoardWithNewMove(tempBoard, a, tempTurn);
                recursiveMiniMax(newTempBoard, getOtherTurn(tempTurn), depth++);
            }
        }
        return evaluateMove(tempBoard, tempTurn);
    }

    public int calculateBestMove() {
        int bestScore = 0;
        int bestMove = 0;
        // for every valid move calcualate recursive moves.
        System.out.println("Calculating best move using minimax.");
        // generate list from [][] array
        ArrayList<boardCell> resultBoard = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int b = 0; b < 8; b++) {
                resultBoard.add(new boardCell(i, b, rootBoard[i][b].getCharacterInCell()));
            }
        }

        for (boardCell a : othelloBoard.logic.fetchValidMovesAsCell(resultBoard, rootTurn)) {
            // generate a tempboard
            boardCell[][] tempboard = rootBoard;
            // apply the move to the board
            tempboard = othelloBoard.logic.generateBoardWithNewMove(tempboard, a, rootTurn);
            // check recursively what the score of the board is for a move at said depth.
            int scoreFromMove = recursiveMiniMax(tempboard, getOtherTurn(rootTurn), 0);
            System.out.println("score for move " + a.rowColToInt(a.getRow(), a.getCol()) + " was " + scoreFromMove);
            if (scoreFromMove > bestScore) {
                bestMove = a.rowColToInt(a.getRow(), a.getCol());
            }
        }

        System.out.println("Best move was : " + bestMove);
        return bestMove;
    }
}
