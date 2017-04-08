package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.server_connection.ServerHandler;
import model.server_connection.ServerHandlerReader;
import model.server_connection.ServerHandlerWriter;
import model.server_connection.TelnetWriter;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ConcurrentModificationException;

public class LoginController extends PreGameView {
    TelnetWriter connectionWriter;
    @FXML
    TextField textField;
    @FXML
    RadioButton playAsBot;
    @FXML
    RadioButton playAsHuman;
    boolean succesfull = false;
    String playerName = null;

    public void loginButton() {
        //Get name
        String playerName = textField.getText();

        //Get playing style
        if (playAsBot.isSelected()) {
            //Play as a bot
        } else {
            //Play as a human
        }



        //Login
        ServerHandlerWriter.login(playerName);
        super.updateLog(ServerHandler.log);
        this.playerName = playerName;
    }

    @Override
    public void startGame(String game, String playerToMove) {

    }

    public void printError(String error) {
        System.out.println(error);
    }

    public void setPlayerName(String name){
        this.playerName = name;
    }

    public void login() {
        //Perform this in the javafx thread
        Platform.runLater(() -> {
            Stage stage = super.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();

                //Set writer in controller
                ConnectedController connectedController = fxmlLoader.getController();
                ServerHandlerReader.currentController = connectedController;
                connectedController.setPlayerName(playerName);
//                connectedController.setStage(super.getStage());
                // Remove this view from the views that get notified on updates from the reader
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException cme) {
                cme.printStackTrace();
            }
            stage.setTitle("Connected");
            stage.setScene(new Scene(root, 300, 400));
        });
    }
}
