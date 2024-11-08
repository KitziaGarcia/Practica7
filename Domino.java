import java.util.ArrayList;
import java.util.Collections;

/**
 * The Domino class represents a set of domino pieces for a domino game.
 * It initializes a collection of domino pieces based on the highest double
 * specified during construction. The class provides methods to shuffle the
 * pieces, retrieve individual pieces, remove pieces, print the collection
 * to the console, and check for the presence or equality of domino pieces.
 */
public class Domino
{
    private ArrayList<DominoPiece> domino;
    private int higherDouble;

    public Domino() {
        higherDouble = 6;
        domino = new ArrayList<>();
        int value;

        for (int i = 0; i <= higherDouble; i++) {
            value = i;
            for (int j = 0; j <= value; j++) {
                DominoPiece newPiece = new DominoPiece(i, j, 0);
                domino.add(newPiece);
            }
        }
    }

    /**
     * Getter for the size of the domino set.
     * @return the size.
     */
    public int getSize() {
        return domino.size();
    }

    /**
     * Shuffles the domino set randomly using the `Collections.shuffle` method.
     */
    public void shuffle() {
        Collections.shuffle(domino);
    }

    /**
     * Returns the domino piece at a specified index `i` from the list.
     * @param i the specified index.
     * @return the piece in that index.
     */
    public DominoPiece getPiece(int i) {
        return domino.get(i);
    }

    /**
     * Removes a domino piece at the specified index `i` from the list.
     * @param i the specified index.
     */
    public void removePiece(int i) {
        domino.remove(i);
    }

    /**
     * Returns he entire list of domino pieces.
     * @returnthe domino pieces.
     */
    public ArrayList<DominoPiece> getTiles() {
        return domino;
    }

    /**
     * Searches the list for a domino piece with the specified leftValue and rightValue.
     * @return the piece if found, otherwise prints a message and returns null.
     */
    public DominoPiece isTilePresent(ArrayList<Piece> domino, int leftValue, int rightValue) {
        DominoPiece foundPiece = new DominoPiece();
        for (Piece piece : domino) {
            if (piece instanceof TridominoPiece) {
                continue;
            } else {
                foundPiece = (DominoPiece) piece;
                if ((foundPiece.getLeftValue() == leftValue && foundPiece.getRightValue() == rightValue) || (foundPiece.getLeftValue() == rightValue && foundPiece.getRightValue() == leftValue)) {
                    return foundPiece;
                }
            }
        }
        System.out.println("No tiene esa ficha.\n");
        return null;
    }
}