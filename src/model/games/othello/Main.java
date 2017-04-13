package model.games.othello;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by jouke on 11-4-2017.
 */
public class Main {
    private static othelloGameModel othello;
    public static void main(String args[]){
        othello = new othelloGameModel('B');
        othello.initGrid();

        randomMove();
    }
    public void humanMove(){
        Scanner scanner = new Scanner(System.in);

        while (true) {

//            othello.startMinimax();

            System.out.println("Turn : " + othello.getCurrentPlayer());
            othello.printBoard();
            ArrayList<Integer> moves = othello.getValidMoves();
            System.out.println("Available moves: " + moves);
            System.out.println("Number of moves: " + moves.size());

            System.out.print("Positie?: ");
            int pos = Integer.parseInt(scanner.nextLine());

            if(moves.size() == 0){
                System.out.println("No more moves for: " + othello.getCurrentPlayer());
                System.out.println("Winner is:" + othello.findCurrentWinner());
            }else{
                if(moves.contains(pos)){
                    othello.move(pos);
                    othello.switchPlayer();
                }else{
                    System.out.println("Wrong move, not an option");
                }
            }
        }
    }

    public static void randomMove(){
        while(!othello.getValidMoves().isEmpty()) {
            othello.printBoard();
            ArrayList<Integer> validMoves = othello.getValidMoves();
            System.out.println("Bot CURRENT PLAYER: " + othello.getCurrentPlayer());
            System.out.println("Bot AVAIABLE MOVES: " + validMoves);

            Random rand = new Random();
            int random = rand.nextInt(validMoves.size());
            System.out.println("Bot MOVE: " + String.valueOf(validMoves.get(random)));

            othello.move(validMoves.get(random));
            othello.switchPlayer();
        }
        System.out.println("Winner is:" + othello.findCurrentWinner());
    }
}
