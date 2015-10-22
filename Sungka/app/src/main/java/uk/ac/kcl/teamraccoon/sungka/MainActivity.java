package uk.ac.kcl.teamraccoon.sungka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {


    Board gameBoard;
    Button[] arrayOfBoardButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialises the game board with trays = 7, store = 0
        gameBoard = new Board();
        setupBoardLayout();

    }

    public void updateBoard() {

        int[] arrayOfTrays = gameBoard.getArrayOfTrays();

        for (int i = 0; i < 16; i++) {
            arrayOfBoardButtons[i].setText(arrayOfTrays[i] + "");
        }


        if (gameBoard.getCurrentPlayer() == Player.PLAYER_ONE) {

            //enables all the trays for player one that are not 0 and disables the opposite side
            for (int i = 14; i > 7; i--) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                }

            }
        } else {

            //enables all the trays for player two that are not 0 and disables the opposite side
            for (int i = 0; i < 7; i++) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                }
            }
        }

    }

    public void setupBoardLayout() {

        arrayOfBoardButtons = new Button[16];

        LinearLayout layout_p2_store = (LinearLayout) findViewById(R.id.layout_p2_store);
        LinearLayout layout_p1_store = (LinearLayout) findViewById(R.id.layout_p1_store);
        LinearLayout layout_p2_trays = (LinearLayout) findViewById(R.id.layout_p2_trays);
        LinearLayout layout_p1_trays = (LinearLayout) findViewById(R.id.layout_p1_trays);

        Button storeButtonp1 = (Button) getLayoutInflater().inflate(R.layout.stores, layout_p1_store, false);
        Button storeButtonp2 = (Button) getLayoutInflater().inflate(R.layout.stores, layout_p2_store, false);

        //store buttons n the array
        arrayOfBoardButtons[7] = storeButtonp1;
        arrayOfBoardButtons[15] = storeButtonp2;
        storeButtonp1.setEnabled(false);
        storeButtonp2.setEnabled(false);

        //setting the text for the buttons
        storeButtonp1.setText("Player 1 store");
        storeButtonp2.setText("Player 2 store");

        //adding the buttons to the layout
        layout_p1_store.addView(storeButtonp1);
        layout_p2_store.addView(storeButtonp2);

        for (int i = 0; i < 7; i++) {
            Button tempTrayp1 = (Button) getLayoutInflater().inflate(R.layout.tray, layout_p1_trays, false);
            tempTrayp1.setText("Tray " + i);
            arrayOfBoardButtons[i] = tempTrayp1;
            layout_p1_trays.addView(tempTrayp1);

            final int p1index = i;

            tempTrayp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameBoard.takeTurn(p1index);
                    updateBoard();
                }
            });


            Button tempTrayp2 = (Button) getLayoutInflater().inflate(R.layout.tray, layout_p2_trays, false);
            tempTrayp2.setText("Tray " + (14 - i));
            arrayOfBoardButtons[14 - i] = tempTrayp2;
            layout_p2_trays.addView(tempTrayp2);

            final int p2index = 14 - i;

            tempTrayp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameBoard.takeTurn(p2index);
                    updateBoard();
                }
            });
        }

        updateBoard();
    }
}
