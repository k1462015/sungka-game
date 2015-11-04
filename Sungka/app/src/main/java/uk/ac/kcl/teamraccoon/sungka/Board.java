package uk.ac.kcl.teamraccoon.sungka;

import android.util.Log;

public class Board {

    private int arrayOfTrays[] = new int[16];
    private int player2store = 15;
    private int player1store = 7;

    public void setCurrentPlayer(Enum currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private Enum currentPlayer = Player.PLAYER_ONE;

    public Board() {

        //initialise the trays with 7
        for (int i = 0; i < 15; i++) {
            arrayOfTrays[i] = 7;
        }

        //initialise the store with zero
        arrayOfTrays[player1store] = 0;
        arrayOfTrays[player2store] = 0;

    }

    public int[] getArrayOfTrays() {
        return arrayOfTrays;
    }

    public void setArrayOfTrays(int[] arrayOfTrays){
        this.arrayOfTrays = arrayOfTrays;
    }

    public Enum getCurrentPlayer() {
        return currentPlayer;
    }

    private void switchPlayer() {
        if (currentPlayer == Player.PLAYER_ONE) {
            currentPlayer = Player.PLAYER_TWO;
            if (!checkTrays(currentPlayer)) {
                switchPlayer();
            }
        } else {
            currentPlayer = Player.PLAYER_ONE;
            if (!checkTrays(currentPlayer)) {
                switchPlayer();
            }
        }
    }

    public void takeTurn(int selectedTrayIndex) {

        //assume that player selects the right tray (on his side, not the store, not empty)

        //first checks if player CAN take turn

        int shellsInHand = arrayOfTrays[selectedTrayIndex];
        arrayOfTrays[selectedTrayIndex] = 0;
        int currentIndex = selectedTrayIndex + 1;

        while (shellsInHand > 0) {

            if ((currentPlayer == Player.PLAYER_ONE && currentIndex % 16 == player2store) ||
                    (currentPlayer == Player.PLAYER_TWO && currentIndex % 16 == player1store)) {
                currentIndex++;
            } else {

                arrayOfTrays[currentIndex % 16]++;
                currentIndex++;
                shellsInHand--;
            }
        }

        //make currentIndex the real index
        currentIndex = --currentIndex % 16;

        //check if empty tray or store
        checkLastTray(currentIndex);

    }

    /**
     * Checks if trays are empty or not for current player.
     *
     * @param player current player
     * @return true if at least one the player's tray is not empty. False otherwise.
     */
    private boolean checkTrays(Enum player) {
        int[] indices = getPlayerTrayIndices(player);
        int indexStart = indices[0];
        int indexEnd = indices[1];

        for (int i = indexStart; i <= indexEnd; i++) {
            if (arrayOfTrays[i] > 0) {
                return true;
            }
        }

        return false;
    }

    private int[] getPlayerTrayIndices(Enum player) {
        int indexStart;
        int indexEnd;

        if (player == Player.PLAYER_ONE) {
            indexStart = 0;
            indexEnd = 6;
        } else {
            indexStart = 8;
            indexEnd = 14;
        }

        return new int[]{indexStart, indexEnd};
    }

    private void checkLastTray(int index) {
        if (index == player1store || index == player2store) {
//            Log.i("MyApp", "it landed on player's side");

            //if the current player can't do his extra turn, else let him take a turn again
            if (!checkTrays(currentPlayer) && !(isGameOver())) {
                switchPlayer();
            }
            return;
        }


        int[] trayIndices = getPlayerTrayIndices(currentPlayer);
        int indexStart = trayIndices[0];
        int indexEnd = trayIndices[1];


        //if the last shell is in player's side
        if (index <= indexEnd && index >= indexStart) {
            // check if last tray was empty
            if (arrayOfTrays[index] == 1) {
                //player's side and opponent's side
                int opponentTray = 14 - index;

                //empty the trays
                arrayOfTrays[index] = 0;

                //update the store
                arrayOfTrays[indexEnd + 1] = arrayOfTrays[indexEnd + 1] + 1 + arrayOfTrays[opponentTray];
                arrayOfTrays[opponentTray] = 0;

                if (isGameOver()) {

                } else {
                    switchPlayer();
                }
            } else {
                switchPlayer();
            }
        } else {
            switchPlayer();
        }

    }

    public boolean isGameOver() {
        if (!(checkTrays(Player.PLAYER_ONE)) && !(checkTrays(Player.PLAYER_TWO))) {
            return true;
        }
        return false;
    }

}
