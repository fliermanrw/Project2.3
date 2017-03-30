package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetWriter implements Runnable{
    Socket socket;

    public TelnetWriter(Socket socket){
        this.socket = socket;
    }

    public void sendData(String data){
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(data);
            bw.newLine(); //==ENTER
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("writer thread");
        sendData("test");
    }
}
