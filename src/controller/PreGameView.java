package controller;

import javafx.stage.Stage;

import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by jouke on 2-4-2017.
 */
public abstract class PreGameView{
    List<String> playerList;
    Stage stage;
    Socket socket;

    public abstract void startGame(String game, String playerToMove);

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    public void setPlayerList(List<String> players){
        System.out.println("playerlist is set in abstractview");
        this.playerList = players;
    }

    public List<String> getPlayerList(){
        return playerList;
    }

    public abstract void useBot();
}
