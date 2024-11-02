import java.util.ArrayList;
import java.util.Collections;

public class Tridomino {

    private ArrayList<TridominoPiece> tridomino;
    private int higherValue;

    public Tridomino() {
        higherValue = 5;
        tridomino = new ArrayList<>();

        for (int i = 0; i <= higherValue; i++) {
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k <= j; k++) {
                    TridominoPiece newPiece = new TridominoPiece(i, j, k);
                    tridomino.add(newPiece);
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(tridomino);
    }

    // Returns the domino piece at a specified index `i` from the list.
    public TridominoPiece getPiece(int i) {
        return tridomino.get(i);
    }

    // Removes a domino piece at the specified index `i` from the list.
    public void removePiece(int i) {
        tridomino.remove(i);
    }

    // Searches the list for a tridomino piece with the specified value1, value2, and value3.
// It returns the piece if found, otherwise prints a message and returns null.
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

                // Check all possible permutations of the values
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
        System.out.println("No tiene esa ficha.");
        return null;
    }



}