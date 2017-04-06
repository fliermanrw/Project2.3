package model.games.tictactoe;

/**
 * Created by Danny on 5-4-2017.
 */
public class Move {

    int index;
    int score;

    public Move(int i) {
        index = i;
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

}
