import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private ArrayList<DominoPiece> domino;
    private ArrayList<TridominoPiece> tridomino;
    private ArrayList<Object> boneyard;
    private ArrayList<Piece> player1Tiles;
    private ArrayList<Piece> player2Tiles;
    private ArrayList<Piece> usedTiles;
    ClearScreen clear = new ClearScreen();
    Scanner input = new Scanner(System.in);
    private Piece foundPiece;
    //private TridominoPiece foundTridomino;
    private int turn;
    private boolean firstTurn;
    private ArrayList<Integer> dominoPlayableValue;
    private ArrayList<Integer> tridominoPlayableValues;
    Piece startingPiecePlayer1 = null;
    Piece startingPiecePlayer2 = null;
    Piece pieceInTable = null;


    public Game(int player1TotalDominoes, int player1TotalTridominoes, int player2TotalDominoes, int player2TotalTridominoes) {
        initializeGame(player1TotalDominoes, player1TotalTridominoes, player2TotalDominoes, player2TotalTridominoes);
        setFirstTurn(true);
        getStartingTile();
        play();
    }

    public void play() {
        System.out.println("testing");


        //System.out.println("testing");
    }

    public void initializeGame(int player1TotalDominoes, int player1TotalTridominoes, int player2TotalDominoes, int player2TotalTridominoes) {
        Domino domino = new Domino();
        domino.shuffle();

        Tridomino tridomino = new Tridomino();
        tridomino.shuffle();

        player1Tiles = new ArrayList<>();
        player2Tiles = new ArrayList<>();

        for (int i = 0; i < player1TotalDominoes; i++) {
            player1Tiles.add(domino.getPiece(0));
            domino.removePiece(0);
        }

        for (int i = 0; i < player1TotalTridominoes; i++) {
            player1Tiles.add(tridomino.getPiece(0));
            tridomino.removePiece(0);
        }

        for (int i = 0; i < player2TotalDominoes; i++) {
            player2Tiles.add(domino.getPiece(0));
            domino.removePiece(0);
        }

        for (int i = 0; i < player2TotalTridominoes; i++) {
            player2Tiles.add(tridomino.getPiece(0));
            tridomino.removePiece(0);
        }

        showPlayerTiles();

        tridominoPlayableValues = new ArrayList<>(Arrays.asList(0,0));
        dominoPlayableValue = new ArrayList<>();
        dominoPlayableValue.add(0);
        usedTiles = new ArrayList<>();
    }

    public void showPlayerTiles() {
        System.out.println("\nFichas jugador 1: " + player1Tiles);
        pressEnterToContinue();

        System.out.println("Fichas jugador 2: " + player2Tiles);
        pressEnterToContinue();
    }

    public void getStartingTile() {
        int opc;
        System.out.println("\nJugador 1 escoga una ficha para comenzar: " + player1Tiles);

        System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
        opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

        if (opc == 1) {
            selectDomino();
        } else {
            selectTridomino();
        }

        System.out.println("\nJugador 2 escoga una ficha para comenzar: " + player2Tiles);
        System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
        opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

        if (opc == 1) {
            selectDomino();
        } else {
            selectTridomino();
        }

        System.out.println("Ficha seleccionada jugador 1: ");
        if (getStartingPiecePlayer1() instanceof DominoPiece) {
            getStartingPiecePlayer1().displayTileInConsole(dominoPlayableValue);
        } else {
            getStartingPiecePlayer2().displayTileInConsole(tridominoPlayableValues);
        }

        System.out.println("Ficha seleccionada jugador 2: ");
        if (getStartingPiecePlayer2() instanceof DominoPiece) {
            getStartingPiecePlayer2().displayTileInConsole(dominoPlayableValue);
        } else {
            getStartingPiecePlayer2().displayTileInConsole(tridominoPlayableValues);
        }

        if (getStartingPiecePlayer1().getSumOfSides() > getStartingPiecePlayer2().getSumOfSides()) {
            setPieceInTable(getStartingPiecePlayer1());
            player1Tiles.remove(getStartingPiecePlayer1());
            usedTiles.add(getStartingPiecePlayer1());

        } else if (getStartingPiecePlayer1().getSumOfSides() < getStartingPiecePlayer2().getSumOfSides()) {
            setPieceInTable(getStartingPiecePlayer2());
            player2Tiles.remove(getStartingPiecePlayer2());
            usedTiles.add(getStartingPiecePlayer2());
        } else {
            System.out.println("Las piezas tienen el mismo valor, seleccionen de nuevo.");
            getStartingTile();
            return;
        }

        System.out.println(player1Tiles);
        System.out.println(player2Tiles);
        System.out.println("USED: " + usedTiles);

        if (getPieceInTable() instanceof TridominoPiece) {
            System.out.println("\n\nSeleccione como quiere poner la ficha: ");
            ((TridominoPiece) getPieceInTable()).displayTileOptionsInConsole();

            int opt = Utilities.isInputValid(input, "Opcion 4." + "     Opcion 5." + "     Opcion 6.", 1, 6);
            setTridominoPlayableValues(opt);
        }

        System.out.println("PLAYABLE VALUES: " + getTridominoPlayableValues());
        setFirstTurn(false);
    }

    public void selectDomino() {
        this.foundPiece = null;
        int leftValue;
        int rightValue;
        int turn = getTurn();
        boolean isPlayable = false;
        Domino domino = new Domino();
        DominoPiece selectedPiece;

        do {
            System.out.println("Seleccione ficha a utilizar: ");
            leftValue = Utilities.isInputValid(input, "Valor izquierdo: ", 0, 6);
            rightValue = Utilities.isInputValid(input, "Valor derecho: ", 0, 6);
            System.out.println();
            selectedPiece = new DominoPiece(leftValue, rightValue);
            this.foundPiece = isTilePresent(selectedPiece, turn, 0, leftValue, rightValue);

            if (isFirstTurn()) {
                if (turn == 0) {
                    setStartingPiecePlayer1((DominoPiece) this.foundPiece);
                    setTurn(1);
                } else {
                    setStartingPiecePlayer2((DominoPiece) this.foundPiece);
                    setTurn(0);
                }
            } else {
                if (turn == 0) {
                    setTurn(1);
                } else {
                    setTurn(0);
                }
            }

                /*if (!isFirstTurn() && foundPiece != null) {
                    isPlayable = isTheValuePlayable(((DominoPiece) foundPiece).getLeftValue(), ((DominoPiece) foundPiece).getRightValue());

                    if (!isPlayable) {
                        System.out.println("La ficha seleccionada no es jugable. Por favor, elige otra ficha.");
                        System.out.println();
                        foundPiece = null;
                    }
                }*/
        } while (foundPiece == null);
    }

    public void selectTridomino() {
        this.foundPiece = null;
        int value1;
        int value2;
        int value3;
        int turn = getTurn();
        boolean isPlayable = false;
        Tridomino tridomino = new Tridomino();
        TridominoPiece selectedPiece;

        do {
            System.out.println("Seleccione ficha a utilizar: ");
            value1 = Utilities.isInputValid(input, "Valor 1: ", 0, 5);
            value2 = Utilities.isInputValid(input, "Valor 2: ", 0, 5);
            value3 = Utilities.isInputValid(input, "Valor 3: ", 0, 5);
            System.out.println();
            selectedPiece = new TridominoPiece(value1, value2, value3);
            this.foundPiece = isTilePresent(selectedPiece, turn, value1, value2, value3);

            if (isFirstTurn()) {
                if (turn == 0) {
                    setStartingPiecePlayer1((TridominoPiece) this.foundPiece);
                    setTurn(1);
                } else {
                    setStartingPiecePlayer2((TridominoPiece) this.foundPiece);
                    setTurn(0);
                }
            } else {
                if (turn == 0) {
                    setTurn(1);
                } else {
                    setTurn(0);
                }
            }

                /*if (!isFirstTurn() && foundPiece != null) {
                    isPlayable = isTheValuePlayable(((DominoPiece) foundPiece).getLeftValue(), ((DominoPiece) foundPiece).getRightValue());

                    if (!isPlayable) {
                        System.out.println("La ficha seleccionada no es jugable. Por favor, elige otra ficha.");
                        System.out.println();
                        foundPiece = null;
                    }
                }*/
        } while (foundPiece == null);
    }

    /*public TridominoPiece getFoundTridomino() {

    }*/

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    public int getDominoPlayableValue() {
        return dominoPlayableValue.getFirst();
    }

    public void setDominoPlayableValue(int dominoPlayableValue) {
        this.dominoPlayableValue.set(0, dominoPlayableValue);
    }

    public Piece getStartingPiecePlayer1() {
        return startingPiecePlayer1;
    }

    public void setStartingPiecePlayer1(Piece startingPiecePlayer1) {
        this.startingPiecePlayer1 = startingPiecePlayer1;
    }

    public Piece getStartingPiecePlayer2() {
        return startingPiecePlayer2;
    }

    public void setStartingPiecePlayer2(Piece startingPiecePlayer2) {
        this.startingPiecePlayer2 = startingPiecePlayer2;
    }

    public Piece getPieceInTable() {
        return pieceInTable;
    }

    public void setPieceInTable(Piece pieceInTable) {
        this.pieceInTable = pieceInTable;
    }

    public ArrayList<Integer> getTridominoPlayableValues() {
        return tridominoPlayableValues;
    }

    public void setTridominoPlayableValues(int opt) {
        TridominoPiece piece = (TridominoPiece) getPieceInTable();

        switch (opt) {
            case 1:
                tridominoPlayableValues.set(0, piece.getLeftValue());
                tridominoPlayableValues.set(1, piece.getRightValue());
                break;
            case 2:
                tridominoPlayableValues.set(0, piece.getUpperValue());
                tridominoPlayableValues.set(1, piece.getLeftValue());
                break;
            case 3:
                tridominoPlayableValues.set(0, piece.getRightValue());
                tridominoPlayableValues.set(1, piece.getUpperValue());
                break;
            case 4:
                tridominoPlayableValues.set(0, piece.getUpperValue());
                tridominoPlayableValues.set(1, -1);
                break;
            case 5:
                tridominoPlayableValues.set(0, piece.getRightValue());
                tridominoPlayableValues.set(1, -1);
                break;
            case 6:
                tridominoPlayableValues.set(0, piece.getLeftValue());
                tridominoPlayableValues.set(1, -1);
                break;
        }

    }

    public Piece isTilePresent(Piece piece, int turn, int upperValue, int leftValue, int rightValue) {
        Domino domino = new Domino();
        Tridomino tridomino = new Tridomino();

        if (piece instanceof DominoPiece && turn == 0) {
            foundPiece = domino.isTilePresent(player1Tiles, leftValue, rightValue);
        } else if (piece instanceof DominoPiece && turn == 1) {
            foundPiece = domino.isTilePresent(player2Tiles, leftValue, rightValue);
        } else if (piece instanceof TridominoPiece && turn == 0) {
            foundPiece = tridomino.isTilePresent(player1Tiles, upperValue, leftValue, rightValue);
        } else if (piece instanceof TridominoPiece && turn == 1) {
            foundPiece = tridomino.isTilePresent(player2Tiles, upperValue, leftValue, rightValue);
        }
        return foundPiece;
    }

    public void displayUsedDominoesInconsole() {
        //for (Piece )
    }

    /*public boolean isTheValuePlayable(int leftValue, int rightValue) {
        for (int i = 0; i < playableValues.size(); i++) {
            if (leftValue == playableValues.get(i) || rightValue == playableValues.get(i)) {
                return true;
            }
        }
        return false;
    }*/

    public void pressEnterToContinue() {
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}
