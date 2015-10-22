package uk.ac.kcl.teamraccoon.sungka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Board gameBoard;
    Button[] arrayOfBoardButtons;
    TextView gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialises the game board with trays = 7, store = 0
        gameBoard = new Board();
        setupBoardLayout();
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First Remove and reset current board
                resetBoard();
                //Sets up board again
                setupBoardLayout();
                //Updates board to show corresponding number
                updateBoard();
            }
        });

    }

    /**
     * Clears all linear layouts on the board
     * And reinitilises board object
     */
    public void resetBoard(){
        LinearLayout layout_p2_store = (LinearLayout) findViewById(R.id.layout_p2_store);
        layout_p2_store.removeAllViews();
        LinearLayout layout_p1_store = (LinearLayout) findViewById(R.id.layout_p1_store);
        layout_p1_store.removeAllViews();
        LinearLayout layout_p2_trays = (LinearLayout) findViewById(R.id.layout_p2_trays);
        layout_p2_trays.removeAllViews();
        LinearLayout layout_p1_trays = (LinearLayout) findViewById(R.id.layout_p1_trays);
        layout_p1_trays.removeAllViews();;

        gameBoard = new Board();
    }

    public void updateBoard() {

        int[] arrayOfTrays = gameBoard.getArrayOfTrays();


        for (int i = 0; i < 16; i++) {
            arrayOfBoardButtons[i].setText(arrayOfTrays[i] + "");
        }


        if (gameBoard.getCurrentPlayer() == Player.PLAYER_ONE) {

            gameStatus.setText("Player 1's turn");

            //enables all the trays for player one that are not 0 and disables the opposite side
            for (int i = 14; i > 7; i--) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                } else {
                    arrayOfBoardButtons[14 - i].setEnabled(false);
                }

            }
        } else {

            gameStatus.setText("Player 2's turn");

            //enables all the trays for player two that are not 0 and disables the opposite side
            for (int i = 0; i < 7; i++) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                } else {
                    arrayOfBoardButtons[14 - i].setEnabled(false);
                }
            }
        }


        if (gameBoard.isGameOver()) {
            if (arrayOfTrays[7] > arrayOfTrays[15]) {
                gameStatus.setText("Player 1 won!");
            } else if (arrayOfTrays[7] < arrayOfTrays[15]) {
                gameStatus.setText("Player 2 won!");
            } else {
                gameStatus.setText("It's a draw!");
            }
        }

    }

    public void setupBoardLayout() {

        arrayOfBoardButtons = new Button[16];
        gameStatus = (TextView) findViewById(R.id.game_status);
        gameStatus.setText("Player 1's turn");

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
