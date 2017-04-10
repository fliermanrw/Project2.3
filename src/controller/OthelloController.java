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
    othelloGameModel othello = new othelloGameModel('W');
    boolean ourturn = false;

    @FXML
    GridPane othelloGameBoard;


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

        if(ourturn) {
            button.setText("B");
            othello.move(index);
            ourturn = false;
        } else{
            //wachten op de move van de server
            // doe dingen..

           button.setText("W");
           ourturn();


        }
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

