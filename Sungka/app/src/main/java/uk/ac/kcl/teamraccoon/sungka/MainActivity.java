package uk.ac.kcl.teamraccoon.sungka;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
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
    int startTime;
    Handler handler = new Handler();
    boolean playerChosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialises the game board with trays = 7, store = zero
        gameBoard = new Board();
        playerChosen = false;
        setupBoardLayout();
        //Sets up timer for initially selecting first player
        setUpTimer();

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First Remove and reset current board
                playerChosen = false;
                resetBoard();
            }
        });




    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    /**
     * Starts the countdown needed at the beginning of the game
     */
    public void setUpTimer(){
        startTime = (int) System.currentTimeMillis();
        disableBoard();
        handler.postDelayed(startTimer, 0);
    }

    public Runnable startTimer = new Runnable(){
        @Override
        public void run() {
            int elapsedTime = (int) System.currentTimeMillis() - startTime;
            int seconds = elapsedTime / 1000;
            gameStatus.setText((3-seconds)+"");
            handler.postDelayed(this,0);
            if(seconds == 3){
                gameStatus.setText("SELECT YOUR PIT!!");
                handler.removeCallbacks(this);
                enableBoard();
            }
        }
    };

    /**
     * Loops around entire board and disables all buttons
     */
    public void disableBoard(){
        for (Button button: arrayOfBoardButtons){
            button.setEnabled(false);
        }
    }

    /**
     * Loops around entire board and enables all buttons
     */
    public void enableBoard(){
        int[] arrayOfTrays = gameBoard.getArrayOfTrays();
        for(int i = 0;i < arrayOfBoardButtons.length;i++){
            Button button = arrayOfBoardButtons[i];
            if(arrayOfTrays[i] == 0){
                button.setEnabled(false);
            }else{
                button.setEnabled(true);
            }
        }

    }


    /**
     * Clears all linear layouts on the board
     * Also setups up a new board and starts the timer
     */
    public void resetBoard(){
        LinearLayout layout_p2_store = (LinearLayout) findViewById(R.id.layout_p2_store);
        layout_p2_store.removeAllViews();
        LinearLayout layout_p1_store = (LinearLayout) findViewById(R.id.layout_p1_store);
        layout_p1_store.removeAllViews();
        LinearLayout layout_p2_trays = (LinearLayout) findViewById(R.id.layout_p2_trays);
        layout_p2_trays.removeAllViews();
        LinearLayout layout_p1_trays = (LinearLayout) findViewById(R.id.layout_p1_trays);
        layout_p1_trays.removeAllViews();

        gameBoard = new Board();
        setupBoardLayout();
        setUpTimer();
    }

    public void updateBoard() {

        int[] arrayOfTrays = gameBoard.getArrayOfTrays();


        for (int i = 0; i < 16; i++) {
            arrayOfBoardButtons[i].setText(arrayOfTrays[i] + "");
            setButtonImage(arrayOfBoardButtons[i],arrayOfTrays[i]);
        }


        if (gameBoard.getCurrentPlayer() == Player.PLAYER_ONE) {
            //enables all the trays for player one that are not zero and disables the opposite side
            for (int i = 14; i > 7; i--) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                } else {
                    arrayOfBoardButtons[14 - i].setEnabled(false);
                }

            }
            if(!playerChosen){
                gameStatus.setText("HURRY!");
            }else{
                gameStatus.setText("Player 1's turn");
            }
        } else {


            //enables all the trays for player two that are not zero and disables the opposite side
            for (int i = 0; i < 7; i++) {
                arrayOfBoardButtons[i].setEnabled(false);
                if (arrayOfTrays[14 - i] != 0) {
                    arrayOfBoardButtons[14 - i].setEnabled(true);
                } else {
                    arrayOfBoardButtons[14 - i].setEnabled(false);
                }
            }
            if(!playerChosen){
                gameStatus.setText("HURRY!");
            }else{
                gameStatus.setText("Player 2's turn");
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

    public void setButtonImage(Button btn,int number){
        Drawable background;
        if(number > 6){
            background = this.getDrawable(R.drawable.sixplus);
        }else{
            switch(number){
                case 0: background = this.getDrawable(R.drawable.zero); break;
                case 1: background = this.getDrawable(R.drawable.one); break;
                case 2: background = this.getDrawable(R.drawable.two); break;
                case 3: background = this.getDrawable(R.drawable.three); break;
                case 4: background = this.getDrawable(R.drawable.four); break;
                case 5: background = this.getDrawable(R.drawable.five); break;
                case 6: background = this.getDrawable(R.drawable.six); break;
                default: background = this.getDrawable(R.drawable.zero);break;

            }
        }
        btn.setBackground(background);
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
                    if(!playerChosen){
                        gameBoard.setCurrentPlayer(Player.PLAYER_ONE);
                        gameBoard.takeTurn(p1index);
                        updateBoard();
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_ONE){
                            enableBoard();
                        }else{
                            playerChosen = true;
                            gameStatus.setText("Player 2's turn");
                        }

                    }else{
                        gameBoard.takeTurn(p1index);
                        updateBoard();
                    }
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
                    if(!playerChosen){
                        gameBoard.setCurrentPlayer(Player.PLAYER_TWO);
                        gameBoard.takeTurn(p2index);
                        updateBoard();
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            enableBoard();
                        }else{
                            playerChosen = true;
                            gameStatus.setText("Player 1's turn");
                        }
                    }else{
                        gameBoard.takeTurn(p2index);
                        updateBoard();
                    }

                }
            });
        }

        updateBoard();
    }
}
