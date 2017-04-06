package controller;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.games.othello.othelloGameModel;
import model.server_connection.TelnetWriter;

import java.util.ArrayList;


public class OthelloController extends GameView{
    TelnetWriter connectionWriter;
    othelloGameModel othello = new othelloGameModel('W');
    boolean ourturn = false;

    @FXML
    GridPane othelloGameBoard;


    @FXML
    public void initialize(){
        for (int row = 0; row < 8; row ++) {
            othelloGameBoard.addRow(row);
            for (int col = 0; col < 8; col++) {
                Button a = new Button();
                a.setPrefWidth(50.0);
                a.setPrefHeight(50.0);
                a.setId(Integer.toString(othello.rowColToInt(row, col)));
                a.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        buttonClick(a.getId());
                    }
                });
                othelloGameBoard.addColumn(col, a);
            }
        }
    }

    public void buttonClick(String id) {
        int index = Integer.parseInt(id);
        ArrayList<Integer> validMoves = othello.getValidMoves();
        othello.move(index);
    }

    //When server notifies us of a new move
    @Override
    public void serverMove(int index) {
        othello.move(index); //set position in model
    }

    @Override
    public void ourturn() {
        ourturn = true;
    }

    @Override
    void move(int place) {
        connectionWriter.sendData("move " + place);
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

