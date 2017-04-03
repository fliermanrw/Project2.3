package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;


/**
 * Created by jouke on 3-4-2017.
 */

public class GridController extends GameView{
    public void buttonClick(ActionEvent actionEvent) {
        String x = actionEvent.getSource().toString();
        System.out.println(x);
        if(x.equals("b1")){
            System.out.println("xx");
        } else System.out.println("yy");
    }

    @Override
    void move(int place) {
        //@todo
    }

    @Override
    void forfeit() {
        //@todo
    }
}

