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
public class SungkaAITest extends ActivityInstrumentationTestCase2<MainMenu>{

    public SungkaAITest(){
        super(MainMenu.class);
    }

    // Thread.sleep( ) are to help observe the changes //and be cause we love Thread
    public void testAIMakingAMove() throws InterruptedException {
        //change activity
        MainMenu mainActivity = getActivity(); // Menu Activitiy
        Instrumentation instrumentation = getInstrumentation(); // something
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false); //monitoring change of activity
        final ImageButton AIbutton = (ImageButton) mainActivity.findViewById(R.id.p1vsAI);
//        Thread.sleep(2000);
        TouchUtils.clickView(this, AIbutton); // press VS AI button

        //start the game
        GameBoardActivity nextActivity = (GameBoardActivity) instrumentation.waitForMonitorWithTimeout(am,2); // get from monitor the current activity
        final Button[] allTrays = nextActivity.arrayOfBoardButtons;
//        Thread.sleep(3000);
        final Button startButton = nextActivity.startButton;
        TouchUtils.clickView(this, startButton); // press START button

//        Thread.sleep(3000);
        TouchUtils.clickView(this, allTrays[3]); // play tray 3
        // now AI should make his turn, switching the current player back to PLAYER_ONE

        //asserting
        Thread.sleep(4000); // THIS THREAD.SLEEP IS NECCESSARY BECAUSE ANIMATIONS NEED TO FINISH BEFORE ASSERTING
        assertEquals(Player.PLAYER_ONE,nextActivity.gameBoard.getCurrentPlayer());

    }
}