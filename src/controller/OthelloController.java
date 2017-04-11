package controller;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.games.othello.boardCell;
import model.games.othello.othelloGameModel;

import java.util.ArrayList;


public class OthelloController extends GameView{
    othelloGameModel othello;
    boolean ourturn = true;

    @FXML
    GridPane othelloGameBoard;

    public OthelloController(){
        othello = new othelloGameModel('B');
        othello.initGrid();
        othello.printBoard();
    }

    @FXML
    public void initialize() {
        for (int row = 0; row < 8; row++) {
            othelloGameBoard.addRow(row);
            for (int col = 0; col < 8; col++) {
                Button a = new Button();
                a.setPrefWidth(50.0);
                a.setPrefHeight(50.0);
                a.setId(Integer.toString(othello.rowColToInt(row, col)));
                a.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        buttonClick(a.getId(), a);
                    }
                });
                a.setText(Character.toString(othello.othelloBoard.cellsOnBoard.get(othello.rowColToInt(row,col)).getCharacterInCell()));
                othelloGameBoard.addColumn(col, a);
            }
        }
    }

    public void buttonClick(String id, Button button) {
        int index = Integer.parseInt(id);
        ArrayList<Integer> validMoves = othello.getValidMoves();

        System.out.println("button text:" + button.getId());
        if(ourturn) {
            if(!validMoves.contains(Integer.valueOf(button.getId()))){
                System.out.println("not a valid move");
            }else{
                button.setText(button.getId());
                //@todo don't set button just update the view representation here.
            }
        }else{
            System.out.println("Niet onze beurt");
        }
//        ourturn = false;
    }

    @Override
    public void serverMove(int index, String playerName) {

    }

    @Override
    public void ourturn() {
        ourturn = true;
    }

    @Override
    void move(int place) {
        //todo
    }

    @Override
    void forfeit() {
        //todo
    }
}

