package model.server_connection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandlerWriter{
    Socket socket;
    static BufferedWriter writer;

    public ServerHandlerWriter(Socket s){
        this.socket = s;
        try {
            writer= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSend(String data){
        ServerHandler.queue.add(data);
        try {
            writer.write(data);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void login(String playerName) {
        ServerHandlerWriter.writeSend("login " + playerName);
    }

    public static void getPlayerList(){
        ServerHandlerWriter.writeSend("get playerlist");
    }

    public static void help() {
        ServerHandlerWriter.writeSend("help");
    }

    public static void acceptChallenge(String challengeNumber) {
        ServerHandlerWriter.writeSend("challenge accept " + challengeNumber);
    }

    public static void denyChallenge(String challengeNumber) {
        ServerHandlerWriter.writeSend("challenge deny " + challengeNumber);
    }
}
