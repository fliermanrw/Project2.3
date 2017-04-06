package model.server_connection;

/**
 * Created by jouke on 6-4-2017.
 */
public interface Commands {
    void login(String playerName);
    void logout();
    void get(String data); //gamelist or playerlist
    void subscribe(String game);
    void move(int index);
    void forfeit();
    void challenge(String player, String game);
}
