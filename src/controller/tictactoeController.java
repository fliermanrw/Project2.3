package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import model.TelnetWriter;
import model.Tictactoe;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by jouke on 3-4-2017.
 */

public class tictactoeController extends GameView{
    TelnetWriter connectionWriter;
    Tictactoe tictactoe = new Tictactoe();
    boolean ourturn = false;

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());

        ArrayList<Integer> validMoves = tictactoe.getValidMoves();

        if(ourturn){
            if(tictactoe.hasWon(tictactoe.getGrid(), tictactoe.getCurrentPlayer())){
                System.out.println(tictactoe.getCurrentPlayer() + "Has won the game");
            }else{
                if(validMoves.size() == 0){
                    System.out.println("Its a tie");
                }else{
                    if(validMoves.contains(index)){
                        btn.setText(tictactoe.getCurrentPlayer());
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

