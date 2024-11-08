import java.util.ArrayList;

public class TridominoPiece extends Piece implements Movible {
    private int upperValue;
    private int leftValue;
    private int rightValue;
    private int orientation;

    /**
     * Initializes a TridominoPiece with default values of 1 for each side.
     */
    public TridominoPiece() {
        upperValue = 1;
        leftValue = 1;
        rightValue = 1;
        orientation = 1;
    }

    /**
     * Initializes a TridominoPiece with specified values for each side.
     * @param upperValue the specified upper value.
     * @param leftValue the specified left value.
     * @param rightValue the specified right value.
     * @param orientation the specified orientation.
     */
    public TridominoPiece(int upperValue, int leftValue, int rightValue, int orientation) {
        this.upperValue = upperValue;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.orientation = orientation;
    }

    /**
     * Returns the value of the upper side of the piece.
     * @return the upper value.
     */
    public int getUpperValue() {
        return upperValue;
    }

    /**
     * Returns the value of the left side of the piece.
     * @return the left value.
     */
    public int getLeftValue() {
        return leftValue;
    }

    /**
     * Returns the value of the right side of the piece.
     * @return the right value.
     */
    public int getRightValue() {
        return rightValue;
    }

    /**
     * Returns the orientation of the piece.
     * @return the value of the orientation.
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of the piece to the specified value.
     * @param orientation the specified orientation to set.
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Calculates the sum of all three sides of the piece.
     * @return the sum of the three sides.
     */
    @Override
    public int getSumOfSides() {
        return getUpperValue() + getLeftValue() + getRightValue();
    }

    /**
     * Returns a string representation of the piece showing the values of its three sides.
     * @return string representation of the piece.
     */
    public String toString() {
        return "[" + getUpperValue() + ", " + getLeftValue() + ", " + getRightValue() + "]";
    }

    /**
     * Displays six different orientation options for the piece in the console.
     */
    @Override
    public void displayTileOptionsInConsole() {
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
        System.out.println("  " + getLeftValue() + "  " + getRightValue() + "          " + getUpperValue() + "  " + getLeftValue() + "          " + getRightValue() + "  " + getUpperValue());
        System.out.println("Opcion 1." + "     Opcion 2." + "     Opcion 3.");
        System.out.println("\n  " + getRightValue() + "  " + getLeftValue() + "          " + getLeftValue() + "  " + getUpperValue() + "          " + getUpperValue() + "  " + getRightValue());
        System.out.println("   " + getUpperValue() + "             " + getRightValue() + "             " + getLeftValue());
    }

    /**
     * Displays the piece in the console according to the specified orientation.
     * @param orientation the specified orientation.
     */
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

    /**
     * Sets the display orientation of the piece based on the playable values and position indicator.
     * @param playableValues current playable values.
     * @param positionIndicator the position indicator of the piece.
     */
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

    /**
     * Returns an indicator for two different display options based on playable values.
     * @param playableValues the current playable values.
     * @return the indicator.
     */
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

    /**
     * Displays two orientation options in the console based on the specified indicator.
     * @param indicator the indicator.
     */
    public void showTwoDisplayOptions(int indicator) {
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

    /**
     * Checks if all three sides of the piece have the same value.
     * @return true if the three sides are the same, false otherwise.
     */
    public boolean isTriple() {
        return (this.leftValue == this.rightValue) && (this.leftValue == this.upperValue);
    }

    /**
     * Rotates the piece to the right, updating the values of each side.
     */
    @Override
    public void rotateRight() {
        int temp = upperValue;
        upperValue = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

    /**
     * Rotates the piece to the left, updating the values of each side.
     */
    @Override
    public void rotateLeft() {
        int temp = upperValue;
        upperValue = rightValue;
        rightValue = leftValue;
        leftValue = temp;
    }
}
