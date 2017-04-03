package model;

import controller.AbstractView;
import controller.LoginController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetReader implements Runnable{
    Socket socket;
    ArrayList<AbstractView> views = new ArrayList<AbstractView>();

    public TelnetReader(Socket socket){
        this.socket = socket;
    }

    public void addView(AbstractView view){
        views.add(view);
    }

    public void removeView(AbstractView view){
        views.remove(view);
    }


    private void readfromSocket(Socket socket){
        String previousLine="";
        String currentLine;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
                if(currentLine.contains("ERR")){
                    String error = currentLine.replaceAll("(\\bERR\\b)", ""); //remove ERR from string
                    for(AbstractView v: views){
                        v.printError("Server tells us:" + error); //notify views
                    }
                }
                if(currentLine.contains("OK")) {
                   for(AbstractView v: views){
                       v.setSuccesfull(true); //last entered command was succesfull, notify the views
                   }
                }
                if(currentLine.contains("PLAYERLIST")){
                    String playerlist = currentLine.replaceAll("(\\[|\\SVR PLAYERLIST|\\]|\")", "");
                    List<String> players = Arrays.asList(playerlist.split(","));
                    for(AbstractView v: views){
                        v.setPlayerList(players);
                        v.setSuccesfull(true);
                    }
                }

                //@todo notify views?
                //@todo maybe pass controler to this class?
                System.out.println(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        readfromSocket(socket);
    }
}
