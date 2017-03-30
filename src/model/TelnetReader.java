package model;

import controller.LoginController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetReader implements Runnable{
    Socket socket;
    LoginController loginController;

    public TelnetReader(Socket socket, LoginController loginController){
        this.socket = socket;
        this.loginController = loginController;
    }

    private void readfromSocket(Socket socket, LoginController loginController){
        String currentLine;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
                if(currentLine.contains("ERR")){
                    String error = currentLine.replaceAll("(\\bERR\\b)", ""); //remove ERR from string
                    loginController.printError("Server tells us:" + error);
                }
                if(currentLine.contains("OK")){
                    //Logged in
                    loginController.login();
                }
                //@todo notify views?
                //@todo maybe pass controler to this class?
                System.out.println(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        readfromSocket(socket, loginController);
    }
}
