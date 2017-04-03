package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import model.TelnetReader;
import model.TelnetWriter;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectedController extends AbstractView{
    TelnetWriter connectionWriter;
    @FXML RadioButton reversi;
    @FXML ComboBox opponentSelection;
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
    public void login() {

    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void setConnectionWriter(TelnetWriter w) {
        connectionWriter = w;
    }

    public void logout(){
        System.out.println("Logged out of the server");
        connectionWriter.sendData("logout");
    }

    /**
     * Get all possible commands from server
     */
    public void help(){
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
        connectionWriter.sendData("subscribe " + game);
        System.out.println("Tried to subcsribe;");
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
