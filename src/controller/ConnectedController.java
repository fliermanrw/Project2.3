package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import model.TelnetReader;
import model.TelnetWriter;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectedController extends AbstractView{
    TelnetWriter connectionWriter;
    @FXML RadioButton reversi;
    @FXML ComboBox opponentSelection;
    @FXML TextArea logArea;
    boolean succesfull = false;
    String command = "no command yet"; //@todo create command class

    public void clickButton(){
        System.out.println("test");
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

    public void setLog(String log){
        logArea.clear();
        logArea.appendText(log);
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
        super.updateLog("subscribe" + game);
        connectionWriter.sendData("subscribe " + game);
    }

    public void getOpponents(){
        connectionWriter.sendData("get playerlist"); //ask for a playerlist
    }

    public void updateOpponentSelection(){
        if(super.getPlayerList()!=null){
//            opponentSelection.getItems().clear();
            for(String player: super.getPlayerList()){
                opponentSelection.getItems().add("test");
            }
            System.out.println(super.getPlayerList().size());
        }
    }
}
