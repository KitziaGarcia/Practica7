import java.util.ArrayList;

public class TridominoPiece extends Piece implements Movible {
    private int upperValue;
    private int leftValue;
    private int rightValue;
    private int orientation;

    public TridominoPiece() {
        upperValue = 1;
        leftValue = 1;
        rightValue = 1;
        orientation = 1;
    }

    public TridominoPiece(int upperValue, int leftValue, int rightValue, int orientation) {
        this.upperValue = upperValue;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.orientation = orientation;
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

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
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

    @Override
    public void displayTileOptionsInConsole() {
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
        System.out.println("  " + getLeftValue() + "  " + getRightValue() + "          " + getUpperValue() + "  " + getLeftValue() + "          " + getRightValue() + "  " + getUpperValue());
        System.out.println("Opcion 1." + "     Opcion 2." + "     Opcion 3.");
        System.out.println("\n  " + getRightValue() + "  " + getLeftValue() + "          " + getLeftValue() + "  " + getUpperValue() + "          " + getUpperValue() + "  " + getRightValue());
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
    }

    @Override
    public void displayTileInConsole(int orientation) {
        int upperValue = getUpperValue();
        int leftValue = getLeftValue();
        int rightValue = getRightValue();

        if (orientation == 1) {
            System.out.println("   " + upperValue);
            System.out.println("  " + leftValue + "  " + rightValue);
        } else {
            System.out.println("  " + rightValue + "  " + leftValue);
            System.out.println("   " + upperValue);
        }
    }

    public void setDisplayOrientation(ArrayList<Integer> playableValues, int positionIndicator) {
        if (positionIndicator == 1 || positionIndicator == 5) {
            if (playableValues.getFirst() == getLeftValue() && playableValues.getLast() == getRightValue()) {
                displayTileInConsole(1);
            } else if (playableValues.getFirst() == getUpperValue() && playableValues.getLast() == getLeftValue()) {
                rotateLeft();
                displayTileInConsole(1);
            } else if (playableValues.getFirst() == getRightValue() && playableValues.getLast() == getUpperValue()) {
                rotateRight();
                displayTileInConsole(1);
            } else {
                displayTileInConsole(1);
            }
            setOrientation(1);
        } else if (positionIndicator == 3) {
            if (playableValues.getFirst() == getUpperValue() && playableValues.getLast() == -1) {
                displayTileInConsole(2);
            } else if(playableValues.getFirst() == getRightValue() && playableValues.getLast() == -1) {
                rotateLeft();
                displayTileInConsole(2);
            } else if (playableValues.getFirst() == getLeftValue() && playableValues.getLast() == -1) {
                rotateRight();
                displayTileInConsole(2);
            } else {
                displayTileInConsole(2);
            }
            setOrientation(2);
        } else if (positionIndicator == -1) {
            setOrientation(1);
            displayTileInConsole(1);
        } else if (positionIndicator == -2) {
            setOrientation(2);
            displayTileInConsole(2);
        } else {
            setOrientation(1);
            displayTileInConsole(1);
        }
    }

    @Override
    public void rotateRight() {
        int temp = upperValue;
        upperValue = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

    @Override
    public void rotateLeft() {
        int temp = upperValue;
        upperValue = rightValue;
        rightValue = leftValue;
        leftValue = temp;
    }
}
