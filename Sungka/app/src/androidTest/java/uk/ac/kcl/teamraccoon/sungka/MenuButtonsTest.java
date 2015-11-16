package uk.ac.kcl.teamraccoon.sungka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.TextUtils;
import android.text.method.Touch;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.InterruptedIOException;
import java.util.ArrayList;

import uk.ac.kcl.teamraccoon.sungka.highscores.HighScoresActivity;

/**
 * Created by Rosen on 12/11/2015.
 */
public class MenuButtonsTest extends ActivityInstrumentationTestCase2<MainMenu> {
    MainMenu mainMenu;
    Instrumentation instrumentation;

    public MenuButtonsTest() {
        super(MainMenu.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        //initialise activity and instrumentation
        mainMenu = getActivity();
        instrumentation = getInstrumentation();
    }

    public void testHighScores() throws InterruptedIOException {
        // press HighScores button and check if HighScoresActivity is not null ( is opened)
        final ImageButton highScores = (ImageButton) mainMenu.findViewById(R.id.highscores);

        Instrumentation.ActivityMonitor amHighScores = instrumentation.addMonitor(HighScoresActivity.class.getName(), null, false);

        TouchUtils.clickView(this, highScores);

        HighScoresActivity nextActivity = (HighScoresActivity) amHighScores.waitForActivity();
        assertNotNull(nextActivity);
        nextActivity.finish();
    }

    public void testVSP2Button() throws InterruptedException {
        // monitor for GameBoardActivity
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        final ImageButton p1vsp2 = (ImageButton) mainMenu.findViewById(R.id.p1vsp2);

        //go to GameBoardActivity
        TouchUtils.clickView(this, p1vsp2);

        GameBoardActivity nextActivity = (GameBoardActivity) am.waitForActivity();

//        Thread.sleep(2000);

        //assert its a human vs human game and GameBoardActivity exists ( is not null )
        assertNotNull(nextActivity);
        assertTrue(!nextActivity.aiChosen);
        nextActivity.finish();
    }

    public void testVSAIButton() throws InterruptedException {
        // monitor for GameBoardActivity
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        // go to GameBoardActivity
        final ImageButton p1vsAI = (ImageButton) mainMenu.findViewById(R.id.p1vsAI);
        TouchUtils.clickView(this, p1vsAI);
        GameBoardActivity nextActivity = (GameBoardActivity) am.waitForActivity();

//        Thread.sleep(2000);
        // assert GameBoardActivity exists (is not null) and aiChosen is true
        assertNotNull(nextActivity);
        assertTrue(nextActivity.aiChosen);
        nextActivity.finish();
    }

}
