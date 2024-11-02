import java.util.ArrayList;

public class TridominoPiece extends Piece implements Movible {
    private int upperValue;
    private int leftValue;
    private int rightValue;

    public TridominoPiece() {
        upperValue = 1;
        leftValue = 1;
        rightValue = 1;
    }

    public TridominoPiece(int upperValue, int leftValue, int rightValue) {
        this.upperValue = upperValue;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public int getUpperValue() {
        return upperValue;
    }

    public void setUpperValue(int upperValue) {
        this.upperValue = upperValue;
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
        return getUpperValue() + getLeftValue() + getRightValue();
    }

    @Override
    public boolean isValuePresent(int value) {
        return (value == getUpperValue()) || (value == getLeftValue()) || (value == getRightValue());
    }

    public String toString() {
        return "[" + getUpperValue() + ", " + getLeftValue() + ", " + getRightValue() + "]";
    }

    public void displayTileOptionsInConsole() {
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
        System.out.println("  " + getLeftValue() + "  " + getRightValue() + "          " + getUpperValue() + "  " + getLeftValue() + "          " + getRightValue() + "  " + getUpperValue());
        System.out.println("Opcion 1." + "     Opcion 2." + "     Opcion 3.");
        System.out.println("\n  " + getRightValue() + "  " + getLeftValue() + "          " + getLeftValue() + "  " + getUpperValue() + "          " + getUpperValue() + "  " + getRightValue());
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
    }

    @Override
    public void displayTileInConsole(ArrayList<Integer> playableValues) {
        int upperValue = getUpperValue();
        int leftValue = getLeftValue();
        int rightValue = getRightValue();

        if (playableValues.getFirst() == getLeftValue() && playableValues.getLast() == getRightValue()) {
            System.out.println("   " + upperValue);
            System.out.println("  " + leftValue + "  " + rightValue);
        } else if (playableValues.getFirst() == getUpperValue() && playableValues.getLast() == getLeftValue()) {
            System.out.println("   " + rightValue);
            System.out.println("  " + upperValue + "  " + leftValue);
        } else if (playableValues.getFirst() == getRightValue() && playableValues.getLast() == getUpperValue()) {
            System.out.println("   " + leftValue);
            System.out.println("  " + rightValue + "  " + upperValue);
        } else if (playableValues.getFirst() == getUpperValue() && playableValues.getLast() == -1) {
            System.out.println("  " + rightValue + "  " + leftValue);
            System.out.println("   " + upperValue);
        } else if(playableValues.getFirst() == getRightValue() && playableValues.getLast() == -1) {
            System.out.println("  " + leftValue + "  " + upperValue);
            System.out.println("   " + rightValue);
        } else if (playableValues.getFirst() == getLeftValue() && playableValues.getLast() == -1) {
            System.out.println("  " + upperValue + "  " + rightValue);
            System.out.println("   " + leftValue);
        } else {
            System.out.println("   " + upperValue);
            System.out.println("  " + leftValue + "  " + rightValue);
        }
    }

    @Override
    public void rotateRight() {

    }

    @Override
    public void rotateLeft() {

    }
}
