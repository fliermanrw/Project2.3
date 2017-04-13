package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerReader;
import model.server_connection.ServerHandlerWriter;

import java.io.IOException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;

public class ConnectedController extends PreGameView implements Initializable {
    @FXML RadioButton bke;
    @FXML RadioButton reversi;
    @FXML ComboBox opponentSelection;
    @FXML TextArea logArea;
    @FXML ToggleGroup test;
    @FXML Label loggedInAs;
    private String selectedOpponent;
    boolean succesfull = false;
    String command = "no command yet"; //@todo create command class

    public void clickButton(){
        System.out.println("test");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // @todo: EVEN LEZEN!
        // @todo We moeten timelines gebruiken, omdat bij het initialiseren getOpponents() wordt aangeroepen voor de
        // @todo get playerlist commando. Vervolgens moet de thread (ServerHandlerReader.java) deze afhandelen en de
        // @todo players eruit filteren om ze in deze superclass te zetten. Omdat het een thread is, is de thread soms
        // @todo eerder of later. Dit zorgt voor unexpected behaviour (soms null values bij super.getPlayerList() en soms niet).
        // @todo Daarom heb ik een soort Task of Keyframe systeem toegevoegd om te zorgen dat ze altijd in de juiste
        // @todo volgorde worden afgehandeld. (Misschien kan dit ook naar de startGame() method verplaatst worden. Even overleggen).
        //Platform.runLater(()-> {
            //updateOpponentSelection();
            //System.out.println("Playerlist22 " + super.getPlayerList());
        //});
        // Create keyframes and use a timeline to run the tasks after each other
        final KeyFrame kf1 = new KeyFrame(Duration.millis(0), e -> getOpponents());
        final KeyFrame kf2 = new KeyFrame(Duration.millis(500), e ->  updateOpponentSelection());
        final Timeline timeline = new Timeline(kf1, kf2);
        Platform.runLater(timeline::play);

        loggedInAs.setText("Logged in as: " + ServerHandler.playerName);
    }

    @Override
    public void startGame(String game, String playerToMove) {
        System.out.println("startgame wel aangeroepen?");
        System.out.println(game);
        System.out.println(playerToMove);
        //change view and start game controller of certain game
        Platform.runLater(() -> {
            if (game.equals("Tic-tac-toe")) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/tictactoeGrid.fxml"));
                Parent root = null;
                try {
                    root = (Parent) fxmlLoader.load();

                    //Set writer in controller
                    TictactoeController tictactoeController = fxmlLoader.getController();
                    ServerHandlerReader.currentGameView = tictactoeController;

                    //Notify if we have to start or opponent is starting with a move, after this the gamereader will handle everything
                    if (playerToMove.equals(ServerHandler.playerName)) {
                        tictactoeController.ourturn();
                    }

                    ServerHandlerReader.currentController = null;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ConcurrentModificationException cme) {
                    //System.out.println("Test");
                    cme.printStackTrace();
                }
                ServerHandlerReader.stage.setTitle("Our playername = " + ServerHandler.playerName);
                ServerHandlerReader.stage.setScene(new Scene(root, 800, 800));

            } else if (game.equals("Reversi")) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/othelloGrid.fxml"));
                Parent root = null;
                try {
                    root = (Parent) fxmlLoader.load();

                    //Set writer in controller
                    OthelloController othelloController = fxmlLoader.getController();
                    ServerHandlerReader.currentGameView = othelloController;

//                    othelloController.init();
                    if (playerToMove.equals(ServerHandler.playerName)) {
                        othelloController.ourturn();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ConcurrentModificationException cme) {
                    cme.printStackTrace();
                }
                ServerHandlerReader.stage.setTitle("Our playername = " + ServerHandler.playerName);
                ServerHandlerReader.stage.setScene(new Scene(root, 800, 800));
            }


        });}



    public void logout(){
        System.out.println("Logged out of the server");
        super.updateLog("logout");
        ServerHandlerWriter.writeSend("logout");

       // and finally close the stage
       ServerHandlerReader.stage.close();

    }

    public void updateLog(String currentLine){
        super.updateLog(currentLine);
        logArea.clear();
        logArea.appendText(super.getLog());
    }

    /**
     * Get all possible commands from server
     */
    public void help(){
        super.updateLog("help");
    }

    /**
     * Subscribe to a game
     * Usage Subscribe <game> @ server
     */
    public void subscribe(){
        String game = "";
        if(reversi.isSelected()){
            game = "Reversi";
        }else{
            game = "Tic-tac-toe";
        }

        //Options server-sided are "Reversi" and "Tic-tac-toe"
        super.updateLog("subscribe " + game);
        ServerHandlerWriter.writeSend("subscribe " + game);
    }

    public void getOpponents(){
        ServerHandlerWriter.getPlayerList();
    }

    private void updateOpponentSelection(){
        if(super.getPlayerList()!=null){
            opponentSelection.getItems().clear();
            for(String player: super.getPlayerList()){
                opponentSelection.getItems().add(player);
            }
            System.out.println(super.getPlayerList().size());
        }
    }

    public void getOpponentIndividual() {
        selectedOpponent = opponentSelection.getSelectionModel().getSelectedItem().toString();
    }

    public void invPlayer() {
        String selectedGame = "";
        if (bke.isSelected()) {
            selectedGame = "Tic-tac-toe";
        } else if (reversi.isSelected()) {
            selectedGame = "Reversi";
        }
        if(selectedOpponent != null){
            logArea.setText("Invited " + selectedOpponent + " for a challenge!");
            ServerHandlerWriter.writeSend("challenge " + "\"" + selectedOpponent.replace(" ", "") +"\""  + " " + "\"" + selectedGame + "\"");

        } else {
            logArea.setText("Select an opponent please..");
        }

    }
}
