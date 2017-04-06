import controller.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.server_connection.PreGameReader;
import model.server_connection.TelnetConnection;
import model.server_connection.TelnetWriter;

import java.net.Socket;

public class Main extends Application {
    TelnetWriter connectionWriter;
    PreGameReader connectionReader;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        //Connect to the Hanze server before going to the login view
        try {
            TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
            Socket socket = telnet.getConnectionSocket();

            //Create a telnet writer
            connectionWriter = new TelnetWriter(socket);
            //Create a telnet reader
            connectionReader = new PreGameReader(socket);
            Thread t1 = new Thread(connectionReader);
            t1.start();

            //Set writer & reader in controller
            LoginController loginController = fxmlLoader.getController();
            loginController.setConnectionWriter(connectionWriter);
            loginController.setConnectionReader(connectionReader);
            loginController.setSocket(socket);

            //Set stage in LoginController
            loginController.setStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing, logging out");
                connectionWriter.sendData("logout");
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
