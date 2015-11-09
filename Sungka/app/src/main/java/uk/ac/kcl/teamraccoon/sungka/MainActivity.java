package uk.ac.kcl.teamraccoon.sungka;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.kcl.teamraccoon.sungka.highscores.AddScoreFragment;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {


    Board gameBoard;
    Button[] arrayOfBoardButtons;
    TextView gameStatus;
    int startTime;
    Handler handler = new Handler();
    Runnable aiMove;
    boolean playerChosen;
    boolean aiChosen;
    ImageView shell;
    Button startButton;
    Toast gameToast;
    MediaPlayer trayCapturedSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shell = (ImageView) findViewById(R.id.shell);
        startButton = (Button) findViewById(R.id.startButton);
        gameToast = Toast.makeText(this,"Game Info",Toast.LENGTH_SHORT);
        trayCapturedSound = MediaPlayer.create(getApplicationContext(),R.raw.traycapturedsound);
        Intent intent = getIntent();
        String option = intent.getStringExtra(MainMenu.GAME_OPTION);
        if(option != null && option.equals("P1P2")){
            aiChosen = false;
            playerChosen = false;
        }else
        if(option != null && option.equals("P1Comp")){
            aiChosen = true;
            playerChosen = true;
        }else
        if(option != null && option.equals("Multiplayer")){
            //Needs to be decided
        }else{
            //Since no option found - start as p1 vs p2
            aiChosen = false;
            playerChosen = false;
        }

        //initialises the game board with trays = 7, store = zero
        gameBoard = new Board();
        setupBoardLayout();
        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameToast.cancel(); //Cancel any showing toasts
                //First Remove and reset current board
                if(!aiChosen){
                    playerChosen = false;
                }
                handler.removeCallbacks(aiMove);
                resetBoard();
                gameStatus.setText(""); //Clears game status
            }
        });
        disableBoard(); //Disable board initially until user clicks start Game
        gameStatus.setText(""); //Clears game status
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!aiChosen){
                    setUpTimer();
                }else{
                    updateBoard();
                }
                startButton.setVisibility(View.INVISIBLE);
            }
        });

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
        gameBoard = new Board();
        updateBoard();
        disableBoard();
        startButton.setVisibility(View.VISIBLE);
    }

    /**
     * Update board method with Animations
     * @param selectedIndex - Index to start animation from
     */
    public void updateBoard(final int selectedIndex, final Player playerCaller){
        Log.i("MYAPP", "UPDATE BOARD WITH ANIMATION");
        final Player methodCaller = playerCaller;
        final int[] arrayOfTrays = gameBoard.getArrayOfTrays();
        //First get the number of shells in that tray
        final int numShellsInHand = Integer.parseInt(arrayOfBoardButtons[selectedIndex].getText().toString());

        //Gets a version of the board before any moves
        int[] tempBoard = new int[16];
        for(int i = 0; i < 16;i++){
            int value = Integer.parseInt(arrayOfBoardButtons[i].getText().toString());
            tempBoard[i] = value;
        }
        final int[] traysBefore = tempBoard;
        //Empty that tray by changing to empty tray image
        setButtonImage(arrayOfBoardButtons[selectedIndex], 0);
        scaleButton(arrayOfBoardButtons[selectedIndex]);
        //Get center X and center Y position of shell to translate
        int[] shellPos = new int[2];
        shell.getLocationInWindow(shellPos);
        final float shellX = shellPos[0] + (shell.getWidth() / 2);
        final float shellY = shellPos[1] + (shell.getWidth() / 2);

        //Index to start animation from
        final int startIndex = selectedIndex;
        final Thread animThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("MYAPP","STARTING ANIMATION THREAD");
                int index = startIndex + 1;
                int remainingShells = numShellsInHand;
                int storeToIgnore = -1; //Need index of store to ignore
                if(methodCaller == Player.PLAYER_ONE){
                    storeToIgnore = 15;
                }else{
                    storeToIgnore = 7;
                }

                //Loops around board
                while(remainingShells > 0){
                    Log.i("MYAPP","ANIMATION INDEX: "+index);
                    Log.i("MYAPP","REMAINING SHELLS: "+remainingShells);
                    final int indexCount = index % 16;
                    //Don't show shell going into enemies store
//                    int newShellCount = gameBoard.getArrayOfTrays()[indexCount];
                    final int oldShellCount = Integer.parseInt(arrayOfBoardButtons[indexCount].getText().toString());
                    final Button tray = arrayOfBoardButtons[indexCount];
                    //If the value of tray doesn't change - Then do nothing
                    if(index != storeToIgnore){
                        //Check if there are any shells in hand - If true then animate
                        if(remainingShells > 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Get center position of tray on screen
                                    int[] trayPos = new int[2];
                                    tray.getLocationInWindow(trayPos);
                                    float trayX = trayPos[0] + (tray.getWidth() / 2);
                                    float trayY = trayPos[1] + (tray.getWidth() / 2);
                                    //Animate shell to tray
                                    float X_TRANSLATE = trayX - shellX;
                                    float Y_TRANSLATE = trayY - shellY;
                                    TranslateAnimation transAnim = new TranslateAnimation(0,X_TRANSLATE,0,Y_TRANSLATE);
                                    Log.i("MYAPP","STARTING ANIMATIOn");
                                    transAnim.setDuration(400);
                                    transAnim.restrictDuration(400);
                                    transAnim.setFillEnabled(true);
                                    shell.startAnimation(transAnim);
                                }
                            });

                            //Make Thread wait for same animation duration
                            try {
                                sleep(400);
                                Log.i("MYAPP","MAKING THREAD WAIT FOR 500ms");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Then update image of tray
                            //Update shellCount text on tray
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MYAPP","Updating images of buttons");
                                    setButtonImage(tray, oldShellCount + 1);
                                    arrayOfBoardButtons[indexCount].setText((oldShellCount+1)+"");
                                }
                            });
                            remainingShells--;
                        }
                    }
                    index++;
                }
                if(aiChosen){
                    checkIfCapturedTray(playerCaller,traysBefore,selectedIndex);
                    if(methodCaller == Player.PLAYER_ONE){
//                        checkIfCapturedTray("Player 1",traysBefore,startIndex,selectedIndex);
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            simulateAiMove();
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gameToast.setText("Player 1 gets another go!");
                                    gameToast.show();
                                }
                            });
                        }
                    }else{
//                        checkIfCapturedTray("Ai",traysBefore,startIndex,selectedIndex);
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gameToast.setText("Ai gets another go!");
                                    gameToast.show();
                                }
                            });
                            simulateAiMove();
                        }
                    }
                }else{
//                    if(methodCaller == Player.PLAYER_ONE){
//                        checkIfCapturedTray("Player 1",traysBefore,startIndex,selectedIndex);
//                    }else{
//                        checkIfCapturedTray("Player 2",traysBefore,startIndex,selectedIndex);
//                    }
                    checkIfCapturedTray(playerCaller,traysBefore,selectedIndex);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBoard();
                        }
                    });
                }




                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateBoard();
                    }
                });

                //Ends thread
                handler.removeCallbacks(this);
            }
        });
        animThread.start();
    }

    /**
     * Animates increasing the views size
     * @param view
     */
    public void scaleButton(final View view){
        final int startWidth = view.getLayoutParams().width;
        final int startHeight = view.getLayoutParams().height;

        ScaleAnimation scale = new ScaleAnimation((float)1.0, (float)1.2, (float)1.0, (float)1.2);
        scale.setDuration(200);
        view.startAnimation(scale);

    }

    public void checkIfCapturedTray(final Player player,int[] traysBefore,int selectedTray){
        //Get shells in hand from selected Tray
        int shellsInHand = traysBefore[selectedTray];
        Log.i("MYAPP","SHELLS IN HAND: "+shellsInHand);
        traysBefore[selectedTray] = 0;  //Clear selected tray
        int index = selectedTray;
        //Distribute shells
        while(shellsInHand != 0){
            index++;
            int trayIndex = index % 16;
            if(player == Player.PLAYER_ONE){
                if(trayIndex != 15){
                    traysBefore[trayIndex] = traysBefore[trayIndex] + 1;
                }
            }
            if(player == Player.PLAYER_TWO){
                if(trayIndex != 7){
                    traysBefore[trayIndex] = traysBefore[trayIndex] + 1;
                }
            }
            shellsInHand--;
        }

        //Find last tray and check if == 1 and on players side
        final int lastTrayIndex = index % 16;
        boolean isTrayCaptured = false;
        if(traysBefore[lastTrayIndex] == 1){
            if(player == Player.PLAYER_ONE && (lastTrayIndex > 0 && lastTrayIndex < 7)){
                isTrayCaptured = true;
            }
            if(player == Player.PLAYER_TWO && (lastTrayIndex > 7 && lastTrayIndex < 15)){
                isTrayCaptured = true;
            }
        }
        //If on players side show blink and toast
        if(isTrayCaptured){
            Log.i("MYAPP","TRAY CAPTURED");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String nameOfPlayer = null;
                    if(player == Player.PLAYER_ONE){
                        nameOfPlayer = "Player 1";
                    }else{
                        if(aiChosen){
                            nameOfPlayer = "Ai";
                        }else{
                            nameOfPlayer = "Player 2";
                        }
                    }
                    Log.i("MYAPP",nameOfPlayer+" captured "+lastTrayIndex);
                    gameToast.setText(nameOfPlayer + " captured tray " + lastTrayIndex);
                    gameToast.show();
                    makeTrayBlink(arrayOfBoardButtons[lastTrayIndex]);
                    makeTrayBlink(arrayOfBoardButtons[14 - lastTrayIndex]);
                    trayCapturedSound.start();
                }
            });
        }else{
            Log.i("MYAPP","TRAY NOT CAPTURED");
        }
    }

    public void makeTrayBlink(View view){
        AlphaAnimation  blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(blinkanimation);
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


    public void simulateAiMove(){
        aiMove = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameStatus.setText("Ai's turn");
                            }
                        });
                        int aiTrayIndex = SungkaAI.takeTurn(gameBoard,2);
                        Log.i("MYAPP", "AI TAKING MOVE AT INDEX: " + aiTrayIndex);
                        Toast.makeText(getApplicationContext(), "Ai choose tray "+aiTrayIndex, Toast.LENGTH_SHORT).show();
                        gameBoard.takeTurn(aiTrayIndex);
                        updateBoard(aiTrayIndex,Player.PLAYER_TWO);
                    }
                });
                handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(aiMove, 2500);
    }

    public void setupBoardLayout() {

        arrayOfBoardButtons = new Button[16];
        gameStatus = (TextView) findViewById(R.id.game_status);

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


        //adding the buttons to the layout
        layout_p1_store.addView(storeButtonp1);
        layout_p2_store.addView(storeButtonp2);

        for (int i = 0; i < 7; i++) {
            Button tempTrayp1 = (Button) getLayoutInflater().inflate(R.layout.tray, layout_p1_trays, false);
            setButtonImage(tempTrayp1,0);
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
                        disableBoard();
                        gameBoard.takeTurn(p1index);
                        updateBoard(p1index,Player.PLAYER_ONE);
                    }
                }
            });


            Button tempTrayp2 = (Button) getLayoutInflater().inflate(R.layout.tray, layout_p2_trays, false);
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
                        disableBoard();
                        gameBoard.takeTurn(p2index);
                        updateBoard(p2index,Player.PLAYER_TWO);
                    }

                }
            });
        }

        updateBoard();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //Adjust all button sizes to be perfectly circular
        resizeButtons();
    }

    public void resizeButtons(){
        for(Button btn:arrayOfBoardButtons){
            int newSize = Math.min(btn.getWidth(), btn.getHeight());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    newSize,newSize
            );
            params.gravity = Gravity.CENTER;
            btn.setLayoutParams(params);

        }
    }
}
