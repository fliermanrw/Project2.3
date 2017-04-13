package model.server_connection;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Map<String, String> getServerFromTxt() throws IOException {
        //Read file and change server and port when it is set in the ifle
        //File should be format key: "value"
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("settings.txt").getAbsoluteFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String currentLine;
        Map<String, String> vars = new HashMap<>();
        while ((currentLine = br.readLine()) != null) {
            Pattern pattern = Pattern.compile("([A-Za-z]+): \"([^\"]*)\"");
            Matcher matcher = pattern.matcher(currentLine);
            while (matcher.find()) {
                vars.put(matcher.group(1), matcher.group(2));
            }
        }
        return vars;
    }
}

