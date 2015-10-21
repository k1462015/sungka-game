package uk.ac.kcl.teamraccoon.sungka;

/**
 * Created by ana on 21/10/15.
 */
public class Board {


    public int arrayOfTrays[] = new int[16];
    public int player2store = 15;
    public int player1store = 7;

    public Enum currentPlayer = Player.PLAYER_ONE;

    public Board(){

        //initialise the trays with 7
        for(int i = 0; i<15; i++){
            arrayOfTrays[i] = 7;
        }

        //initialise the store with 0
        arrayOfTrays[player1store] = 0;
        arrayOfTrays[player2store] = 0;

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

        //first checks if player CAN take turn
        if(checkTrays(currentPlayer)){
            int shellsInHand = arrayOfTrays[selectedTrayIndex];
            arrayOfTrays[selectedTrayIndex] = 0;
            int currentIndex = selectedTrayIndex + 1;

            while (shellsInHand > 0) {

                if ( (currentPlayer == Player.PLAYER_ONE && currentIndex%16 == player2store) ||
                        (currentPlayer == Player.PLAYER_TWO && currentIndex%16 == player1store) ){
                    currentIndex ++;
                }
                else {

                    arrayOfTrays[currentIndex%16] ++;
                    currentIndex ++;
                    shellsInHand --;
                }
            }
        } else {
            switchPlayer();
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
            if (arrayOfTrays[i] > 0){
                return true;
            }
        }

        return false;
    }

}
