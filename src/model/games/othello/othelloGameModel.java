package model.games.othello;

import model.games.GameModel;
import model.games.tictactoe.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel extends othelloLogic implements GameModel {
    private ArrayList<boardCell> othelloBoard;
    private char turn;

    private int size = 8;

    public othelloGameModel(char turn) {
        this.turn = turn;
        // Make a new board and assign the colour for the bot.
        // 'B' if bot plays first 'W' if bot plays second
    }

    public ArrayList<boardCell> getOthelloBoard() {
        synchronized (othelloBoard) {
            return othelloBoard;
        }
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return Character.toString(turn);
    }

    @Override
    public void switchPlayer() {
        swapTurn();
    }

    public char getTurn() {
        return turn;
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
        synchronized (othelloBoard) {
            for (boardCell cell : fetchValidMovesAsCell(turn, othelloBoard)) {
                listOfMoves.add(rowColToInt(cell.getRow(), cell.getCol()));
            }
            return listOfMoves;
        }
    }

    @Override
    public void move(int move) {
        synchronized (othelloBoard) {
            applyMove(othelloBoard, othelloBoard.get(move), turn);
        }
    }

    @Override
    public void initGrid() {
        boardCell[][] newBoard = new boardCell[size][size];
        for (int i = 0; i < size; i++) {
            for (int b = 0; b < size; b++) {
                if (i == 3 && b == 3 || i == 4 && b == 4) {
                    // white
                    newBoard[i][b] = new boardCell(i, b, 'W');
                } else if ((i == 3 && b == 4) || (i == 4 && b == 3)) {
                    // black
                    newBoard[i][b] = new boardCell(i, b, 'B');
                } else {
                    // empty
                    newBoard[i][b] = new boardCell(i, b, '#');
                }
            }
        }
        // Generate a board object.
        othelloBoard = twoDArrayToList(newBoard);
    }

    private ArrayList<boardCell> twoDArrayToList(boardCell[][] twoDArray) {
        ArrayList<boardCell> list = new ArrayList<boardCell>();
        for (boardCell[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public ArrayList<boardCell> convertBoardToArrayListOfCells(boardCell[][] list) {
        ArrayList<boardCell> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int b = 0; b < 8; b++) {
                result.add(new boardCell(i, b, list[i][b].getCharacterInCell()));
            }
        }
        return result;
    }

    public int getMiniMaxMove(char turn) {
        return new othelloMiniMax().calculateBestMove(othelloBoard, turn);
    }

    private String findCurrentWinner() {
        HashMap<String, Integer> map = getCurrentPoints();
        int whitePoints = map.get("W");
        int blackPoints = map.get("B");

        if (whitePoints > blackPoints) {
            return "W"; // White wins
        } else if (blackPoints > whitePoints) {
            return "B"; // Black wins
        }
        return "T"; // Tie
    }

    private HashMap<String, Integer> getCurrentPoints() {
        int whitePoints = 0;
        int blackPoints = 0;
        synchronized (othelloBoard) {
            for (boardCell a : othelloBoard) {
                    switch (a.getCharacterInCell()) {
                        case 'W':
                            whitePoints++;
                            break;
                        case 'B':
                            blackPoints++;
                            break;
                        default:
                            break;
                    }
                }
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("W", whitePoints);
        map.put("B", blackPoints);

        return map;
    }

    private void swapTurn() {
        if (turn == 'B') {
            turn = 'W';
        } else {
            turn = 'B';
        }
        //System.out.println("Current turn is : " + turn);
    }

    public ArrayList<boardCell> getBoard() {
        synchronized (othelloBoard) {
            return othelloBoard;
        }
    }


}
