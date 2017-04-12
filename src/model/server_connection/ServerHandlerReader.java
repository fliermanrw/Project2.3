package model.server_connection;


import controller.GameView;
import controller.LoginController;
import controller.PreGameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private ArrayList<String> receivedMoves = new ArrayList<>();

    public ServerHandlerReader(Socket socket, Stage stage) {
        this.socket = socket;
        this.stage = stage;
    }

    @Override
    public void run() {
        String currentLine;
        String previousLine = "";
        try {
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while ((currentLine = reader.readLine()) != null) {
//                System.out.println("aaa");
//                //Process commands that are set by the ServerHandlerWriter
//                if(!ServerHandler.queue.empty()){
//                    //execute command
//                    currentCommand = ServerHandler.queue.pop();
//                    System.out.println(currentCommand);
//                    ServerHandler.log += currentCommand + "\n";
//                    bw.write(currentCommand);
//                    bw.newLine(); //==ENTER
//                    bw.flush();
//                }


//                //A string is ready to be read
//                if(reader.ready()){
//                    currentLine = reader.readLine();
                //Read + add every new line to the log
                System.out.println(currentLine);
                ServerHandler.log += currentLine + "\n";

                if (currentLine.contains("OK")) {
                    currentCommand = ServerHandler.queue.pop();
                    System.out.println(currentCommand);
                    ServerHandler.log += currentCommand + "\n";
                    if (currentCommand.contains("login")) {
                        String username = currentCommand.replaceAll("(login )", "");
                        System.out.println("username: " + username);
                        LoginController lc = new LoginController();
                        lc.setStage(this.stage);
                        lc.login();
                    }
                }


                if (currentLine.contains("PLAYERLIST")) {
                    String playerlist = currentLine.replaceAll("(\\[|\\SVR PLAYERLIST|\\]|\")", "");
                    List<String> players = Arrays.asList(playerlist.split(","));
                    currentController.setPlayerList(players);
                }

                if (currentLine.contains("CHALLENGE")) {
                    Map<String, String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                    System.out.println(vars);

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Challenge Request");
                        alert.setContentText("You have been challenged by " + vars.get("CHALLENGER") + " for a game of " + vars.get("GAMETYPE"));
                        alert.setOnHidden(e -> {
                            if (alert.getResult().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                                //Only accept challenge when button ok is pressed
                                String challengeNumber = (String) vars.get("CHALLENGENUMBER");
                                ServerHandlerWriter.acceptChallenge(challengeNumber);
                            }
                        });
                        alert.show();
                    });
                }

                // Match if a game is found
                if (currentLine.contains("MATCH")) {
                    Map<String, String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                    if (vars.get("PLAYERTOMOVE") != null) {
                        currentController.startGame(String.valueOf(vars.get("GAMETYPE")), String.valueOf(vars.get("PLAYERTOMOVE")));
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                        //@TODO PLEASE FIX THIS AS SOON AS POSSIBLE
                        //We should check if the controller is loaded
                        //This is a wasted second, coulnd't find another solution right now
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }


                if(currentLine.contains("SVR GAME YOURTURN") && previousLine.contains("SVR GAME MATCH ")){
                    System.out.println("IF 1");
                    //@todo object is not initalized so firstturn = null
                    currentGameView.ourturn();
                }
                if(currentLine.contains("SVR GAME YOURTURN") && previousLine.contains("SVR GAME MOVE ")){
                    System.out.println("IF 2, previousline is" + previousLine);
                    if(!receivedMoves.contains(previousLine)) {
                        //MAKE SURE ITS NOT A DUPLICATE MOVE
                        //@TODO CLEAR receivedMoves after game!!
                        //@todo maybe handle this in the controller?
                        receivedMoves.add(currentLine);

                        System.out.println("GameReader: we received a new move");
                        Map<String, String> vars = stringToMap(previousLine); //convert string to map with "Key": "Value" format

                        if (!String.valueOf(vars.get("PLAYER")).equals(ServerHandler.playerName)) {
                            System.out.println("Processing the turn of an opponent");
                            //The last move was made by the opponent
                            if (vars.get("MOVE") != null) {
                                currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
                            }
                        }else{
                            System.out.println("Hmm thats weird, it's our turn again after we made a turn");
                            //The lastmove was by us
                            //So it's probably a situation where there are no move left for our opponent
                            //Its ur turn again
                            currentGameView.ourTurnAgain();
                        }
                    }
                }

//                if (currentLine.contains("SVR GAME YOURTURN")) {
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                        //@todo remove this later, TAKES a second!!
//                        //@todo problem is that controller is not loaded? how can we check this?
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("previousline=" + previousLine);
//                    //Its our turn
//                        currentGameView.ourturn();
//                    if (previousLine.contains("SVR GAME MOVE")) {
//                        if(!receivedMoves.contains(previousLine)){
//                            //MAKE SURE ITS NOT A DUPLICATE MOVE
//                            //@TODO CLEAR receivedMoves after game!!
//                            //@todo maybe handle this in the controller?
//                            System.out.println("GameReader: we received a new move");
//                            Map<String, String> vars = stringToMap(previousLine); //convert string to map with "Key": "Value" format
//
//                            if (vars.get("MOVE") != null) {
//                                currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
//                            }
//                        }
//                    }
//                }

//                    if(previousLine.contains("SVR GAME MOVE")){
//                        System.out.println("GameReader: we received a new move");
//                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format
//
//                        if (vars.get("MOVE") != null) {
//                            currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
//                        }
//                    }

                previousLine = currentLine;
            }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("FUCK UIT DE WHILE LOOp");
    }

    /**
     * Converts string using the format {Key: "value"} to a hashmap
     *            First group: ([A-Za-z]+):  //Match any characert from A-Z or a-z, + means 1 or more, end with :
     *            So the first group matches every word that ends with :
     *            ---------------------------------------------------------------
     *            Second group: "([^"]*)"    //Match the ", then [^"] = match a single characer " that is not in the set,  * = between zero and more times, end with a " .
     *            So the second group matches everything between quotes and checks if it doesn't contain quotes itself?
     * @return Map Can be used as Map.get("KEY")
     */
    public Map<String,String> stringToMap(String line) {
        Map<String, String> vars = new HashMap<>();
        Pattern pattern = Pattern.compile("([A-Za-z]+): \"([^\"]*)\"");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            vars.put(matcher.group(1), matcher.group(2));
        }

        return vars;
    }
}
