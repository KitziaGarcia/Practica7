import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private ArrayList<Object> boneyard;
    private ArrayList<Piece> player1Tiles;
    private ArrayList<Piece> player2Tiles;
    private ArrayList<Piece> usedTiles;
    private ArrayList<Integer> points;
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

    /**
     * Constructor for the Game class. Initializes the game by setting up players' tiles,
     * determining the starting player, and initiating the game.
     * @param player1TotalDominoes the number of domino pieces for player 1.
     * @param player1TotalTridominoes the number of tridomino pieces for player 1.
     * @param player2TotalDominoes the number of domino pieces for player 2.
     * @param player2TotalTridominoes the number of tridomino pieces for player 2.
     */
    public Game(int player1TotalDominoes, int player1TotalTridominoes, int player2TotalDominoes, int player2TotalTridominoes) {
        initializeGame(player1TotalDominoes, player1TotalTridominoes, player2TotalDominoes, player2TotalTridominoes);
        setFirstTurn(true);
        getStartingTile();
        displayUsedDominoesInConsole();
        play();
    }

    /**
     * Sets up the initial state of the game by shuffling the tiles, assigning tiles to each player,
     * and preparing the boneyard.
     * @param player1TotalDominoes the number of domino pieces for player 1.
     * @param player1TotalTridominoes the number of tridomino pieces for player 1.
     * @param player2TotalDominoes the number of domino pieces for player 2.
     * @param player2TotalTridominoes the number of tridomino pieces for player 2.
     */
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

    /**
     * Manages the main game loop, checking for available moves for each player,
     * handling turns and determining the end of the game.
     */
    public void play() {
        boolean moreMovesForPlayer;
        int winnerIndex;
        boolean endGame = false;
        setPositionIndicator(-1);
        System.out.println("\nFichas en pozo: " + boneyard.size());
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

        if (player1Tiles.isEmpty() || player2Tiles.isEmpty()) {
            endGame = true;
        }

        if (endGame) {
            winnerIndex = getWinner();
            System.out.print("Ha ganado el jugador " + (winnerIndex + 1) + " con " + points.get(winnerIndex) + " puntos.");
        }

        if (getPieceInTable() instanceof DominoPiece) {
            setDominoPlayableValues();
        } else {
            setTridominoPlayableValues();
        }

        setPlayersPoints(getPieceInTable().getSumOfSides());

        setNextTurn();
        play();
    }

    /**
     * It prints the players' tiles in console.
     */
    public void showPlayerTiles() {
        System.out.println("\nFichas jugador 1: " + player1Tiles);
        pressEnterToContinue();

        System.out.println("Fichas jugador 2: " + player2Tiles);
    }

    /**
     * Manages the selection of the starting tile for both players.
     * If both players choose tiles with the same sum, they must reselect their pieces.
     */
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
            setNextTurn();
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
        } else if (getFoundPiece() instanceof DominoPiece && !(((DominoPiece) getFoundPiece()).isDouble())) {
            System.out.println("\n\nSeleccione como quiere poner la ficha: ");
            ((DominoPiece) getPieceInTable()).displayTileOptionsInConsole();
            int opt = Utilities.isInputValid(input, "Opcion 1." + "     Opcion 2.", 1, 2);

            setPositionIndicator(-1);

            if (opt == 2) {
                getPieceInTable().rotateLeft();
            }
        }

        if (getFoundPiece() instanceof DominoPiece) {
            setDominoPlayableValues();
        } else if (getFoundPiece() instanceof TridominoPiece){
            if (((TridominoPiece) getFoundPiece()).isTriple()) {
                setPositionIndicator(-1);
            }
            setTridominoPlayableValues();
        }

        getPieceInTable().setDisplayOrientation(playableValues, getPositionIndicator());
        usedTiles.add(getPieceInTable());
        orientation.add(getPieceInTable().getOrientation());
        setPlayersPoints(getPieceInTable().getSumOfSides());
        setFirstTurn(false);
        setNextTurn();
    }

    /**
     * Allows the current player to select the type of tile they want to play (Domino or Tridomino).
     */
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

    /**
     * Selects a specific piece based on the player's choice of tile type and values.
     * It checks if the tile is present in the player's hand and if it is playable.
     *
     * @param option The type of piece the player chooses to play, 1 for Domino or 2 for Tridomino.
     */
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
                    if ((getPositionIndicator() == 1 || getPositionIndicator() == 5) && (indicator != 0) && !(((TridominoPiece) getFoundPiece()).isTriple())) {
                        System.out.println("\nSeleccione como quiere poner la ficha: ");
                        ((TridominoPiece) getFoundPiece()).showTwoDisplayOptions(indicator);
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

    /**
     * Returns the current turn.
     * @return The value representing the current turn.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Sets the current turn based on the parameter.
     * @param turn The value representing the turn to set.
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Checks if it is the first turn of the game.
     * @return true if it is the first turn, false otherwise.
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * Sets the first turn status.
     * @param firstTurn A boolean value to set the first turn status.
     */
    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    /**
     * Getter for the starting piece for player 1.
     * @return Piece representing player 1 starting piece.
     */
    public Piece getStartingPiecePlayer1() {
        return startingPiecePlayer1;
    }

    /**
     * Sets the starting piece for player 1.
     * @param startingPiecePlayer1 Piece to be set as player 1 starting piece.
     */
    public void setStartingPiecePlayer1(Piece startingPiecePlayer1) {
        this.startingPiecePlayer1 = startingPiecePlayer1;
    }

    /**
     * Getter for the starting piece for player 2.
     * @return Piece representing player 2 starting piece.
     */
    public Piece getStartingPiecePlayer2() {
        return startingPiecePlayer2;
    }

    /**
     * Sets the starting piece for player 2.
     * @param startingPiecePlayer2 Piece to be set as player 2 starting piece.
     */
    public void setStartingPiecePlayer2(Piece startingPiecePlayer2) {
        this.startingPiecePlayer2 = startingPiecePlayer2;
    }

    /**
     * Retrieves the piece currently in play on the table.
     * @return Piece currently in play on the table.
     */
    public Piece getPieceInTable() {
        return pieceInTable;
    }

    /**
     * Sets the piece currently in play on the table.
     * @param pieceInTable Piece to be set as the current piece on the table.
     */
    public void setPieceInTable(Piece pieceInTable) {
        this.pieceInTable = pieceInTable;
    }

    /**
     * Retrieves the found piece from the player's hand.
     * @return Piece that was found in the player's hand.
     */
    public Piece getFoundPiece() {
        return foundPiece;
    }

    /**
     * Sets the found piece in the player's hand.
     * @param foundPiece Piece to be set as the found piece.
     */
    public void setFoundPiece(Piece foundPiece) {
        this.foundPiece = foundPiece;
    }

    /**
     * Retrieves the position indicator for the piece placement.
     * @return int representing the position indicator.
     */
    public int getPositionIndicator() {
        return positionIndicator;
    }

    /**
     * Sets the position indicator for the piece placement.
     * @param positionIndicator int to be set as the position indicator.
     */
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

    /**
     * Configures the playable values for a Tridomino piece based on the position indicator.
     */
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

    /**
     * Configures the playable values for a Domino piece based on the position indicator.
     */
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

    /**
     * Checks if a tile is present in the player's hand for the specified values.
     * @param piece Piece to search for in the player's hand.
     * @param turn The current turn.
     * @param upperValue Upper value of the tile.
     * @param leftValue Left value of the tile.
     * @param rightValue Right value of the tile.
     * @return Piece that matches the specified values, or null if not found.
     */
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

    /**
     * Displays all used domino tiles in the console with their respective orientations.
     */
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
     * If the selected piece is a Domino, it compares the left and right values with the current playable values.
     * If the selected piece is a Tridomino, it compares the upper, left and right value with the current playable values.
     *
     * @param selectedPiece The selected piece from the player's hand.
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
        }
        return false;
    }

    /**
     * Determines if there are more moves available for the current player.
     * @param playerTiles Tiles held by the current player.
     * @return true if the player has playable moves, false otherwise.
     */
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

    /**
     * Checks if there are no more moves available for both players in the game.
     * @return true if both players have no moves left, false otherwise.
     */
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

    /**
     * Determines the winner of the game based on points.
     * @return int representing the winning player.
     */
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
