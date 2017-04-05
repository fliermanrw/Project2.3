package model.server_connection;

import controller.GameView;
import controller.GameView;
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
public class GameReader implements Runnable{
    Socket socket;
    ArrayList<GameView> views = new ArrayList<GameView>();

    public GameReader(Socket socket){
        this.socket = socket;
    }

    public void addView(GameView view){
        views.add(view);
    }

    public void removeView(GameView view){
        views.remove(view);
    }


    private void readfromSocket(Socket socket){
        String previousLine="";
        String currentLine;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
                if(currentLine.contains("YOURTURN")){
                    for(GameView v : views){
                        v.ourturn();//dummy data is index 1
                    }
                }
                if(currentLine.contains("MOVE")){
                    //opponent has made a move
                    String line = currentLine;
                    line = line.replaceAll("(SVR GAME MOVE )", ""); //remove SVR GAME MATCH
                    line = line.replaceAll("(\"|-)", "");//remove quotations and -
                    line = line.replaceAll("(\\w+)", "\"$1\""); //add quotations to every word
                    JSONParser parser = new JSONParser();
                    try {
                        JSONObject json = (JSONObject) parser.parse(line);
                        System.out.println(json.get("PLAYER"));
                        System.out.println(json.get("MOVE"));
                        System.out.println(json.get("DETAILS"));
                        int index = Integer.valueOf(json.get("MOVE").toString());
                        for(GameView v : views){
                            v.serverMove(index);//dummy data is index 1
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
//                for(GameView v : views){
//                    v.serverMove(1);//dummy data is index 1
//                }


//            SVR GAME MATCH {PLAYERTOMOVE: "test", GAMETYPE: "Tic-tac-toe", OPPONENT: "muis"}
//            SVR GAME YOURTURN {TURNMESSAGE: ""}
//            move 0
//            OK
//            SVR GAME MOVE {PLAYER: "test", MOVE: "0", DETAILS: ""}

                //@Todo Notift views on certain conditions

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
