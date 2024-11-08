import java.util.ArrayList;
import java.util.Collections;

/**
 * The Tridomino class represents a set of tridomino pieces for a tridomino game.
 * It initializes a collection of tridomino pieces based on the higher value
 * specified during construction. The class provides methods to shuffle the
 * pieces, retrieve individual pieces, remove pieces, print the collection
 * to the console, and check for the presence or equality of domino pieces.
 */
public class Tridomino {

    private ArrayList<TridominoPiece> tridomino;
    private int higherValue;

    public Tridomino() {
        higherValue = 5;
        tridomino = new ArrayList<>();

        for (int i = 0; i <= higherValue; i++) {
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    TridominoPiece newPiece = new TridominoPiece(i, j, k, 0);
                    tridomino.add(newPiece);
                }
            }
        }
    }

    /**
     * Shuffles the domino set randomly using the `Collections.shuffle` method.
     */
    public void shuffle() {
        Collections.shuffle(tridomino);
    }

    /**
     * Getter for the size of the tridomino set.
     * @return the size.
     */
    public int getSize() {
        return tridomino.size();
    }

    /**
     * Returns the tridomino piece at a specified index `i` from the list.
     * @param i the specified index.
     * @return the piece in that index.
     */
    public TridominoPiece getPiece(int i) {
        return tridomino.get(i);
    }

    /**
     * Removes a tridomino piece at the specified index `i` from the list.
     * @param i the specified index.
     */
    public void removePiece(int i) {
        tridomino.remove(i);
    }

    /**
     * Searches the list for a tridomino piece with the specified upper value, leftValue and rightValue.
     * @return the piece if found, otherwise prints a message and returns null.
     */
    public TridominoPiece isTilePresent(ArrayList<Piece> tridomino, int value1, int value2, int value3) {
        TridominoPiece foundPiece = new TridominoPiece();

        for (Piece piece : tridomino) {
            if (piece instanceof DominoPiece) {
                continue;
            } else {
                foundPiece = (TridominoPiece) piece;
                int upperValue = foundPiece.getUpperValue();
                int leftValue = foundPiece.getLeftValue();
                int rightValue = foundPiece.getRightValue();

                if ((upperValue == value1 && leftValue == value2 && rightValue == value3) ||
                        (upperValue == value1 && leftValue == value3 && rightValue == value2) ||
                        (upperValue == value2 && leftValue == value1 && rightValue == value3) ||
                        (upperValue == value2 && leftValue == value3 && rightValue == value1) ||
                        (upperValue == value3 && leftValue == value1 && rightValue == value2) ||
                        (upperValue == value3 && leftValue == value2 && rightValue == value1)) {
                    return foundPiece;
                }
            }
        }
        System.out.println("No tiene esa ficha.\n");
        return null;
    }
}