package model.server_connection;


import controller.ConnectedController;
import controller.GameView;
import controller.LoginController;
import controller.PreGameView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


                    // If the server sends the playerlist
                    if(currentLine.contains("PLAYERLIST")){
                        String playerlist = currentLine.replaceAll("(\\[|\\SVR PLAYERLIST|\\]|\")", "");
                        List<String> players = Arrays.asList(playerlist.split(","));
                        currentController.setPlayerList(players);
                    }

                    // If another player challenges us
                    if (currentLine.contains("CHALLENGE")) {
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        System.out.println(vars);

                        // Call a confirmation screen with accept or cancel options
                        Platform.runLater(()->{
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Challenge Request");
                                alert.setContentText("You have been challenged by " + vars.get("CHALLENGER") + " for a game of " + vars.get("GAMETYPE"));

                            ButtonType buttonTypeAcceptPlayer = new ButtonType("ACCEPT AS PLAYER");
                            ButtonType buttonTypeAcceptBot = new ButtonType("ACCEPT AS BOT");
                            ButtonType buttonTypeCancel = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);

                            // Get the challengeNumber
                            String challengeNumber = (String) vars.get("CHALLENGENUMBER");

                            alert.getButtonTypes().setAll(buttonTypeAcceptPlayer, buttonTypeAcceptBot, buttonTypeCancel);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && (result.get() == buttonTypeAcceptPlayer)){
                                ServerHandlerWriter.acceptChallenge(challengeNumber);
                                ServerHandlerReader.useBot = false;
                                System.out.println("PLAYING AS PLAYER");
                            } else if (result.isPresent() && (result.get() == buttonTypeAcceptBot)) {
                                ServerHandlerWriter.acceptChallenge(challengeNumber);
                                ServerHandlerReader.useBot = true;
                                System.out.println("PLAYING AS BOT");

                            }
                        });
                    }

                    // Match if a game is found
                    if(currentLine.contains("MATCH")){
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        if (vars.get("PLAYERTOMOVE") != null) {
                            currentController.startGame(String.valueOf(vars.get("GAMETYPE")), String.valueOf(vars.get("PLAYERTOMOVE")));
                        }
                    }


                    // Receive a move from server
                    if(currentLine.contains("SVR GAME MOVE")){
                        System.out.println("GameReader: we received a new move");
                        Map<String,String> vars = stringToMap(currentLine); //convert string to map with "Key": "Value" format

                        if (vars.get("MOVE") != null) {
                            currentGameView.serverMove(Integer.valueOf(vars.get("MOVE")), String.valueOf(vars.get("PLAYER")));
                        }
                    }

                    // When game is ended. win/lose/draw options
                    if(currentLine.contains("SVR GAME LOSS") || (currentLine.contains("SVR GAME WIN")) || (currentLine.contains("SVR GAME DRAW"))){
                        // change String winLose to win, lost or draw
                        String winLose = "lost";
                        if(currentLine.contains("SVR GAME WIN")){
                            winLose = "won";
                        }else if(currentLine.contains("SVR GAME DRAW")){
                            winLose = "drawn";
                        }

                        System.out.println("Game is over.. we need to switch back to the Lobby");

                        String finalWinLose = winLose;

                        // Change the view to ConnectedView
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("You have " + finalWinLose + "!");
                            alert.setHeaderText("Do you want to review the game return to lobby?");
                            alert.setContentText("Choose from the buttons below");


                            ButtonType buttonTypeLobby = new ButtonType("Back to Lobby");
                            ButtonType buttonTypeCancel = new ButtonType("Cancel and see the game ", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(buttonTypeLobby, buttonTypeCancel);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && (result.get() == buttonTypeLobby)) {
                                System.out.println("LOBBY");

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
                                Parent root = null;
                                try {
                                    root = (Parent) fxmlLoader.load();

                                    //Set writer in controller
                                    ServerHandlerReader.currentController = fxmlLoader.<ConnectedController>getController();

                                } catch (IOException | ConcurrentModificationException e) {
                                    e.printStackTrace();
                                }
                                ServerHandlerReader.stage.setTitle("Lobby");
                                ServerHandlerReader.stage.setScene(new Scene(root, 300, 400));
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
