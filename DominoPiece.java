import java.util.ArrayList;

public class DominoPiece extends Piece implements Movible {
    private int leftValue;
    private int rightValue;
    private int orientation;

    public DominoPiece() {
        leftValue = 3;
        rightValue = 4;
        orientation = 1;
    }

    public DominoPiece(int leftValue, int rightValue, int orientation) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.orientation = orientation;
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
        return getLeftValue() + getRightValue();
    }

    @Override
    public boolean isValuePresent(int value) {
        return (value == getRightValue()) || (value == getLeftValue());
    }

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

    @Override
    public void setDisplayOrientation(ArrayList<Integer> playableValues,  int positionIndicator) {
        int leftValue = getLeftValue();
        int rightValue = getRightValue();
        if (positionIndicator == 0 || positionIndicator == 1 || positionIndicator == 4) {
            if (playableValues.getFirst() == leftValue) {
                displayTileInConsole(1);
            } else if (playableValues.getFirst() == rightValue) {
                rotateLeft();
                displayTileInConsole(1);
            } else {
                displayTileInConsole(1);
            }

            setOrientation(1);
        } else if (positionIndicator == 2) {
            if (playableValues.getFirst() == leftValue && playableValues.getLast() == rightValue) {
                displayTileInConsole(2);
            } else if (playableValues.getFirst() == rightValue && playableValues.getLast() == leftValue) {
                rotateRight();
                displayTileInConsole(2);
            }  else {
                displayTileInConsole(2);
            }
            setOrientation(2);
        } else if (positionIndicator == -1) {
            setOrientation(1);
            displayTileInConsole(1);
        } else {
            System.out.println(toString());
        }


    }

    public String toString() {
        return "[" + getLeftValue() + ", " + getRightValue() + "]";
    }

    @Override
    public void displayTileOptionsInConsole() {
        System.out.println("   " + leftValue + "       " + rightValue);
        System.out.println("   --      --");
        System.out.println("   " + rightValue + "       " + leftValue);
    }

    @Override
    public void rotateRight() {
        int temp = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

    @Override
    public void rotateLeft() {
        int temp = leftValue;
        leftValue = rightValue;
        rightValue = temp;
    }

}