import java.util.ArrayList;

public abstract class Piece implements Movible {

    public abstract int getSumOfSides();
    public abstract void displayTileInConsole(int orientation);
    public abstract void setDisplayOrientation(ArrayList<Integer> playableValues,  int positionIndicator);
    public abstract void displayTileOptionsInConsole();
    public abstract int getOrientation();
}