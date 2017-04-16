import controller.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.server_connection.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    public String server = "127.0.0.1"; //default server
    public Integer port = 7789; //default port

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        //Connect to the Hanze server before going to the login view
        try {

            // Remote
          //  TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
            // get the server and port data from the txt file
           Map<String, String> vars = TelnetConnection.getServerFromTxt();

            if(vars.get("server") != null){
                this.server = vars.get("server");
            }
            if(vars.get("port") != null){
                this.port = Integer.valueOf(vars.get("port"));
            }

            // Local
            TelnetConnection telnet = new TelnetConnection(server, port);

            ServerHandler serverHandler = new ServerHandler(telnet.getConnectionSocket(), primaryStage);
            LoginController loginController = fxmlLoader.getController();
            loginController.setStage(primaryStage);
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, 200, 150));
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Stage is closing, logging out");
                    ServerHandlerWriter.writeSend("logout");
                }
            });
            primaryStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection to server failed");
            alert.setContentText("Failed to connect to the server located at: " + "\n" + "server: " + server + "\n" + "port: " + port);
            alert.show();
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
