package controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.TelnetWriter;

import javax.swing.*;

public class LoginController {
    TelnetWriter connectionWriter;
    @FXML TextField textField;
    @FXML RadioButton playAsBot;
    @FXML RadioButton playAsHuman;

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
        //gets called by server
        System.out.println("We are logged in to the server. Change view?");
    }

}
