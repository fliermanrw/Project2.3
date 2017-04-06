package model.server_connection;

import com.sun.corba.se.spi.activation.Server;
import controller.LoginController;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Stack;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandlerReader implements Runnable {
    public Socket socket;
    private String currentCommand;
    private Stage stage;

    public ServerHandlerReader(Socket socket, Stage stage) {
        this.socket = socket;
        this.stage = stage;
    }
    @Override
    public void run() {
        String currentLine;
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                //Process commands that are set by the ServerHandlerWriter
                if(!ServerHandler.queue.empty()){
                    //execute command
                    currentCommand = ServerHandler.queue.pop();
                    System.out.println(currentCommand);
                    ServerHandler.log += currentCommand + "\n";
                    bw.write(currentCommand);
                    bw.newLine(); //==ENTER
                    bw.flush();
                }


                //A string is ready to be read
                if(reader.ready()){
                    currentLine = reader.readLine();
                    //Read + add every new line to the log
                    System.out.println(currentLine);
                    ServerHandler.log += currentLine + "\n";

                    if(currentLine.contains("OK")){
                        if(currentCommand.contains("login")){
                            String username = currentCommand.replaceAll("(login )", "");
                            System.out.println(username);
                            LoginController lc = new LoginController();
                            lc.setStage(this.stage);
                            lc.setPlayerName(username);
                            lc.login();
                        }
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
