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
    private ArrayList<Integer> points;
    ClearScreen clear = new ClearScreen();
    Scanner input = new Scanner(System.in);
    private Piece foundPiece;
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
        displayUsedDominoesInConsole();
        play();
    }

    public void initializeGame(int player1TotalDominoes, int player1TotalTridominoes, int player2TotalDominoes, int player2TotalTridominoes) {
        boneyard = new ArrayList<>();
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

        int size = domino.getSize() + tridomino.getSize();
        for (int i = 0; i < size; i++) {
            if (i < domino.getSize()) {
                boneyard.add(domino.getPiece(i));
            }

            if (i < tridomino.getSize()) {
                boneyard.add(tridomino.getPiece(i));
            }
        }

        showPlayerTiles();
        pressEnterToContinue();

        playableValues = new ArrayList<>(Arrays.asList(0,0));
        usedTiles = new ArrayList<>();
        orientation = new ArrayList<>();
        points = new ArrayList<>(Arrays.asList(0,0));
        setStartingPiecePlayer1(null);
        setStartingPiecePlayer2(null);
    }

    public void play() {
        boolean moreMovesForPlayer;
        int winnerIndex;
        System.out.println("INDICATOR FICHA PUESTA: " + getPositionIndicator());
        setPositionIndicator(-1);
        System.out.println("Fichas en pozo: " + boneyard.size());
        System.out.println("USED: " + usedTiles);
        System.out.println("Puntos jugador 1: " + points.getFirst() + ".   Puntos jugador 2: " + points.getLast() + ".");

        if (noMoreMovesInGame() && boneyard.isEmpty()) {
            showPlayerTiles();
            System.out.println("\nEl juego se ha cerrado y no hay mas fichas por comer.");
            winnerIndex = getWinner();

            if (winnerIndex == 2) {
                System.out.print("Empate, ambos tienen los mismos puntos.");
            } else {
                System.out.print("Ha ganado el jugador " + (winnerIndex + 1) + " con " + points.get(winnerIndex) + " puntos.");
                return;
            }
        }

        System.out.println("\nTURNO JUGADOR " + (getTurn() + 1) + ":");
        if (getTurn() == 0) {
            moreMovesForPlayer = moreMovesForPlayer(player1Tiles);
        } else {
            moreMovesForPlayer = moreMovesForPlayer(player2Tiles);
        }

        if (!moreMovesForPlayer && !boneyard.isEmpty()) {

            if (getTurn() == 0) {
                System.out.println("Fichas: " + player1Tiles);
                System.out.println("No tienes movimientos, tienes que comer 2 fichas.");
                pressEnterToContinue();
                eatFromBoneyard(player1Tiles);
                System.out.println("Fichas despues de comer: " + player1Tiles);
                moreMovesForPlayer = moreMovesForPlayer(player1Tiles);
            } else {
                System.out.println("Fichas: " + player2Tiles);
                System.out.println("No tienes movimientos, tienes que comer 2 fichas.");
                pressEnterToContinue();
                eatFromBoneyard(player2Tiles);
                System.out.println("Fichas despues de comer: " + player2Tiles);
                moreMovesForPlayer = moreMovesForPlayer(player2Tiles);
            }

            if (!moreMovesForPlayer) {
                System.out.println("No tienes mas movimientos, pasas automaticamente.");
                pressEnterToContinue();
                setNextTurn();
                displayUsedDominoesInConsole();
                play();
                return;
            }
        } else if (!moreMovesForPlayer) {
            System.out.println("No tienes mas movimientos y ya no hay fichas para comer, pasas automaticamente.");
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

        setPlayersPoints(getPieceInTable().getSumOfSides());

        setNextTurn();
        play();
    }

    public void showPlayerTiles() {
        System.out.println("\nFichas jugador 1: " + player1Tiles);
        pressEnterToContinue();

        System.out.println("Fichas jugador 2: " + player2Tiles);
    }

    public void getStartingTile() {
        int opc;

        do {
            System.out.println("\nJugador 1: " + player1Tiles);

            System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
            opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

            selectPiece(opc);
        } while (getStartingPiecePlayer1() == null);
        setNextTurn();

        do {
            System.out.println("\nJugador 2: " + player2Tiles);
            System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
            opc = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

            selectPiece(opc);
        } while (getStartingPiecePlayer2() == null);

        System.out.println("Ficha seleccionada jugador 1: ");
        if (getStartingPiecePlayer1() instanceof DominoPiece) {
            getStartingPiecePlayer1().setDisplayOrientation(playableValues, -1);
        } else {
            getStartingPiecePlayer1().setDisplayOrientation(playableValues, -1);
        }

        getStartingPiecePlayer1().displayTileInConsole(getStartingPiecePlayer1().getOrientation());

        System.out.println("Ficha seleccionada jugador 2: ");
        if (getStartingPiecePlayer2() instanceof DominoPiece) {
            getStartingPiecePlayer2().setDisplayOrientation(playableValues, -1);
        } else {
            getStartingPiecePlayer2().setDisplayOrientation(playableValues, -1);
        }

        getStartingPiecePlayer2().displayTileInConsole(getStartingPiecePlayer2().getOrientation());

        if (getStartingPiecePlayer1().getSumOfSides() > getStartingPiecePlayer2().getSumOfSides()) {
            System.out.println("\nComienza el jugador 1 porque sumó " + getStartingPiecePlayer1().getSumOfSides() + " puntos.\n\n");
            setTurn(0);
            pressEnterToContinue();
            setPieceInTable(getStartingPiecePlayer1());
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

        if (getPieceInTable() instanceof TridominoPiece && !(((TridominoPiece) getPieceInTable()).isTriple())) {
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
                case 4:
                    setPositionIndicator(-2);
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
        } else if (getFoundPiece() instanceof DominoPiece && !(((DominoPiece) getFoundPiece()).isDouble())) {
            System.out.println("\n\nSeleccione como quiere poner la ficha: ");
            ((DominoPiece) getPieceInTable()).displayTileOptionsInConsole();
            int opt = Utilities.isInputValid(input, "Opcion 1." + "     Opcion 2.", 1, 2);

            setPositionIndicator(-1);

            if (opt == 2) {
                getPieceInTable().rotateLeft();
            }

            setDominoPlayableValues();
        }

        getPieceInTable().setDisplayOrientation(playableValues, getPositionIndicator());
        usedTiles.add(getPieceInTable());
        orientation.add(getPieceInTable().getOrientation());
        setPlayersPoints(getPieceInTable().getSumOfSides());
        setFirstTurn(false);
        setNextTurn();
    }

    public void selectTileType() {
        int opt;

        do {
            if (getTurn() == 0) {
                System.out.println("\nFichas: " + player1Tiles);
            } else {
                System.out.println("\nFichas: " + player2Tiles);
            }

            System.out.println("Seleccione tipo de ficha a utilizar:    1. Domino.         2. Tridomino.");
            opt = Utilities.isInputValid(input, "Seleccion: ", 1, 2);

            selectPiece(opt);
        } while (getFoundPiece() == null);
    }

    public void selectPiece(int option) {
        setFoundPiece(null);
        int leftValue;
        int rightValue;
        int turn = getTurn();
        boolean isPlayable = false;
        Domino domino = new Domino();
        DominoPiece selectedDomino;
        int value1;
        int value2;
        int value3;
        Tridomino tridomino = new Tridomino();
        TridominoPiece selectedTridomino;

        if (option == 1) {
            System.out.println("Seleccione ficha a utilizar: ");
            leftValue = Utilities.isInputValid(input, "Valor izquierdo: ", 0, 6);
            rightValue = Utilities.isInputValid(input, "Valor derecho: ", 0, 6);
            System.out.println();
            selectedDomino = new DominoPiece(leftValue, rightValue, 0);
            setFoundPiece(isTilePresent(selectedDomino, turn, 0, leftValue, rightValue));

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
        } else {
            System.out.println("Seleccione ficha a utilizar: ");
            value1 = Utilities.isInputValid(input, "Valor 1: ", 0, 5);
            value2 = Utilities.isInputValid(input, "Valor 2: ", 0, 5);
            value3 = Utilities.isInputValid(input, "Valor 3: ", 0, 5);
            System.out.println();
            selectedTridomino = new TridominoPiece(value1, value2, value3, 0);
            setFoundPiece(isTilePresent(selectedTridomino, turn, value1, value2, value3));

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
                } else {
                    int indicator = 0;
                    indicator = ((TridominoPiece) getFoundPiece()).getIndicatorForTwoDisplayOptions(playableValues);
                    if ((getPositionIndicator() == 1 || getPositionIndicator() == 5) && (indicator != 0) && !((TridominoPiece) getFoundPiece()).isTriple()) {
                        System.out.println("\nSeleccione como quiere poner la ficha: ");
                        ((TridominoPiece) getFoundPiece()).showTwoDisplayOptions(playableValues, indicator);
                        int opt = Utilities.isInputValid(input, "Opcion 1." + "     Opcion 2.", 1, 2);

                        if (opt == 1 && indicator == 1) {
                            getFoundPiece().rotateRight();
                        } else if (opt == 1 && indicator == 2) {
                            getFoundPiece().rotateLeft();
                        } else if (opt == 1 && indicator == 3) {
                            getFoundPiece().rotateLeft();
                        }
                    }

                }
            }
        }

        if (!firstTurn && getFoundPiece() != null) {
            getFoundPiece().setDisplayOrientation(playableValues, getPositionIndicator());
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
        TridominoPiece piece = (TridominoPiece) getPieceInTable();

        switch (getPositionIndicator()) {
            case -1, 1, 5:
                playableValues.set(0, piece.getLeftValue());
                playableValues.set(1, piece.getRightValue());
                break;
            case -2, 0, 2, 3:
                playableValues.set(0, piece.getUpperValue());
                playableValues.set(1,-1);
                break;
        }
    }

    public void setDominoPlayableValues() {
        DominoPiece piece = (DominoPiece) getPieceInTable();

        switch (getPositionIndicator()) {
            case -1, 1, 0, 4:
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
                usedTiles.get(i).displayTileInConsole(orientation.get(i));
                orientation.set(i, usedTiles.get(i).getOrientation());
            } else {
                usedTiles.get(i).displayTileInConsole(orientation.get(i));
            }
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

        if (getPieceInTable() instanceof DominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof DominoPiece && orientation.getLast() != 2) {
            setPositionIndicator(0);
            return leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if (getPieceInTable() instanceof DominoPiece && playableValues.getLast() == -1) {
            setPositionIndicator(1);
            return upperValue == playableValues.getFirst() || leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if ((getPieceInTable() instanceof TridominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof DominoPiece)
        || (getPieceInTable() instanceof DominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof DominoPiece && orientation.getLast() == 2)) {
            setPositionIndicator(2);
            return (leftValue == playableValues.getFirst() && rightValue == playableValues.getLast()) || (rightValue == playableValues.getFirst() && leftValue == playableValues.getLast());
        } else if ((getPieceInTable() instanceof TridominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof TridominoPiece) ||
                (getPieceInTable() instanceof DominoPiece && playableValues.getLast() != -1 && selectedPiece instanceof TridominoPiece)) {
            setPositionIndicator(3);
            return (leftValue == playableValues.getFirst() && upperValue == playableValues.getLast()) || (rightValue == playableValues.getFirst() && leftValue == playableValues.getLast()) || (upperValue == playableValues.getFirst() && rightValue == playableValues.getLast());
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() == -1 && selectedPiece instanceof DominoPiece) {
            setPositionIndicator(4);
            return leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else if (getPieceInTable() instanceof TridominoPiece && playableValues.getLast() == -1 && selectedPiece instanceof TridominoPiece) {
            setPositionIndicator(5);
            return upperValue == playableValues.getFirst() || leftValue == playableValues.getFirst() || rightValue == playableValues.getFirst();
        } else {
            System.out.print("NO ENTRA");
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
            } else {
                setPositionIndicator(-1);
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

    public int getWinner() {
        if (points.getFirst() > points.getLast()) {
            return 0;
        } else if (points.getFirst() < points.getLast()) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Draws two tiles from the boneyard for the current player.
     * If the boneyard is not empty, the method adds the first two tiles from the boneyard
     * to the player's tiles and removes it from the boneyard.
     *
     * @param playerTiles
     */
    public void eatFromBoneyard(ArrayList<Piece> playerTiles) {
        if (!boneyard.isEmpty()) {
            int boneyardSize = boneyard.size();

            int tilesToTake = Math.min(boneyardSize, 2);

            for (int i = 0; i < tilesToTake; i++) {
                playerTiles.add((Piece) boneyard.getFirst());
                boneyard.removeFirst();
            }
        } else {
            System.out.println("No hay fichas para comer, pasas automaticamente.");
        }
    }

    /**
     * Updates the player's points.
     * @param newPoints The points to add.
     */
    public void setPlayersPoints(int newPoints) {
        int currentPlayerPoints = 0;

        currentPlayerPoints = points.get(getTurn());
        points.set(getTurn(), newPoints + currentPlayerPoints);
        System.out.println("Sumaste: " + newPoints + " puntos.");
        pressEnterToContinue();
    }


    public void pressEnterToContinue() {
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }
}
