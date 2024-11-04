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

    public int getSize() {
        return domino.size();
    }


    // Prints the list of domino pieces to the console.
    public void printDominoInConsole() {
        System.out.println(domino.toString());
    }

    // Shuffles the domino set randomly using the `Collections.shuffle` method.
    public void shuffle() {
        Collections.shuffle(domino);
    }

    // Returns the domino piece at a specified index `i` from the list.
    public DominoPiece getPiece(int i) {
        return domino.get(i);
    }

    // Removes a domino piece at the specified index `i` from the list.
    public void removePiece(int i) {
        domino.remove(i);
    }

    // Returns the entire list of domino pieces.
    public ArrayList<DominoPiece> getTiles() {
        return domino;
    }

    // Searches the list for a domino piece with the specified leftValue and rightValue.
    // It returns the piece if found, otherwise prints a message and returns null.
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
        System.out.println("No tiene esa ficha.");
        return null;
    }

    // Compares two domino pieces to check if they are equal.
    // Returns true if both the left and right values of the pieces are identical.
    public boolean areTilesEqual(DominoPiece piece1, DominoPiece piece2) {
        //return piece1.equals(piece2);
        return (piece1.getLeftValue() == piece2.getLeftValue()) && (piece1.getRightValue() == piece2.getRightValue());
    }
}