package uk.ac.kcl.teamraccoon.sungka.highscores;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import uk.ac.kcl.teamraccoon.sungka.GameBoardActivity;

public class AddScoreTest extends ActivityInstrumentationTestCase2<GameBoardActivity> {

    public AddScoreTest() {
        super(GameBoardActivity.class);
    }

    public void testDialogLaunchesHighScoreTable() throws InterruptedException {
        GameBoardActivity boardActivity = getActivity();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(HighScoresActivity.class.getName(), null, false);

        boardActivity.displayDialog();
        Thread.sleep(4000);

        AddScoreFragment scoreFragment = (AddScoreFragment)
               boardActivity.getFragmentManager().findFragmentByTag(AddScoreFragment.DIALOG_TAG);

        AlertDialog alertDialog = (AlertDialog) scoreFragment.getDialog();
        TouchUtils.clickView(this, alertDialog.getButton(AlertDialog.BUTTON_POSITIVE));
        Thread.sleep(4000);

        String nextActivityName = instrumentation.waitForMonitorWithTimeout(am,2).getClass().getName();
        String expectedActivityName = HighScoresActivity.class.getName();
        assertEquals("Check if the HighScoresActivity has been launched",
                expectedActivityName, nextActivityName);
    }
}
