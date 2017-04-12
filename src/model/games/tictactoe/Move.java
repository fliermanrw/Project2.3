package model.games.tictactoe;

/**
 * Created by Danny on 5-4-2017.
 */
public class Move {

    int index;
    int score;

    public Move(int i) {
        index = i;
        score = 0;
    }

    public void setScore(int s) {
        score = s;
    }

    public int getScore() {
        return score;
    }

    public int getIndex() {
        return index;
    }

    public void addScore(int s) {
        score += s;
    }

}
