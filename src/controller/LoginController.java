package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.TelnetWriter;

import javax.swing.*;
import java.io.IOException;

public class LoginController {
    TelnetWriter connectionWriter;
    @FXML TextField textField;
    @FXML RadioButton playAsBot;
    @FXML RadioButton playAsHuman;
    Stage stage;

    public void clickButton(){
        //Get name
        String playerName = textField.getText();

        //Get playing style
        if(playAsBot.isSelected()){
            //Play as a bot
        }else {
            //Play as a human
        }

        //Login
        connectionWriter.sendData("login " + playerName);
    }

    public void setConnectionWriter(TelnetWriter w){
        connectionWriter = w;
    }

    public void printError(String error){
        System.out.println(error);
    }

    public void login(){
        System.out.println("We are logged in to the server. So we are changing the view");

        //Perform this in the javafx thread
        Platform.runLater(()->{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/connectedView.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Connected");
            stage.setScene(new Scene(root, 300, 400));
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}
