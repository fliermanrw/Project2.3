package model.games.othello;

import java.util.ArrayList;

/**
 * Created by arch on 4/12/17.
 */
public class othelloMiniMax extends othelloLogic {
    int totalDepth;
    int maxDepth = 10;
    char rootTurn;

    long scoreForBranch = 0;

    public long evaluateMove(ArrayList<boardCell> board, char turn) {
        int scoreForBoard = 0;
        int[][] weights = {
                {100, 5, 50, 20, 20, 50, 5, 100},
                {5, 1, 50, 15, 15, 50, 1, 5},
                {50, 50, 25, 10, 10, 25, 50, 50},
                {20, 15, 10, 50, 50, 10, 15, 20},
                {20, 15, 10, 50, 50, 10, 15, 20},
                {50, 50, 25, 10, 10, 25, 50, 50},
                {5, 1, 50, 15, 15, 50, 1, 5},
                {100, 5, 50, 20, 20, 50, 5, 100}
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int index = rowColToInt(i, j);
                if (board.get(index).getCharacterInCell() == turn) {
                    scoreForBoard += weights[i][j];
                }
            }
        }
        scoreForBranch += scoreForBoard;
        return scoreForBranch;
    }

    public char getOtherTurn(char currentTurn) {
        if (currentTurn == 'B') {
            return 'W';
        } else {
            return 'B';
        }
    }

    public long recursiveMiniMax(ArrayList<boardCell> board, char tempTurn, int depth) {
        totalDepth++;
        if (maxDepth > depth) {
            // get the available moves from the current board
            for (boardCell a : fetchValidMovesAsCell(tempTurn, board)) {
                ArrayList<boardCell> newBoard = new ArrayList<>();
                for (boardCell rootCell2 : board) {
                    try {
                        newBoard.add((boardCell) rootCell2.clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                // for each board recursive search a new board with the new move
                applyMove(newBoard, a, tempTurn);
                evaluateMove(newBoard, rootTurn);
                recursiveMiniMax(newBoard, getOtherTurn(tempTurn), depth += 1);
            }
        }
        return evaluateMove(board, rootTurn);
    }

    public int calculateBestMove(ArrayList<boardCell> rootBoard, char turn) {
        this.rootTurn = turn;

        long bestScore = 0;
        int bestMove = 0;
        // for every valid move calcualate recursive moves.
        System.out.println("Calculating best move using minimax.");

        // Generate new boards based on available moves from rootboard.
        for (boardCell cell : fetchValidMovesAsCell(turn, rootBoard)) {
            ArrayList<boardCell> newRootBoard = new ArrayList<>();
            for (boardCell rootCell : rootBoard) {
                try {
                    newRootBoard.add((boardCell) rootCell.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            applyMove(newRootBoard, cell, turn);

            long scoreFromMove = recursiveMiniMax(newRootBoard, getOtherTurn(turn), 0);
            System.out.println("score for move " + cell.getIndexForCell() + " was " + scoreFromMove);
            // check recursively what the score of the board is for a move at said depth.
            if (scoreFromMove >= bestScore) {
                bestMove = cell.getIndexForCell();
                bestScore = scoreFromMove;
            }
            scoreForBranch = 0;
        }
        System.out.println("Best move was : " + bestMove);
        System.out.println("Totaldepth: " + totalDepth);
        return bestMove;
    }
}
