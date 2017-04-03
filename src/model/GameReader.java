package model;

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
