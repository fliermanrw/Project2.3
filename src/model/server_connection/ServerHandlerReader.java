package model.server_connection;

import com.sun.corba.se.spi.activation.Server;
import controller.ConnectedController;
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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandlerReader implements Runnable {
    public Socket socket;
    private String currentCommand;
    public static Stage stage;
    public static PreGameView currentController;

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
                            lc.setPlayerName(username);
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
                                    // @todo Redirect naar nieuwe view (Othello of BKE). Gebruik json.get("GAMETYPE")
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

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
