import java.util.ArrayList;

public class DominoPiece extends Piece implements Movible {
    private int leftValue;
    private int rightValue;
    private int orientation;

    /**
     * Initializes a DominoPiece with default values of 1 for each side.
     */
    public DominoPiece() {
        leftValue = 1;
        rightValue = 1;
        orientation = 1;
    }

    /**
     * Initializes a DominoPiece with specified values for each side.
     * @param leftValue the specified left value.
     * @param rightValue the specified right value.
     * @param orientation the specified orientation.
     */
    public DominoPiece(int leftValue, int rightValue, int orientation) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.orientation = orientation;
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
     * Calculates the sum of all sides of the piece.
     * @return the sum of the sides.
     */
    @Override
    public int getSumOfSides() {
        return getLeftValue() + getRightValue();
    }

    /**
     * Displays the piece in the console according to the specified orientation.
     * @param orientation the specified orientation.
     */
    @Override
    public void displayTileInConsole(int orientation) {
        if (orientation == 1) {
            System.out.println("   " + leftValue);
            System.out.println("   --");
            System.out.println("   " + rightValue);
        } else {
            System.out.println("  " + getLeftValue() + "| " + getRightValue());
        }
    }

    /**
     * Sets the display orientation of the piece based on the playable values and position indicator.
     * @param playableValues current playable values.
     * @param positionIndicator the position indicator of the piece.
     */
    @Override
    public void setDisplayOrientation(ArrayList<Integer> playableValues,  int positionIndicator) {
        int leftValue = getLeftValue();
        int rightValue = getRightValue();
        if (positionIndicator == 0 || positionIndicator == 1 || positionIndicator == 4) {
            if (playableValues.getFirst() == rightValue) {
                rotateLeft();
            }
            setOrientation(1);
        } else if (positionIndicator == 2) {
            if (playableValues.getFirst() == rightValue && playableValues.getLast() == leftValue) {
                rotateRight();
            }
            setOrientation(2);
        } else if (positionIndicator == -1) {
            setOrientation(1);
        }
    }

    /**
     * Returns a string representation of the piece showing the values of both sides.
     * @return string representation of the piece.
     */
    public String toString() {
        return "[" + getLeftValue() + ", " + getRightValue() + "]";
    }

    /**
     * Displays two different orientation options for the piece in the console.
     */
    @Override
    public void displayTileOptionsInConsole() {
        System.out.println("   " + leftValue + "       " + rightValue);
        System.out.println("   --      --");
        System.out.println("   " + rightValue + "       " + leftValue);
    }

    /**
     * Checks if all sides of the piece have the same value.
     * @return true if the sides are the same, false otherwise.
     */
    public boolean isDouble() {
        return this.leftValue == this.rightValue;
    }

    /**
     * Rotates the piece to the right, updating the values of each side.
     */
    @Override
    public void rotateRight() {
        int temp = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

    /**
     * Rotates the piece to the left, updating the values of each side.
     */
    @Override
    public void rotateLeft() {
        int temp = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

}