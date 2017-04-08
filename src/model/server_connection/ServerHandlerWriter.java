package model.server_connection;

import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by jouke on 6-4-2017.
 */
public class ServerHandlerWriter{

    public static void writeSend(String data){
        ServerHandler.queue.add(data);
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
}
