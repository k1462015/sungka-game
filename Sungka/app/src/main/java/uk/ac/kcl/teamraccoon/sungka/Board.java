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

}
