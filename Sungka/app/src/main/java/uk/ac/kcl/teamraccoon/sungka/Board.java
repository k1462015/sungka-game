package uk.ac.kcl.teamraccoon.sungka;

/**
 * Created by ana on 21/10/15.
 */
public class Board {


    public int arrayOfButtons[] = new int[16];
    public int player2store = 15;
    public int player1store = 7;

    public Enum currentPlayer = Player.PLAYER_ONE;

    public Board(){

        //initialise the trays with 7
        for(int i = 0; i<15; i++){
            arrayOfButtons[i] = 7;
        }

        //initialise the store with 0
        arrayOfButtons[player1store] = 0;
        arrayOfButtons[player2store] = 0;

    }

    public void switchPlayer(){
        if (currentPlayer == Player.PLAYER_ONE){
            currentPlayer = Player.PLAYER_TWO;
        } else {
            currentPlayer = Player.PLAYER_ONE;
        }
    }

    public void takeTurn(int selectedTrayIndex){

        //assume that player selects the right tray (on his side, not the store, not empty)

        if(checkTrays(currentPlayer)){

        }

    }

    /**
     * Checks if trays are empty or not for current player.
     * @param player current player
     * @return true if at least one the player's tray is not empty. False otherwise.
     */
    public boolean checkTrays(Enum player){
        int indexStart;
        int indexEnd;

        if(player == Player.PLAYER_ONE){
            indexStart = 0;
            indexEnd = 6;
        } else {
            indexStart = 8;
            indexEnd = 14;
        }

        for(int i = indexStart; i <= indexEnd; i++){
            if (arrayOfButtons[i] > 0){
                return true;
            }
        }

        return false;
    }

}
