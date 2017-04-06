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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    System.out.println("GameReader: it's now our turn, notify views");
                    for(GameView v : views){
                        v.ourturn();//dummy data is index 1
                    }
                }
                if(currentLine.contains("MOVE")){
                    //opponent has made a move
                    String line = currentLine;
                    //        First group: ([A-Za-z]+):  //Match any characert from A-Z or a-z, + means 1 or more, end with :
                    //        So the first group matches every word that ends with :
                    //        ---------------------------------------------------------------
                    //        Second group: "([^"]*)"    //Match the ", then [^"] = match a single characer " that is not in the set,  * = between zero and more times, end with a " .
                    //        So the second group matches everything between quotes and checks if it doesn't contain quotes itself?
                    Map<String,String> vars = new HashMap<>();
                    Pattern pattern = Pattern.compile("([A-Za-z]+): \"([^\"]*)\"");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        vars.put(matcher.group(1), matcher.group(2));
                    }
                    System.out.println(vars.get("MOVE"));

                    for(GameView v : views){
                            v.serverMove(Integer.valueOf(vars.get("MOVE")));//dummy data is index 1
                    }
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
