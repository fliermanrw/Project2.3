package model.games.othello;

/**
 * Created by jouke on 11-4-2017.
 */
public class Main {
    public static void main(String args[]){
        othelloGameModel m = new othelloGameModel('B');
        othelloBoard board = m.getOthelloBoard();
        board.recursiveMove();
        board.printBoard();
    }
}
