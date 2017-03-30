package model;

import java.io.*;
import java.net.Socket;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetConnection{
    String ip;
    int port;
    Socket socket;

    public TelnetConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.socket = openSocket(ip, port);

        //Create a telnet writer
        TelnetWriter w = new TelnetWriter(socket);
        Thread t2 = new Thread(w);
        t2.start();

        //Create a telnet reader
        TelnetReader r = new TelnetReader(socket);
        Thread t1 = new Thread(r);
        t1.start();
    }

    //Socket only has to be opened once
    private Socket openSocket(String ip, int port){
        try {
            return new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
