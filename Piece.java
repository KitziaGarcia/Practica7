import java.util.ArrayList;

public abstract class Piece implements Movible {

    public abstract int getSumOfSides();
    public abstract boolean isValuePresent(int value);
    public abstract void displayTileInConsole(ArrayList<Integer> playableValues);
}