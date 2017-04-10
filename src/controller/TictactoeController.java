package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.server_connection.ServerHandlerWriter;
import model.server_connection.TelnetWriter;
import model.games.tictactoe.Tictactoe;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by jouke on 3-4-2017.
 */

public class TictactoeController extends GameView {
    TelnetWriter connectionWriter;
    Tictactoe tictactoe;
    boolean ourturn = false;
    @FXML GridPane gameBoard;


    public TictactoeController() {
        tictactoe = new Tictactoe();
    }

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());
        System.out.println("INDEX: " + index);
        ArrayList<Integer> validMoves = tictactoe.getValidMoves();
        System.out.println("Validmoves" + validMoves);

        if(ourturn){
            System.out.println("OURMOVE = TRUE");
            if(tictactoe.hasWon(tictactoe.getGrid(), tictactoe.getCurrentPlayer())){
                System.out.println(tictactoe.getCurrentPlayer() + "Has won the game");
            }else{
                if(validMoves.size() == 0){
                    System.out.println("Its a tie");
                }else{
                    if(validMoves.contains(index)){
                        tictactoe.move(index);
                        move(index);
                        ourturn = false;
                    }
                }
            }
        }
    }

    //When server notifies us of a new move
    @Override
    public void serverMove(int index) {
        tictactoe.move(index); //set position in model
        Platform.runLater(()->{
            for(Node node : gameBoard.getChildren()){
                System.out.println("looping trough nodes");
                if(node instanceof Button){
                    Button button = (Button) node;
                    int buttonid = Integer.valueOf(button.getId());
                    System.out.println("Button id:" + buttonid + "server index id:" + index);
                    if(buttonid == index){
                        System.out.println("button id equals index");
                        button.setText(tictactoe.getCurrentPlayer());
                    }
                }
            }
        });
        tictactoe.switchPlayer();
        ourturn();
//        System.out.println(tictactoe.getGrid());
//        System.out.println(tictactoe.getCurrentPlayer());
//        System.out.println(index);
//        tictactoe.move(index); //set position in model
//        System.out.println(tictactoe.getCurrentPlayer());
//        System.out.println(tictactoe.getGrid());
//        System.out.println("TictactoeController: Received move: " + index);
    }

    @Override
    public void ourturn() {
        ourturn = true;
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Positie? (0 t/m 8): ");
//        int pos = Integer.parseInt(scanner.nextLine());
//        connectionWriter.sendData("move " + Integer.toString(pos));
        System.out.println("TictactoeController: Got notified it's now our turn and we can make a move");
    }

    @Override
    void move(int place) {
        ServerHandlerWriter.writeSend("move " + place);
    }

    @Override
    void forfeit() {
        connectionWriter.sendData("forfeit");
    }

    @Override
    public void setConnectionWriter(TelnetWriter w) {
        connectionWriter = w;
    }
}

