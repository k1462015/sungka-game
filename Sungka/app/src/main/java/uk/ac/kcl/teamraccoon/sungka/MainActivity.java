package uk.ac.kcl.teamraccoon.sungka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
    Button shellsInHandButton;
    TextView shellStatus;
    int shellsInHand;
    int index;
    TranslateAnimation anim;
    MediaPlayer mp;
    MediaPlayer backgroundMusic;
    TextView shellsInHandDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shellsInHandButton = (Button) findViewById(R.id.shellsInHand);
        shellsInHandButton.setVisibility(View.VISIBLE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.gong);
        mp.start();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);

        shellsInHandDisplay = (TextView) findViewById(R.id.shellInHandDisplay);
//        shellStatus = (TextView) findViewById(R.id.handStatus);

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

        // Restore preferences
//        SharedPreferences trayData = getSharedPreferences("TRAYDATA", 0);
//        int[] tempArray = new int[16];
//        for(int i = 0;i < 16;i++){
//            tempArray[i] = trayData.getInt("TRAY"+i,0);
//        }
//        gameBoard.setArrayOfTrays(tempArray);
        Log.i("MYAPP","ONCREATE IS BEING CALLED");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MYAPP","ONSTOP IS BEING CALLED");
//        int[] arrayOfTrays = gameBoard.getArrayOfTrays();
//
//        // We need an Editor object to make preference changes.
//        // All objects are from android.context.Context
//        SharedPreferences settings = getSharedPreferences("TRAYDATA", 0);
//
//        for(int i = 0;i < 16;i++){
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putInt("TRAY"+i,arrayOfTrays[i]);
//
//            // Commit the edits!
//            editor.commit();
//        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MYAPP", "WIPED TRAY DATA");
//        this.getSharedPreferences("TRAYDATA",0).edit().clear().commit();
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
        if(backgroundMusic != null){
            backgroundMusic.start();
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
            gameStatus.setText((5-seconds)+"");
            handler.postDelayed(this,0);
            if(seconds == 5){
                gameStatus.setText("SELECT YOUR PIT!!");
                handler.removeCallbacks(this);
                enableBoard();
                MediaPlayer hereWeGo = MediaPlayer.create(getApplicationContext(), R.raw.go);
                hereWeGo.start();
                backgroundMusic = MediaPlayer.create(getApplicationContext(),R.raw.backgroundmusic);
                backgroundMusic.setLooping(true);
                backgroundMusic.start();

            }
        }
    };


    @Override
    public void onBackPressed() {
        Log.i("MYAPP", "USER WANTS TO GO BACK");
        Intent intent = new Intent(this,PauseMenu.class);
        startActivity(intent);
//        super.onBackPressed();
    }

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
        backgroundMusic.reset();
        backgroundMusic.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.pause();
    }

    public void updateBoard(final int selectedTray) {

        final int[] arrayOfTrays = gameBoard.getArrayOfTrays();
        shellsInHandButton.setVisibility(View.VISIBLE);
        disableBoard();
        if(playerChosen && selectedTray != -1){
            shellsInHandDisplay.setText("Shells in Hand: "+arrayOfBoardButtons[selectedTray].getText().toString());
            setButtonImage(arrayOfBoardButtons[selectedTray], 0);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    shellsInHand = Integer.parseInt(arrayOfBoardButtons[selectedTray].getText().toString());
                    Log.i("MYAPP","SHELLS IN HAND "+shellsInHand);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arrayOfBoardButtons[selectedTray].setText("0");
                        }
                    });

                    //Center Positions of shells in hand button
                    int[] shellsXy = new int[2];
                    shellsInHandButton.getLocationInWindow(shellsXy);
                    final float shellsInHandX =  shellsXy[0] + (shellsInHandButton.getWidth() / 2);
                    final float shellsInHandY = shellsXy[1] + (shellsInHandButton.getHeight() / 2);


                    //Loop starting at selected tray
                    //and then back to that tray - 1
                    index = selectedTray + 1;
                    while((index % 16) != selectedTray){
                        //Do something
                        Button tray = arrayOfBoardButtons[index % 16];

                        //Center Position of button tray
                        int[] trayXy = new int[2];
                        tray.getLocationInWindow(trayXy);
                        final float trayX =  trayXy[0] + (tray.getWidth() / 2);
                        final float trayY = trayXy[1] + (tray.getHeight() / 2);

                        //Animate shell going from center to tray
                        int currentValue = Integer.parseInt(tray.getText().toString());
                        int newValue = arrayOfTrays[index % 16];
                        if (shellsInHand > 0 && currentValue != newValue) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Runnable uiThread = this;
                                    anim = new TranslateAnimation(0, trayX - shellsInHandX, 0, trayY - shellsInHandY);
                                    anim.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            anim = null;
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    anim.setDuration(500);
                                    anim.restrictDuration(500);
                                    anim.setFillEnabled(true);
                                    shellsInHandButton.startAnimation(anim);

                                }
                            });
                            mp.start();
                            shellsInHand--;
                        }

                        if(shellsInHand > 0 && currentValue != newValue){
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Decrease shellsInHand
                                shellsInHandButton.setText(shellsInHand + "");
                                shellsInHandDisplay.setText("Shells In Hand: "+shellsInHand);
//                                        shellStatus.setText(shellsInHand+"");

                                //Update shell count on tray
                                arrayOfBoardButtons[(index-1) % 16].setText(arrayOfTrays[(index - 1) % 16] + "");
                                setButtonImage(arrayOfBoardButtons[(index-1) % 16], arrayOfTrays[(index-1) % 16]);
//                                arrayOfBoardButtons[index % 16].setText(arrayOfTrays[index % 16] + "");
//                                setButtonImage(arrayOfBoardButtons[index % 16], arrayOfTrays[index % 16]);
                            }
                        });
                        index++;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shellsInHandButton.setVisibility(View.INVISIBLE);
                            boolean check = false;
                            updateBoardLol();
                        }
                    });
                    handler.removeCallbacks(this);
                }
            };

            thread.start();



//            int shellsInHand = Integer.parseInt(arrayOfBoardButtons[selectedTray].getText().toString());
//            shellsInHandButton.setText(shellsInHand+"");
//
//            //Center Positions of shells in hand button
//            float shellsInHandX =  shellsInHandButton.getX() + (shellsInHandButton.getWidth() / 2);
//            float shellsInHandY = shellsInHandButton.getY() + (shellsInHandButton.getHeight() / 2);
//
//
//            //Loop starting at selected tray
//            //and then back to that tray - 1
//            int index = selectedTray + 1;
//            while((index % 16) != selectedTray){
//                //Do something
//                Button tray = arrayOfBoardButtons[index % 16];
//
//                //Center Position of button tray
//                float trayX =  tray.getX() + (tray.getWidth() / 2);
//                float trayY = tray.getY() + (tray.getHeight() / 2);
//
//                //Animate shell going from center to tray
//                if(shellsInHand != 0){
//                    TranslateAnimation anim = new TranslateAnimation( 0, shellsInHandX - trayX , 0, shellsInHandY - trayY);
//                    anim.setDuration(1000);
//                    anim.setFillEnabled(true);
//                    anim.setFillAfter(true);
//                    shellsInHandButton.startAnimation(anim);
//                    anim.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//                        }
//                    });
//                }
//
//
//                //Update shell count on tray
//                arrayOfBoardButtons[index % 16].setText(arrayOfTrays[index % 16] + "");
//                setButtonImage(arrayOfBoardButtons[index % 16], arrayOfTrays[index % 16]);
//
//                //Decrease shellsInHand
//                shellsInHand--;
//                shellsInHandButton.setText(shellsInHand+"");
//
//                index++;
//            }
        }else{
            updateBoardLol();
        }










//        if (gameBoard.getCurrentPlayer() == Player.PLAYER_ONE) {
//            //enables all the trays for player one that are not zero and disables the opposite side
//            for (int i = 14; i > 7; i--) {
//                arrayOfBoardButtons[i].setEnabled(false);
//                if (arrayOfTrays[14 - i] != 0) {
//                    arrayOfBoardButtons[14 - i].setEnabled(true);
//                } else {
//                    arrayOfBoardButtons[14 - i].setEnabled(false);
//                }
//
//            }
//            if(!playerChosen){
//                gameStatus.setText("HURRY!");
//            }else{
//                gameStatus.setText("Player 1's turn");
//            }
//        } else {
//
//
//            //enables all the trays for player two that are not zero and disables the opposite side
//            for (int i = 0; i < 7; i++) {
//                arrayOfBoardButtons[i].setEnabled(false);
//                if (arrayOfTrays[14 - i] != 0) {
//                    arrayOfBoardButtons[14 - i].setEnabled(true);
//                } else {
//                    arrayOfBoardButtons[14 - i].setEnabled(false);
//                }
//            }
//            if(!playerChosen){
//                gameStatus.setText("HURRY!");
//            }else{
//                gameStatus.setText("Player 2's turn");
//            }
//        }
//
//
//        if (gameBoard.isGameOver()) {
//            if (arrayOfTrays[7] > arrayOfTrays[15]) {
//                gameStatus.setText("Player 1 won!");
//            } else if (arrayOfTrays[7] < arrayOfTrays[15]) {
//                gameStatus.setText("Player 2 won!");
//            } else {
//                gameStatus.setText("It's a draw!");
//            }
//        }

    }

    public void updateBoardLol(){
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
//        if(btn.getWidth() != 0){
//            // Read your drawable from somewhere
//            Bitmap bitmap = ((BitmapDrawable) background).getBitmap();
//            int size = Math.min(btn.getWidth(),btn.getHeight());
//            // Scale it to 50 x 50
//            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
//            // Set your new, scaled drawable "d"
//            btn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,d);
//            btn.setBackground(null);
//        }else{
            btn.setBackground(background);
//        }


    }

    public void setupBoardLayout() {

        arrayOfBoardButtons = new Button[16];
        gameStatus = (TextView) findViewById(R.id.game_status);
        gameStatus.setText("Player 1's turn");

        LinearLayout layout_p2_store = (LinearLayout) findViewById(R.id.layout_p2_store);
        layout_p2_store.setZ(10);
        LinearLayout layout_p1_store = (LinearLayout) findViewById(R.id.layout_p1_store);
        layout_p1_store.setZ(10);
        LinearLayout layout_p2_trays = (LinearLayout) findViewById(R.id.layout_p2_trays);
        layout_p2_trays.setZ(10);
        LinearLayout layout_p1_trays = (LinearLayout) findViewById(R.id.layout_p1_trays);
        layout_p1_trays.setZ(10);

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
                        updateBoard(p1index);
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_ONE){
                            enableBoard();
                        }else{
                            playerChosen = true;
                            gameStatus.setText("Player 2's turn");
                        }

                    }else{
                        gameBoard.takeTurn(p1index);
                        updateBoard(p1index);
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
                        updateBoard(p2index);
                        if(gameBoard.getCurrentPlayer() == Player.PLAYER_TWO){
                            enableBoard();
                        }else{
                            playerChosen = true;
                            gameStatus.setText("Player 1's turn");
                        }
                    }else{
                        gameBoard.takeTurn(p2index);
                        updateBoard(p2index);
                    }

                }
            });
        }

        updateBoard(-1);
    }
}
