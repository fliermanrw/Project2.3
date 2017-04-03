package model;

import controller.PreGameView;
import controller.LoginController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
public class PreGameReader implements Runnable{
    Socket socket;
    ArrayList<PreGameView> views = new ArrayList<PreGameView>();

    public PreGameReader(Socket socket){
        this.socket = socket;
    }

    public void addView(PreGameView view){
        views.add(view);
    }

    public void removeView(PreGameView view){
        views.remove(view);
    }


    private void readfromSocket(Socket socket){
        String previousLine="";
        String currentLine;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
//                if(currentLine.contains("ERR")){
//                    String error = currentLine.replaceAll("(\\bERR\\b)", ""); //remove ERR from string
//                    for(PreGameView v: views){
//                        v.printError("Server tells us:" + error); //notify views
//                    }
//                }
                if(currentLine.contains("OK")) {
                   for(PreGameView v: views){
                       v.setSuccesfull(true); //last entered command was succesfull, notify the views
                   }
                }
                if(currentLine.contains("PLAYERLIST")){
                    String playerlist = currentLine.replaceAll("(\\[|\\SVR PLAYERLIST|\\]|\")", "");
                    List<String> players = Arrays.asList(playerlist.split(","));
                    for(PreGameView v: views){
                        v.setPlayerList(players);
                        v.setSuccesfull(true);
                    }
                }
                //Match for a game is found
                if(currentLine.contains("MATCH")){
                    String line = currentLine;
                    line = line.replaceAll("(SVR GAME MATCH )", ""); //remove SVR GAME MATCH
                    line = line.replaceAll("(\"|-)", "");//remove quotations and -
                    line = line.replaceAll("(\\w+)", "\"$1\""); //add quotations to every word
                    JSONParser parser = new JSONParser();
                    try {
                        JSONObject json = (JSONObject) parser.parse(line);
                        System.out.println(json.get("PLAYERTOMOVE"));
                        System.out.println(json.get("GAMETYPE"));
                        System.out.println(json.get("OPPONENT"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for(PreGameView v: views){
                    v.updateLog(currentLine);
                }


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
