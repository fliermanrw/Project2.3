import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TelnetConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Connect to the Hanze server before going to the login view
        TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
        launch(args);
    }
}
