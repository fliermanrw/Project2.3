package controller;

import javafx.stage.Stage;
import model.server_connection.PreGameReader;
import model.server_connection.TelnetWriter;

import java.net.Socket;
import java.util.List;

/**
 * Created by jouke on 2-4-2017.
 */
public abstract class PreGameView{
    List<String> playerList;
    PreGameReader connectionReader;
    String log = "";
    Stage stage;
    Socket socket;

    public abstract void setSuccesfull(boolean status);
    public abstract void setConnectionWriter(TelnetWriter w);

    public void setConnectionReader(PreGameReader r){
        connectionReader = r;
        connectionReader.addView(this);
    }

    public void setSocket(Socket s){
        socket = s;
    }

    public Socket getSocket(){
        return socket;
    }
    public abstract void startGame(String game);

    public void updateLog(String currentLine){
        log += currentLine;
        log += "\n";
    }

    public String getLog(){
        return log;
    }

    public PreGameReader getConnectionReader(){
        return connectionReader;
    }

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
}
