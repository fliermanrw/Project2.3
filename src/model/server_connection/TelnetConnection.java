package model.server_connection;

import java.io.*;
import java.net.Socket;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetConnection{
    String ip;
    int port;
    Socket socket;

    public TelnetConnection(String ip, int port) throws Exception{
        this.ip = ip;
        this.port = port;
        this.socket = openSocket(ip, port);
    }

    //Socket only has to be opened once
    private Socket openSocket(String ip, int port) throws Exception{
            return new Socket(ip, port);
    }

    public Socket getConnectionSocket(){
        return socket;
    }
}
