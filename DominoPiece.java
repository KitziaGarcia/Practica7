import java.util.ArrayList;

public class DominoPiece extends Piece implements Movible {
    private int leftValue;
    private int rightValue;

    public DominoPiece() {
        leftValue = 3;
        rightValue = 4;
    }

    public DominoPiece(int leftValue, int rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public int getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(int leftValue) {
        this.leftValue = leftValue;
    }

    public int getRightValue() {
        return rightValue;
    }

    public void setRightValue(int rightValue) {
        this.rightValue = rightValue;
    }

    @Override
    public int getSumOfSides() {
        return getLeftValue() + getRightValue();
    }

    @Override
    public boolean isValuePresent(int value) {
        return (value == getRightValue()) || (value == getLeftValue());
    }

    @Override
    public void displayTileInConsole(ArrayList<Integer> playableValue) {
        int leftValue = getLeftValue();
        int rightValue = getRightValue();

        if (playableValue.getFirst() == getLeftValue()) {
            System.out.println("   " + rightValue);
            System.out.println("   --");
            System.out.println("   " + leftValue);
        } else {
            System.out.println("   " + leftValue);
            System.out.println("   --");
            System.out.println("   " + rightValue);
        }

    }

    public String toString() {
        return "[" + getLeftValue() + ", " + getRightValue() + "]";
    }

    @Override
    public void rotateRight() {

    }

    @Override
    public void rotateLeft() {

    }
}