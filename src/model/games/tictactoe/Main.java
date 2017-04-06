package model.games.tictactoe;

import java.util.Scanner;

/**
 * Created by Danny on 5-4-2017.
 */
public class Main {

    public static void main(String[] args) {

//        playWithHuman();
//        playWithAi();

    }

    private static void playWithHuman() {

        Tictactoe tictactoe = new Tictactoe();
        tictactoe.setCurrentPlayer("X");
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Positie? (0 t/m 8): ");
            int pos = Integer.parseInt(scanner.nextLine());
            tictactoe.move(pos);
            tictactoe.printGrid();

        }

    }

    private static void playWithAi() {

        Tictactoe tictactoe = new Tictactoe();
        tictactoe.setHumanPlayer("X");
        tictactoe.setAiPlayer("O");
        tictactoe.setCurrentPlayer(tictactoe.getHumanPlayer());
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Positie? (0 t/m 8): ");
            int pos = Integer.parseInt(scanner.nextLine());
            tictactoe.move(pos);
            tictactoe.printGrid();

            Move bestMove = tictactoe.minimax(tictactoe.getGrid(), tictactoe.getAIPlayer(), new Move(0));
            tictactoe.move(bestMove.getIndex());
            tictactoe.printGrid();
        }

    }

}
