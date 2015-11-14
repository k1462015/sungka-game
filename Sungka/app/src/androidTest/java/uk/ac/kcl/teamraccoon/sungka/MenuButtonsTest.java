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

    public void testMPJoin() throws InterruptedException {
        final ImageButton mpMenu = (ImageButton) mainMenu.findViewById(R.id.p1vsp2lan);
        // monitors for MultiplayerMenu and GameBoardActivity
        Instrumentation.ActivityMonitor amMPMenu = instrumentation.addMonitor(MultiplayerMenu.class.getName(), null, false);
        Instrumentation.ActivityMonitor amGame = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        //go to MultiplayerMenu
        TouchUtils.clickView(this, mpMenu);
//        Thread.sleep(1000);

        //go to GameBoardActivtiy as a client
        MultiplayerMenu nextActivity = (MultiplayerMenu) amMPMenu.waitForActivity();
        final ImageButton join = (ImageButton) nextActivity.findViewById(R.id.join);
        TouchUtils.clickView(this, join);
//        Thread.sleep(1000);

        //enter valid IP address
        instrumentation.sendStringSync("5.5.5.5");
        instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB));
        instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_TAB));
        instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));

        //assert that player entered as client
        GameBoardActivity gameActivity = (GameBoardActivity) amGame.waitForActivity();
        assertNotNull(gameActivity);
        assertTrue(gameActivity.isClient);
        gameActivity.finish();
        nextActivity.finish();
    }


    public void testMPHost() throws InterruptedException {
        final ImageButton mpMenu = (ImageButton) mainMenu.findViewById(R.id.p1vsp2lan);
        // initialise monitors for MultiplayerMenu and GameBoardActivity
        Instrumentation.ActivityMonitor amMPMenu = instrumentation.addMonitor(MultiplayerMenu.class.getName(), null, false);
        Instrumentation.ActivityMonitor amGame = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        //go to MultiplayerMenu
        TouchUtils.clickView(this, mpMenu);
//        Thread.sleep(2000);

        // host a game
        MultiplayerMenu nextActivity = (MultiplayerMenu) amMPMenu.waitForActivity();
        final ImageButton host = (ImageButton) nextActivity.findViewById(R.id.host);
        TouchUtils.clickView(this, host);
//        Thread.sleep(2000);

        //assert player is a server
        GameBoardActivity gameActivity = (GameBoardActivity) amGame.waitForActivity();
        assertNotNull(gameActivity);
        assertTrue(gameActivity.isServer);
        gameActivity.finish();
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
