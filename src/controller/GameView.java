package controller;

import javafx.fxml.Initializable;
import model.GameReader;
import model.TelnetWriter;

/**
 * Created by Ryan on 3-4-2017.
 */
public abstract class GameView{
    GameReader connectionReader;

    abstract void move(int place);

    abstract void forfeit();

    public abstract void setConnectionWriter(TelnetWriter w);

    public void setConnectionReader(GameReader r){
        connectionReader = r;
//        connectionReader.addView(this);
    }
}
