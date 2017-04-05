package controller;

import model.server_connection.GameReader;
import model.server_connection.TelnetWriter;

/**
 * Created by Ryan on 3-4-2017.
 */
public abstract class GameView{
    GameReader connectionReader;

    public abstract void serverMove(int place);

    public abstract void ourturn();

    abstract void move(int place);

    abstract void forfeit();

    public abstract void setConnectionWriter(TelnetWriter w);

    public void setConnectionReader(GameReader r){
        connectionReader = r;
        connectionReader.addView(this);
    }
}
