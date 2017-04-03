package othello;

/**
 * Created by Cyriel on 2-4-2017.
 */
public enum Turn {
    BLACK, WHITE;

    private static Turn[] vals = values();

    public Turn next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
