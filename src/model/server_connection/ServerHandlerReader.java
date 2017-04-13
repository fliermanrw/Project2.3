package model.server_connection;


import controller.GameView;
import controller.LoginController;
import controller.PreGameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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

                    if(currentLine.contains("OK")){
                        currentCommand = ServerHandler.queue.pop();
                        System.out.println(currentCommand);
                        ServerHandler.log += currentCommand + "\n";
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
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        System.out.println(vars);

                        Platform.runLater(()->{
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Challenge Request");
                                alert.setContentText("You have been challenged by " + vars.get("CHALLENGER") + " for a game of " + vars.get("GAMETYPE"));
                                alert.setOnHidden(e -> {
                                    if(alert.getResult().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)){
                                        //Only accept challenge when button ok is pressed
                                        String challengeNumber = (String) vars.get("CHALLENGENUMBER");
                                        ServerHandlerWriter.acceptChallenge(challengeNumber);
                                    }
                                });
                                alert.show();
                        });
                    }

                    // Match if a game is found
                    if(currentLine.contains("MATCH")){
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        if (vars.get("PLAYERTOMOVE") != null) {
                            currentController.startGame(String.valueOf(vars.get("GAMETYPE")), String.valueOf(vars.get("PLAYERTOMOVE")));
                        }
                    }



                    if(currentLine.contains("SVR GAME MOVE")){
                        System.out.println("GameReader: we received a new move");
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        if (vars.get("MOVE") != null) {
                            currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
                        }
                    }

                    if(currentLine.contains("SVR GAME LOSS") || (currentLine.contains("SVR GAME WIN"))){
                        // change String var to win or lost
                        String winLose = "lost";
                        if(currentLine.contains("SVR GAME WIN")){
                            winLose = "won";
                        }

                        System.out.println("Game is over.. we need to switch back to the Lobby");

                        String finalWinLose = winLose;
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("You have " + finalWinLose + "!");
                            alert.setHeaderText("Do you want to do a rematch or return to lobby?");
                            alert.setContentText("Choose from the buttons below");

                            ButtonType buttonTypeRematch = new ButtonType("Rematch!");
                            ButtonType buttonTypeLobby = new ButtonType("Back to Lobby");
                            ButtonType buttonTypeCancel = new ButtonType("Cancel and see how I " + finalWinLose, ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(buttonTypeRematch, buttonTypeLobby, buttonTypeCancel);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && (result.get() == buttonTypeRematch)){
                                //TODO immediately ask for rematch
                                System.out.println("REMATCH");
                            } else if (result.isPresent() && (result.get() == buttonTypeLobby)) {
                                //TODO call function to switch view to lobby
                                System.out.println("LOBBY");
                            }
                        });

                    }



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
