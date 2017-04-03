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

    public void buttonClick(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Integer index = Integer.valueOf(btn.getId().toString());

        ArrayList<Integer> validMoves = tictactoe.getValidMoves();

        if(tictactoe.hasWon(tictactoe.getGrid(), tictactoe.getCurrentPlayer())){
            System.out.println(tictactoe.getCurrentPlayer() + "Has won the game");
        }else{
            if(validMoves.size() == 0){
                System.out.println("Its a tie");
            }else{
                if(validMoves.contains(index)){
                    btn.setText(tictactoe.getCurrentPlayer());
                    tictactoe.move(index);
                }
            }
        }

//        connectionWriter.sendData("move " + index);
    }

    @Override
    void move(int place) {
        //@todo
    }

    @Override
    void forfeit() {
        //@todo
    }

    @Override
    public void setConnectionWriter(TelnetWriter w) {
        connectionWriter = w;
    }
}

