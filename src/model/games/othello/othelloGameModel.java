package model.games.othello;

import model.games.GameModel;
import model.games.tictactoe.Move;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 347727 on 5-4-2017.
 */
public class othelloGameModel implements GameModel {

    public othelloBoard othelloBoard;
    char turn;
    private int depth;
    ArrayList<Integer> grid;

    private boardCell[][] copyOfBoard;

    public othelloGameModel(char turn) {
        this.turn = turn;
        // Make a new board and assign the colour for the bot.
        // 'B' if bot plays first 'W' if bot plays second
        othelloBoard = new othelloBoard(turn);
    }

    public othelloBoard getOthelloBoard(){
        return othelloBoard;
    }

    @Override
    public boolean hasWon(ArrayList<String> grid, String player) {
        return false;
    }

    @Override
    public String getCurrentPlayer() {
        return Character.toString(othelloBoard.turn);
    }

    @Override
    public void switchPlayer() {
        othelloBoard.swapTurn();
    }

    @Override
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
//        System.out.println("We are now trying to get the validmoves of turn: " + othelloBoard.turn);
        System.out.println("TURN IN OTHELLO MODEL: " + othelloBoard.turn);
        for (boardCell cell : othelloBoard.logic.fetchValidMovesAsCell(othelloBoard.cellsOnBoard, othelloBoard.turn)) {
            listOfMoves.add(rowColToInt(cell.getRow(), cell.getCol()));
        }
        return listOfMoves;
    }

    public ArrayList<Integer> getValidMoves(othelloBoard board) {
        ArrayList<Integer> listOfMoves = new ArrayList<>();
        for (boardCell cell : board.logic.fetchValidMovesAsCell(board.cellsOnBoard, board.turn)) {
            listOfMoves.add(rowColToInt(cell.getRow(), cell.getCol()));
        }
        return listOfMoves;
    }

    @Override
    public void move(int move) {
        int y = move % 8;
        int x = 0;
        for (int j = 0; j < move + 1; j++) {
            if (j % 8 == 0) {
                x++;
            }
        }
        othelloBoard.logic.applyMove(othelloBoard.getBoard(), othelloBoard.getCellOnBoard(x - 1, y), othelloBoard.turn);
        othelloBoard.refreshBoardList();
    }

    public othelloBoard moveAndCopy(int move) {
        othelloBoard clone = othelloBoard.clone();
        int y = move % 8;
        int x = 0;
        for (int j = 0; j < move + 1; j++) {
            if (j % 8 == 0) {
                x++;
            }
        }
        clone.logic.applyMove(othelloBoard.getBoard(), othelloBoard.getCellOnBoard(x - 1, y), othelloBoard.turn);
        clone.refreshBoardList();
        return clone;
    }

    public String findCurrentWinner(){
        return othelloBoard.findCurrentWinner();
    }

    @Override
    public void initGrid() {
        othelloBoard.generateNewBoard();
        othelloBoard.refreshBoardList();
    }

    public void printBoard(){
        othelloBoard.printBoard();
    }

    public int rowColToInt(int row, int col) {
        int index = row * 8 + col;
        return index;
    }

    public ArrayList<String> convertBoardToArrayList() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int row = othelloBoard.getBoard()[i][j].getRow();
                int col = othelloBoard.getBoard()[i][j].getCol();

                if (othelloBoard.getBoard()[i][j].getCharacterInCell() == '#') {
                    result.add(Integer.toString(rowColToInt(row, col)));
                } else {
                    result.add(Character.toString(othelloBoard.getBoard()[i][j].getCharacterInCell()));
                }
            }
        }
        return result;
    }


    public void startMinimax() {
        ArrayList<String> boardArrayList = convertBoardToArrayList();
        minimax(othelloBoard.getBoard(), getCurrentPlayer(), new Move(0));
    }

    public void minimax(boardCell[][] newBoard, String player, Move m) {
        // Increment the depth (or function call) by 1
        this.depth++;

        // Get the empty spots
        ArrayList<Integer> availableSpots = getValidMoves();

        if (othelloBoard.getTurn() == turn && othelloBoard.findCurrentWinner().equals(Character.toString(othelloBoard.getTurn()))) {
//            // Win
//            // @todo setScore & return
            System.out.println("WIN");

            //return;
        } else if (othelloBoard.getTurn() != turn && othelloBoard.findCurrentWinner().equals(Character.toString(othelloBoard.getTurn()))) {
//            // Loss
//            // @todo setScore & return
            System.out.println("LOSS");
            //return;
        } else if (othelloBoard.findCurrentWinner().equals("T") && availableSpots.size() == 0) {
            // Tie
            // @todo setScore & return
            System.out.println("TIE");
            //return;
        }

        // Create an ArrayList with Move objects
        ArrayList<Move> moves = new ArrayList<>();
        System.out.println(availableSpots);

        for (int i = 0; i < availableSpots.size(); i++) {

            Move move = new Move(availableSpots.get(i));

            // Since the moves with index are saved, we can overwrite the spot with the current player
//            newBoard[copyBoard[i]] = player;
//            newBoard.remove(Integer.parseInt(availableSpots.get(i)));
//            newBoard.add(Integer.parseInt(availableSpots.get(i)), player);
            //move(availableSpots.get(i));
            //othelloBoard copyBoard = moveAndCopy(availableSpots.get(i));
            boardCell[][] test = newBoard.clone();
            move(availableSpots.get(i));

            if (othelloBoard.getTurn() == turn) {
                othelloBoard.swapTurn();
                minimax(test, getCurrentPlayer(), move);
            } else {
                othelloBoard.swapTurn();
                minimax(test, getCurrentPlayer(), move);
            }
//            moveAndCopy(availableSpots.get(i));

        }
        System.out.println(depth);

    }
}
