package controller;

/**
 * Created by Ryan on 3-4-2017.
 */
public abstract class GameView{
    public abstract void serverMove(int index, String playerName);

    public abstract void ourturn();

    abstract void move(int place);

    abstract void forfeit();
}
