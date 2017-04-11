import controller.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.server_connection.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
            //TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);

            //Read file and change server and port when it is set in the ifle
            //File should be format key: "value"
            BufferedReader br = new BufferedReader(new FileReader(new File("settings.txt").getAbsoluteFile()));
            String currentLine;
            Map<String, String> vars = new HashMap<>();
            while ((currentLine = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("([A-Za-z]+): \"([^\"]*)\"");
                Matcher matcher = pattern.matcher(currentLine);
                while (matcher.find()) {
                    vars.put(matcher.group(1), matcher.group(2));
                }
            }
            System.out.println(vars);
            if(vars.get("server") != null){
                this.server = vars.get("server");
                System.out.printf("??");
            }
            if(vars.get("port") != null){
                this.port = Integer.valueOf(vars.get("port"));
            }

            // Local
            TelnetConnection telnet = new TelnetConnection(server, port);

            ServerHandler serverHandler = new ServerHandler(telnet.getConnectionSocket(), primaryStage);
            LoginController loginController = fxmlLoader.getController();
            loginController.setStage(primaryStage);

//            TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
//            Socket socket = telnet.getConnectionSocket();
//
//            //Create a telnet writer
//            connectionWriter = new TelnetWriter(socket);
//            //Create a telnet reader
//            connectionReader = new PreGameReader(socket);
//            Thread t1 = new Thread(connectionReader);
//            t1.start();
//
//            //Set writer & reader in controller
//            LoginController loginController = fxmlLoader.getController();
//            loginController.setConnectionWriter(connectionWriter);
//            loginController.setConnectionReader(connectionReader);
//            loginController.setSocket(socket);

            //Set stage in LoginController
//            loginController.setStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing, logging out");
                ServerHandlerWriter.writeSend("logout");
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
