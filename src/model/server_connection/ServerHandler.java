package model.server_connection;

import javafx.stage.Stage;
import java.net.Socket;
import java.util.Stack;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandler {
    public static Stack<String> queue = new Stack<>();
    public static String log;
    public static String playerName;
//
//    public static void main(String[] args) {
//        try {
//            TelnetConnection telnet = new TelnetConnection("145.33.225.170", 7789);
//            new ServerHandler(telnet.getConnectionSocket());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public ServerHandler(Socket socket, Stage stage){
        Thread t1 = new Thread(new ServerHandlerReader(socket, stage));
        ServerHandlerWriter test = new ServerHandlerWriter();
        t1.start();
        //ServerHandlerWriter.help();

    }
}
