package model.server_connection;

import com.sun.corba.se.spi.activation.Server;
import controller.ConnectedController;
import controller.GameView;
import controller.LoginController;
import controller.PreGameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandlerReader implements Runnable {
    public Socket socket;
    private String currentCommand;
    public static Stage stage;
    public static PreGameView currentController;
    public static GameView currentGameView;
    public static boolean useBot = false;

    public ServerHandlerReader(Socket socket, Stage stage) {
        this.socket = socket;
        this.stage = stage;
    }

    @Override
    public void run() {
        String currentLine;
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                //Process commands that are set by the ServerHandlerWriter
                if(!ServerHandler.queue.empty()){
                    //execute command
                    currentCommand = ServerHandler.queue.pop();
                    System.out.println(currentCommand);
                    ServerHandler.log += currentCommand + "\n";
                    bw.write(currentCommand);
                    bw.newLine(); //==ENTER
                    bw.flush();
                }


                //A string is ready to be read
                if(reader.ready()){
                    currentLine = reader.readLine();
                    //Read + add every new line to the log
                    System.out.println(currentLine);
                    ServerHandler.log += currentLine + "\n";

                    if(currentLine.contains("OK")){
                        if(currentCommand.contains("login")){
                            String username = currentCommand.replaceAll("(login )", "");
                            System.out.println("username: " + username);
                            LoginController lc = new LoginController();
                            lc.setStage(this.stage);
                            lc.login();
                        }
                    }



                    if(currentLine.contains("PLAYERLIST")){
                        String playerlist = currentLine.replaceAll("(\\[|\\SVR PLAYERLIST|\\]|\")", "");
                        List<String> players = Arrays.asList(playerlist.split(","));
                        currentController.setPlayerList(players);
                    }

                    if (currentLine.contains("CHALLENGE")) {
                        String line = currentLine;
                        line = line.replaceAll("(SVR GAME CHALLENGE )", ""); //remove SVR GAME MATCH
                        line = line.replaceAll("(\"|-)", "");//remove quotations and -
                        line = line.replaceAll("(\\w+)", "\"$1\""); //add quotations to every word
                        JSONParser parser = new JSONParser();
                        try {
                            JSONObject json = (JSONObject) parser.parse(line);
                            Platform.runLater(()->{
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Challenge Request");
                                alert.setContentText("You have been challenged by " + json.get("CHALLENGER") + " for a game of " + json.get("GAMETYPE"));
                                alert.setOnHidden(e -> {
                                    String challengeNumber = (String) json.get("CHALLENGENUMBER");
                                    ServerHandlerWriter.acceptChallenge(challengeNumber);
                                });
                                alert.show();
                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // Match if a game is found
                    if(currentLine.contains("MATCH")){
                        System.out.println("stage: " + stage);
                        String line = currentLine;
                        line = line.replaceAll("(SVR GAME MATCH )", ""); //remove SVR GAME MATCH
                        line = line.replaceAll("(\"|-)", "");//remove quotations and -
                        line = line.replaceAll("(\\w+)", "\"$1\""); //add quotations to every word
                        JSONParser parser = new JSONParser();
                        try {
                            JSONObject json = (JSONObject) parser.parse(line);
                            currentController.startGame(String.valueOf(json.get("GAMETYPE")), String.valueOf(json.get("PLAYERTOMOVE")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if(currentLine.contains("SVR GAME MOVE")){
                        System.out.println("GameReader: we received a new move");
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
                        System.out.println("vars" + vars.get("MOVE") + vars.get("PLAYER"));
                        if (vars.get("MOVE") != null) {
                            currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
                        }

//                        System.out.println(Integer.valueOf(vars.get("MOVE")));
    //                    for(GameView v : views){
    //                            v.serverMove(Integer.valueOf(vars.get("MOVE")));//dummy data is index 1
    //                    }
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
