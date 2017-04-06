package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import model.games.othello.othelloGameModel;
import model.server_connection.TelnetWriter;

import java.util.ArrayList;


public class OthelloController extends GameView{
    TelnetWriter connectionWriter;
    othelloGameModel othello = new othelloGameModel('W');
    boolean ourturn = false;

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());

        ArrayList<Integer> validMoves = othello.getValidMoves();

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

