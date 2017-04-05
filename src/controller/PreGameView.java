package controller;

import model.server_connection.PreGameReader;
import model.server_connection.TelnetWriter;

import java.util.List;

/**
 * Created by jouke on 2-4-2017.
 */
public abstract class PreGameView{
    List<String> playerList;
    PreGameReader connectionReader;
    String log = "";

    public abstract void setSuccesfull(boolean status);
    public abstract void setConnectionWriter(TelnetWriter w);

    public void setConnectionReader(PreGameReader r){
        connectionReader = r;
        connectionReader.addView(this);
    }

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

    public void setPlayerList(List<String> players){
        System.out.println("playerlist is set in abstractview");
        this.playerList = players;
    }

    public List<String> getPlayerList(){
        return playerList;
    }
}
