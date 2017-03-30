import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TelnetConnection;
import model.TelnetReader;
import model.TelnetWriter;

import java.net.Socket;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        //Connect to the Hanze server before going to the login view
        try {
            TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
            Socket socket = telnet.getConnectionSocket();

            //Create a telnet writer
            TelnetWriter w = new TelnetWriter(socket);

            //Set writer in controller
            LoginController loginController = fxmlLoader.getController();
            loginController.setConnectionWriter(w);

            //Set stage in LoginController
            loginController.setStage(primaryStage);

            //Create a telnet reader
            TelnetReader r = new TelnetReader(socket, loginController);
            Thread t1 = new Thread(r);
            t1.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
