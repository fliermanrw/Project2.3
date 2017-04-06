package controller;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.event.ActionEvent;
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

//<Button fx:id="7" onAction="#buttonClick" prefHeight="50.0" prefWidth="50.0" GridPane.columnIndex="0" GridPane.rowIndex="7" />
//<Button fx:id="8" onAction="#buttonClick" prefHeight="50.0" prefWidth="50.0" GridPane.columnIndex="1" GridPane.rowIndex="0" /

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());

        ArrayList<Integer> validMoves = othello.getValidMoves();

        if(validMoves.contains(index)){
            btn.setText("X");
        }
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

