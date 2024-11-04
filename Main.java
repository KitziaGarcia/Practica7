import java.util.Scanner;

/*
   Agregar que la mula se ponga horizontal.
   Checa el metodo para detectar si un jugador tiene movimientos.
   Hacer que coman 2 si no tienen movimientos y si el bonyard no esta empty.
 */

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int player1TotalDominoes = 0;
        int player1TotalTridominoes = 0;
        int player2TotalDominoes = 0;
        int player2TotalTridominoes = 0;

        do {
            System.out.println("Jugador 1: ");
            player1TotalDominoes = Utilities.isInputValid(input, "Ingrese la cantidad de dominos a tomar: ",0, 10);
            player1TotalTridominoes = Utilities.isInputValid(input, "\nIngrese la cantidad de tridominos a tomar: ",0, 10);
            System.out.println();
        } while ((player1TotalDominoes + player1TotalTridominoes != 10));


        do {
            System.out.println("Jugador 2: ");
            player2TotalDominoes = Utilities.isInputValid(input, "Ingrese la cantidad de dominos a tomar: ",0, 10);
            player2TotalTridominoes = Utilities.isInputValid(input, "\nIngrese la cantidad de tridominos a tomar: ",0, 10);
        } while (player2TotalDominoes + player2TotalTridominoes != 10);

        Game game = new Game(player1TotalDominoes, player1TotalTridominoes, player2TotalDominoes, player2TotalTridominoes);
    }
}
