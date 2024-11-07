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
            if (playableValues.getFirst() == this.leftValue) {
                rotateRight();
            } else if (playableValues.getFirst() == this.rightValue) {
                rotateLeft();
            }
            this.orientation = 1;
        } else if (positionIndicator == 3) {
            if (playableValues.getFirst() == getUpperValue() && playableValues.getLast() == rightValue) {
                rotateRight();
            } else if (playableValues.getFirst() == getLeftValue() && playableValues.getLast() == upperValue) {
                rotateLeft();
            }
            setOrientation(2);
        } else if (positionIndicator == -1) {
            setOrientation(1);
        } else if (positionIndicator == -2) {
            setOrientation(2);
        } else {
            setOrientation(1);
        }
    }

    public int getIndicatorForTwoDisplayOptions(ArrayList<Integer> playableValues) {
        int c = 0;
        if (upperValue == playableValues.getFirst() && leftValue == playableValues.getFirst()) {
            c = 1;
        } else if (upperValue == playableValues.getFirst() && rightValue == playableValues.getFirst()) {
            c = 2;
        } else if (rightValue == playableValues.getFirst() && leftValue == playableValues.getFirst()) {
            c = 3;
        }
        return c;
    }

    public void showTwoDisplayOptions(ArrayList<Integer> playableValues, int indicator) {
        if (indicator == 1) {
            System.out.println("   " + getUpperValue() + "             " + getLeftValue());
            System.out.println("  " + getLeftValue() + "  " + getRightValue() + "          " + getRightValue() + "  " + getUpperValue());
        } else if (indicator == 2) {
            System.out.println("   " + getUpperValue() + "             " + getRightValue());
            System.out.println("  " + getLeftValue() + "  " + getRightValue() + "          " + getUpperValue() + "  " + getLeftValue());
        } else if (indicator == 3) {
            System.out.println("   " + getRightValue() + "             " + getLeftValue());
            System.out.println("  " + getUpperValue() + "  " + getLeftValue() + "          " + getRightValue() + "  " + getUpperValue());
        }
    }

    public boolean isTriple() {
        return (this.leftValue == this.rightValue) && (this.leftValue == this.upperValue);
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
