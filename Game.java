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
    private ArrayList<Integer> playableValues;
    Piece startingPiecePlayer1 = null;
    Piece startingPiecePlayer2 = null;
    Piece pieceInTable = null;
    private int positionIndicator;
    private ArrayList<Integer> orientation;


    public Game(int player1TotalDominoes, int player1TotalTridominoes, int player2TotalDominoes, int player2TotalTridominoes) {
        initializeGame(player1TotalDominoes, player1TotalTridominoes, player2TotalDominoes, player2TotalTridominoes);
        setFirstTurn(true);
        getStartingTile();
        play();
    }

    public void play() {
        boolean moreMovesForPlayer;
        int winnerIndex;
        setPositionIndicator(-1);
        System.out.println("USED: " + usedTiles);
        System.out.println("ORIENTATION: " + orientation);
        System.out.println("PLAYABLE VALUES: " + playableValues);

        if (noMoreMovesInGame()) {
            System.out.println("\nJuego terminado.");
            winnerIndex = getWinner(player1Tiles, player2Tiles);

            if (winnerIndex == -1) {
                System.out.print("Empate, ambos tienen la misma cantidad de piezas.");
            } else {
                System.out.print("Ha ganado el jugador " + (winnerIndex + 1));
                return;
            }
        }

        System.out.println("\nTURNO JUGADOR " + (getTurn() + 1) + ":");
        if ((getTurn() + 1) == 0) {
            moreMovesForPlayer = moreMovesForPlayer(player1Tiles);
        } else {
            moreMovesForPlayer = moreMovesForPlayer(player2Tiles);
        }

        if (!moreMovesForPlayer) {
            System.out.println("No tienes mas movimientos, pasas automaticamente.");
            pressEnterToContinue();
            setNextTurn();
            play();
            return;
        }

        selectTileType();
        displayUsedDominoesInConsole();
        if (getPieceInTable() instanceof DominoPiece) {
            setDominoPlayableValues();
        } else {
            setTridominoPlayableValues();
        }

        setNextTurn();
        play();
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

        playableValues = new ArrayList<>(Arrays.asList(0,0));
        usedTiles = new ArrayList<>();
        orientation = new ArrayList<>();
    }

    public void showPlayerTiles() {
        System.out.println("\nFichas jugador 1: " + player1Tiles);
        pressEnterToContinue();

        System.out.println("Fichas jugador 2: " + player2Tiles);
        pressEnterToContinue();
    }

    public void getStartingTile() {
        int opc;
        System.out.println("\nJugador 1: " + player1Tiles);

        System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
        opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

        if (opc == 1) {
            selectDomino();
        } else {
            selectTridomino();
        }

        setNextTurn();

        System.out.println("\nJugador 2: " + player2Tiles);
        System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
        opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

        if (opc == 1) {
            selectDomino();
        } else {
            selectTridomino();
        }

        System.out.println("Ficha seleccionada jugador 1: ");
        if (getStartingPiecePlayer1() instanceof DominoPiece) {
            getStartingPiecePlayer1().setDisplayOrientation(playableValues, -1);
        } else {
            getStartingPiecePlayer1().setDisplayOrientation(playableValues, -1);
        }

        System.out.println("Ficha seleccionada jugador 2: ");
        if (getStartingPiecePlayer2() instanceof DominoPiece) {
            getStartingPiecePlayer2().setDisplayOrientation(playableValues, -1);
        } else {
            getStartingPiecePlayer2().setDisplayOrientation(playableValues, -1);
        }

        if (getStartingPiecePlayer1().getSumOfSides() > getStartingPiecePlayer2().getSumOfSides()) {
            System.out.println("\nComienza el jugador 1 porque sumó " + getStartingPiecePlayer1().getSumOfSides() + " puntos.\n\n");
            setTurn(0);
            pressEnterToContinue();
            setPieceInTable(getStartingPiecePlayer1());
            System.out.println("PIECE IN TABLE: " + getPieceInTable());
            player1Tiles.remove(getStartingPiecePlayer1());

        } else if (getStartingPiecePlayer1().getSumOfSides() < getStartingPiecePlayer2().getSumOfSides()) {
            System.out.println("\nComienza el jugador 2 porque sumó " + getStartingPiecePlayer2().getSumOfSides() + " puntos.\n\n");
            setTurn(1);
            pressEnterToContinue();
            setPieceInTable(getStartingPiecePlayer2());
            player2Tiles.remove(getStartingPiecePlayer2());
        } else {
            System.out.println("Las piezas tienen el mismo valor, seleccionen de nuevo.");
            getStartingTile();
            return;
        }

        if (getPieceInTable() instanceof TridominoPiece) {
            System.out.println("\n\nSeleccione como quiere poner la ficha: ");
            ((TridominoPiece) getPieceInTable()).displayTileOptionsInConsole();
            int opt = Utilities.isInputValid(input, "Opcion 4." + "     Opcion 5." + "     Opcion 6.", 1, 6);

            switch(opt) {
                case 1:
                    setPositionIndicator(-1);
                    break;
                case 2:
                    setPositionIndicator(-1);
                    getPieceInTable().rotateLeft();
                    break;
                case 3:
                    setPositionIndicator(-1);
                    getPieceInTable().rotateRight();
                    break;
                case 5:
                    setPositionIndicator(-2);
                    getPieceInTable().rotateLeft();
                    break;
                case 6:
                    setPositionIndicator(-2);
                    getPieceInTable().rotateRight();
                    break;
            }

            setTridominoPlayableValues();
        } else {
            System.out.println("\n\nSeleccione como quiere poner la ficha: ");
            ((DominoPiece) getPieceInTable()).displayTileOptionsInConsole();
            int opt = Utilities.isInputValid(input, "Opcion 1." + "     Opcion 2.", 1, 2);

            setPositionIndicator(-1);

            if (opt == 2) {
                getPieceInTable().rotateLeft();
            }

            setDominoPlayableValues();
        }

        usedTiles.add(getPieceInTable());
        orientation.add(getPieceInTable().getOrientation());
        System.out.println("PLAYABLE VALUES: " + getPlayableValues());
        setFirstTurn(false);
        setNextTurn();
    }

    public void selectTileType() {
        int opt;

        if ((getTurn() + 1) == 1) {
            System.out.println("\nFichas: " + player1Tiles);
        } else {
            System.out.println("\nFichas: " + player2Tiles);
        }

        System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
        opt = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

        if (opt == 1) {
            selectDomino();
        } else {
            selectTridomino();
        }
    }

    public void selectDomino() {
        setFoundPiece(null);
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
            selectedPiece = new DominoPiece(leftValue, rightValue, 0);
            setFoundPiece(isTilePresent(selectedPiece, turn, 0, leftValue, rightValue));

            if (isFirstTurn()) {
                if (turn == 0) {
                    setStartingPiecePlayer1((DominoPiece) getFoundPiece());
                } else {
                    setStartingPiecePlayer2((DominoPiece) getFoundPiece());
                }
            }

            if (!isFirstTurn() && getFoundPiece() != null) {
                isPlayable = isTheValuePlayable(getFoundPiece());

                if (!isPlayable) {
                    System.out.println("La ficha seleccionada no es jugable. Por favor, elige otra ficha.");
                    System.out.println();
                    setFoundPiece(null);
                }
            }
        } while (getFoundPiece() == null);

        if (!firstTurn) {
            usedTiles.add(getFoundPiece());
            orientation.add(getFoundPiece().getOrientation());
            setPieceInTable(getFoundPiece());

            if (getTurn() == 0) {
                player1Tiles.remove(getFoundPiece());
            } else {
                player2Tiles.remove(getFoundPiece());
            }
        }
    }

    public void selectTridomino() {
        setFoundPiece(null);
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
            selectedPiece = new TridominoPiece(value1, value2, value3, 0);
            setFoundPiece(isTilePresent(selectedPiece, turn, value1, value2, value3));

            if (isFirstTurn()) {
                if (turn == 0) {
                    setStartingPiecePlayer1((TridominoPiece) getFoundPiece());
                } else {
                    setStartingPiecePlayer2((TridominoPiece) getFoundPiece());
                }
            }

            if (!isFirstTurn() && getFoundPiece() != null) {
                isPlayable = isTheValuePlayable(getFoundPiece());

                if (!isPlayable) {
                    System.out.println("La ficha seleccionada no es jugable. Por favor, elige otra ficha.");
                    System.out.println();
                    setFoundPiece(null);
                }
            }

        } while (foundPiece == null);

        if (!firstTurn) {
            usedTiles.add(getFoundPiece());
            orientation.add(getFoundPiece().getOrientation());
            setPieceInTable(getFoundPiece());

            if (getTurn() == 0) {
                player1Tiles.remove(getFoundPiece());
            } else {
                player2Tiles.remove(getFoundPiece());
            }
        }
    }

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

    public ArrayList<Integer> getPlayableValues() {
        return playableValues;
    }

    public void setPlayableValues(ArrayList<Integer> playableValues) {
        this.playableValues = playableValues;
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

    public Piece getFoundPiece() {
        return foundPiece;
    }

    public void setFoundPiece(Piece foundPiece) {
        this.foundPiece = foundPiece;
    }

    public int getPositionIndicator() {
        return positionIndicator;
    }

    public void setPositionIndicator(int positionIndicator) {
        this.positionIndicator = positionIndicator;
    }

    /**
     * Advances to the next player's turn, cycling back to the first player if necessary.
     */
    public void setNextTurn() {
        this.turn++;

        if (this.turn > 1) {
            this.turn = 0;
        }
    }

    public void setTridominoPlayableValues() {
        System.out.println("PIECE IN TABLE: " + getPieceInTable());
        TridominoPiece piece = (TridominoPiece) getPieceInTable();

        switch (getPositionIndicator()) {
            case -1, 1, 5:
                playableValues.set(0, piece.getLeftValue());
                playableValues.set(1, piece.getRightValue());
                break;
            case -2, 0, 2:
                playableValues.set(0, piece.getUpperValue());
                playableValues.set(1,-1);
                break;
        }
    }

    public void setDominoPlayableValues() {
        DominoPiece piece = (DominoPiece) getPieceInTable();

        switch (getPositionIndicator()) {
            case -1, 0, 4:
                playableValues.set(0, piece.getRightValue());
                playableValues.set(1, -1);
                break;
            case 2:
                playableValues.set(0, piece.getLeftValue());
                playableValues.set(1, piece.getRightValue());
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

    public void displayUsedDominoesInConsole() {
        for (int i = 0; i < usedTiles.size(); i++) {
            if (usedTiles.get(i) == usedTiles.getLast()) {
                usedTiles.get(i).setDisplayOrientation(playableValues, getPositionIndicator());
            } else {
                usedTiles.get(i).displayTileInConsole(orientation.get(i));
            }
            orientation.set(i, usedTiles.get(i).getOrientation());
        }
    }

    /**
     * Checks if a value is playable.
     * Compares the left and right values with the current playable values.
     *
     * @return True if either value is playable, false otherwise.
     */
    public boolean isTheValuePlayable(Piece selectedPiece) {
        int upperValue = 0;
        int leftValue = 0;
        int rightValue = 0;

        if (selectedPiece instanceof DominoPiece) {
            leftValue = ((DominoPiece) selectedPiece).getLeftValue();
            rightValue = ((DominoPiece) selectedPiece).getRightValue();
        } else if (selectedPiece instanceof TridominoPiece) {
            upperValue = ((TridominoPiece) selectedPiece).getUpperValue();
            leftValue = ((TridominoPiece) selectedPiece).getLeftValue();
            rightValue = ((TridominoPiece) selectedPiece).getRightValue();
        }

        if (getPieceInTable() instanceof DominoPiece && selectedPiece instanceof DominoPiece) {
            setPositionIndicator(0);
            return leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if (getPieceInTable() instanceof DominoPiece && selectedPiece instanceof TridominoPiece) {
            setPositionIndicator(1);
            return upperValue == playableValues.getFirst() || leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof DominoPiece) {
            setPositionIndicator(2);
            return (leftValue == playableValues.getFirst() && rightValue == playableValues.getLast()) || (rightValue == playableValues.getFirst() && leftValue == playableValues.getLast());
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof TridominoPiece) {
            setPositionIndicator(3);
            return (leftValue == playableValues.getFirst() && upperValue == playableValues.getLast()) || (rightValue == playableValues.getFirst() && leftValue == playableValues.getLast()) || (upperValue == playableValues.getFirst() && rightValue == playableValues.getLast());
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() == -1 && selectedPiece instanceof DominoPiece) {
            setPositionIndicator(4);
            return leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() == -1 && selectedPiece instanceof TridominoPiece) {
            setPositionIndicator(5);
            return upperValue == playableValues.getFirst() || leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        }
        return false;
    }

    public boolean moreMovesForPlayer(ArrayList<Piece> playerTiles) {
        boolean moreMoves = false;
        int totalOfMoves = 0;

        for (Piece piece : playerTiles) {
            moreMoves = isTheValuePlayable(piece);

            if (moreMoves) {
                totalOfMoves++;
            }
        }

        return totalOfMoves > 0;
    }

    public boolean noMoreMovesInGame() {
        boolean moreMovesPlayer1;
        boolean moreMovesPlayer2;

        moreMovesPlayer1 = moreMovesForPlayer(player1Tiles);
        moreMovesPlayer2 = moreMovesForPlayer(player2Tiles);

        if (!moreMovesPlayer1 && !moreMovesPlayer2) {
            return true;
        } else {
            return false;
        }

    }

    public int getWinner(ArrayList<Piece> player1Tiles, ArrayList<Piece> player2Tiles) {
        int c0 = 0;
        int c1 = 0;

        for (Piece piece : player1Tiles) {
            c0++;
        }

        for (Piece piece : player2Tiles) {
            c1++;
        }

        if (c0 > c1) {
            return 0;
        } else if (c0 < c1) {
            return 1;
        } else {
            return -1;
        }
    }


    public void pressEnterToContinue() {
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}
