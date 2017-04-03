package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import model.TelnetWriter;


/**
 * Created by jouke on 3-4-2017.
 */

public class tictactoeController extends GameView{
    public void buttonClick(ActionEvent actionEvent) {
        String index = actionEvent.getSource().toString();
        Button btn = (Button) actionEvent.getSource();
        btn.setText("X");
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

    }
}

