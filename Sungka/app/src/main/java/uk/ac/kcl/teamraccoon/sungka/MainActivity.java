package uk.ac.kcl.teamraccoon.sungka;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.kcl.teamraccoon.sungka.highscores.AddScoreFragment;

public class MainActivity extends AppCompatActivity {


    Board gameBoard;
    Button[] arrayOfBoardButtons;
    TextView gameStatus;
    int startTime;
    Handler handler = new Handler();
    Runnable aiMove;
    boolean playerChosen;
    boolean aiChosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialises the game board with trays = 7, store = zero
        gameBoard = new Board();
//        playerChosen = false;
        setupBoardLayout();
        //Sets up timer for initially selecting first player
//        setUpTimer();

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First Remove and reset current board
                playerChosen = false;
                handler.removeCallbacks(aiMove);
                resetBoard();
            }
        });
        //Assume User chooses to play against Ai
        playerChosen = true;
        aiChosen = true;
        if(aiChosen){
           gameStatus.setText("Player 1's Move");
        }else{
           setUpTimer();
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
        playerChosen = true;
        aiChosen = true;
        if(aiChosen){
            gameStatus.setText("Player 1's Move");
        }else{
            setUpTimer();
        }
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
                if(aiChosen){
                    gameStatus.setText("Ai's turn");
                }else{
                    gameStatus.setText("Player 2's turn");
                }
            }
        }

        if (gameBoard.isGameOver()) {
            displayDialog();
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

    public void makeAiMove(){
        Log.i("MYAPP", "STARTING AI MOVE");
        updateBoard();
        disableBoard();
        simulateAiMove();
    }

    public void simulateAiMove(){
        aiMove = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int aiTrayIndex = SungkaAI.takeTurn(gameBoard,2);
                        Log.i("MYAPP", "AI TAKING MOVE AT INDEX: " + aiTrayIndex);
                        Toast.makeText(getApplicationContext(), "Ai choose tray "+aiTrayIndex, Toast.LENGTH_SHORT).show();
                        gameBoard.takeTurn(aiTrayIndex);
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            Toast.makeText(getApplicationContext(), "Ai gets another go! "+aiTrayIndex, Toast.LENGTH_SHORT).show();
                            makeAiMove();
                        }else{
                            updateBoard();
                        }
                    }
                });
                handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(aiMove,2500);
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
                        if(aiChosen && gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            makeAiMove();
                        }
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
                        //Just in case-Inputs should be ignored if ai is chosen
                        if(!aiChosen){
                            gameBoard.takeTurn(p2index);
                            updateBoard();
                        }
                    }

                }
            });
        }

        updateBoard();
    }

    // Display a dialog to save game result and show statistics
    private void displayDialog() {
        int[] arrayOfTrays = gameBoard.getArrayOfTrays();

        Bundle bundleDialog = new Bundle();
        int[] scores = {arrayOfTrays[7], arrayOfTrays[15]};
        bundleDialog.putIntArray(AddScoreFragment.BUNDLE_TAG, scores);

        AddScoreFragment addScoreFragment = new AddScoreFragment();
        addScoreFragment.setArguments(bundleDialog);
        addScoreFragment.show(getFragmentManager(), "dialog");
    }
}
