package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.server_connection.TelnetWriter;

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

    public void clickButton() {
        //Get name
        String playerName = textField.getText();

        //Get playing style
        if (playAsBot.isSelected()) {
            //Play as a bot
        } else {
            //Play as a human
        }

        //Login
        connectionWriter.sendData("login " + playerName);
        super.updateLog("login " + playerName);
    }

    public void setConnectionWriter(TelnetWriter w) {
        connectionWriter = w;
    }

    @Override
    public void startGame(String game) {

    }

    public void printError(String error) {
        System.out.println(error);
    }

    public void login() {
//        System.out.println("We are logged in to the server. So we are changing the view");

        //Remove this view from the views that get notified on updates from the reader
//        connectionReader.removeView(this); //@todo can throw concurrentmodificationexeption

        //Perform this in the javafx thread
        Platform.runLater(() -> {
            Stage stage = super.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();

                //Set writer in controller
                ConnectedController connectedController = fxmlLoader.getController();
                connectedController.setConnectionWriter(connectionWriter);
                connectedController.setConnectionReader(super.getConnectionReader());
                connectedController.setSocket(super.getSocket());
                connectedController.setStage(super.getStage());
                // Remove this view from the views that get notified on updates from the reader
                connectionReader.removeView(this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException cme) {
                System.out.println("Test");
                cme.printStackTrace();
            }
            stage.setTitle("Connected");
            stage.setScene(new Scene(root, 300, 400));
        });
    }

    @Override
    public void setSuccesfull(boolean status) {
        succesfull = status;
        //Last command was succesfull
        login();
    }

}
