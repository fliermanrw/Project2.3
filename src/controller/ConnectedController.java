package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import model.server_connection.TelnetWriter;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectedController extends PreGameView implements Initializable {
    TelnetWriter connectionWriter;
    @FXML RadioButton bke;
    @FXML RadioButton reversi;
    @FXML ComboBox opponentSelection;
    @FXML TextArea logArea;
    @FXML ToggleGroup test;
    private String selectedOpponent;
    boolean succesfull = false;
    String command = "no command yet"; //@todo create command class

    public void clickButton(){
        System.out.println("test");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()-> {
            getOpponents();
        });
        updateOpponentSelection();
    }

    @Override
    public void setSuccesfull(boolean status) {
        succesfull = status;
        System.out.println("ConnectedController: Last executed command was succesfull");
        opponentSelection.getItems().add("test");
        updateOpponentSelection();
    }

    @Override
    public void setConnectionWriter(TelnetWriter w) {
        connectionWriter = w;
    }

    public void logout(){
        System.out.println("Logged out of the server");
        super.updateLog("logout");
        connectionWriter.sendData("logout");
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
        connectionWriter.sendData("help");
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
        connectionWriter.sendData("subscribe " + game);
    }

    public void getOpponents(){
        connectionWriter.sendData("get playerlist"); //ask for a playerlist

    }

    private void updateOpponentSelection(){
        if(super.getPlayerList()!=null){
//            opponentSelection.getItems().clear();
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
        connectionWriter.sendData("challenge " + "\"" + selectedOpponent.replace(" ", "") +"\""  + " " + "\"" + selectedGame + "\"");
    }

    public TelnetWriter getConnectionWriter() {
        return connectionWriter;
    }

}
