package uk.ac.kcl.teamraccoon.sungka;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.method.Touch;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Rosen on 10/11/2015.
 */
public class GameRulesTest extends ActivityInstrumentationTestCase2<MainMenu>{
    private GameBoardActivity nextActivity;
    public GameRulesTest(){
        super(MainMenu.class);
    }

    // Thread.sleep( ) are to help observe the changes //and be cause we love Thread
    public void testExtraTurn() throws InterruptedException {
        //Initialise Menu, Instrumentation and Monitor
        MainMenu mainMenu = getActivity(); // Menu Activitiy
        Instrumentation instrumentation = getInstrumentation(); // something
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        //press P1 vs P2
        final ImageButton p1vsp2 = (ImageButton) mainMenu.findViewById(R.id.p1vsp2);
        TouchUtils.clickView(this, p1vsp2);

        //initialise GameBoardActivity
        nextActivity = (GameBoardActivity) am.waitForActivity();

        //press Start
        final Button startButton = (Button) nextActivity.findViewById(R.id.startButton);
        TouchUtils.clickView(this, startButton);

        //sleep until countdown for beginning game has started
        Thread.sleep(3500);

        //set Board to
        //1 1 1 1 1 1 1
        //1           1
        //1 0 1 1 1 1 1
        // and PLAYER_ONE
        nextActivity.gameBoard.setArrayOfTrays(new int[]{1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        nextActivity.gameBoard.setCurrentPlayer(Player.PLAYER_ONE);
        assertEquals(nextActivity.gameBoard.getCurrentPlayer(), Player.PLAYER_ONE);
        nextActivity.runOnUiThread(new Runnable() {
            public void run() {
                nextActivity.updateBoard();
            }
        });
        Thread.sleep(5000);

        // play tray 6 and check if it is still PLAYER_ONE's turn
        TouchUtils.clickView(this, nextActivity.arrayOfBoardButtons[6]);
        assertEquals(nextActivity.gameBoard.getCurrentPlayer(), Player.PLAYER_ONE);
        nextActivity.finish();
    }

    public void testStealShells() throws InterruptedException {
        //Initialise Menu, Instrumentation and Monitor
        MainMenu mainMenu = getActivity();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        //press P1 vs P2
        final ImageButton p1vsp2 = (ImageButton) mainMenu.findViewById(R.id.p1vsp2);
        TouchUtils.clickView(this, p1vsp2);

        //initialise GameBoardActivity
        nextActivity = (GameBoardActivity) am.waitForActivity();

        //press Start
        final Button startButton = (Button) nextActivity.findViewById(R.id.startButton);
        TouchUtils.clickView(this, startButton);

        //sleep until countdown for beginning game has started
        Thread.sleep(3500);

        //set Board to
        //1 1 1 1 1 1 1
        //1           1
        //1 0 1 1 1 1 1
        // and PLAYER_ONE
        nextActivity.gameBoard.setArrayOfTrays(new int[]{1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        nextActivity.gameBoard.setCurrentPlayer(Player.PLAYER_ONE);
        assertEquals(nextActivity.gameBoard.getCurrentPlayer(), Player.PLAYER_ONE);
        nextActivity.runOnUiThread(new Runnable() {
            public void run() {
                nextActivity.updateBoard();
            }
        });
        Thread.sleep(5000);

        // play tray 0 which steals tray 13 and sets player to PLAYER_TWO
        TouchUtils.clickView(this,nextActivity.arrayOfBoardButtons[0]);
        assertEquals(nextActivity.gameBoard.getCurrentPlayer(), Player.PLAYER_TWO);
        assertEquals(nextActivity.gameBoard.getArrayOfTrays()[14-1],0);
        nextActivity.finish();

    }
}