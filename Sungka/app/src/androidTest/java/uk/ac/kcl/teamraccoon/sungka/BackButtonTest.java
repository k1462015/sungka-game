package uk.ac.kcl.teamraccoon.sungka;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Rosen on 12/11/2015.
 */
public class BackButtonTest extends ActivityInstrumentationTestCase2<MainMenu> {

    public BackButtonTest() {
        super(MainMenu.class);
    }

    public void testBackButtonPressed() throws InterruptedException {
        // initialise MainMenu, Instrumentation and Monitor
        MainMenu mainMenu = getActivity();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor amGame = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        //press P1 vs P2
        final ImageButton p1vsp2 = (ImageButton) mainMenu.findViewById(R.id.p1vsp2);
        TouchUtils.clickView(this, p1vsp2);

        //initialise GameBoardActivity
        GameBoardActivity nextActivity = (GameBoardActivity) amGame.waitForActivity();
        assertNotNull(nextActivity);
        assertFalse(mainMenu.isActivityVisible());

        // press Back Button
        final Button backButton = (Button) nextActivity.findViewById(R.id.backButton);
        TouchUtils.clickView(this,backButton);

        // press Positive Button
        AlertDialog alertDialog = nextActivity.returnBackAlertDialog();
        Button alertDialogButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        TouchUtils.clickView(this, alertDialogButton);

        assertTrue(mainMenu.isActivityVisible());
    }

}
