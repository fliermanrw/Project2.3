package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by jouke on 30-3-2017.
 */
public class TelnetReader implements Runnable{
    Socket socket;
    public TelnetReader(Socket socket){
        this.socket = socket;
    }

    private void readfromSocket(Socket socket){
        String currentLine;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
                //@todo notify views?
                System.out.println(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        readfromSocket(socket);
    }
}
