package uk.ac.kcl.teamraccoon.sungka;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

/**
 * Created by Ben on 12/11/2015.
 */
public class RestartButtonTest extends ActivityInstrumentationTestCase2<GameBoardActivity> {

    public RestartButtonTest(){
        super(GameBoardActivity.class);
    }

    public void testRestartButtonPressed() throws InterruptedException {

        //starts activity in GameBoardActivity
        GameBoardActivity originalGameBoardActivity = getActivity();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(GameBoardActivity.class.getName(), null, false);

        final int[] originalIntArray = originalGameBoardActivity.gameBoard.getArrayOfTrays();

        final Button originalStartButton = (Button) originalGameBoardActivity.findViewById(R.id.startButton);
        TouchUtils.clickView(this, originalStartButton);

        //sleep until countdown for beginning game has started
        Thread.sleep(3500);

        final Button[] originalGameBoardActivityTrays = originalGameBoardActivity.arrayOfBoardButtons;
        final Button originalButtonTwo = originalGameBoardActivityTrays[1];
        TouchUtils.clickView(this, originalButtonTwo);


        //click on the reset button
        final Button resetButton = (Button) originalGameBoardActivity.findViewById(R.id.resetButton);
        TouchUtils.clickView(this, resetButton);

        AlertDialog alertDialog = originalGameBoardActivity.returnRestartAlertDialog();
        Button alertDialogButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        TouchUtils.clickView(this, alertDialogButton);

        //wait for activity to start again as a result of reset button being pressed
        GameBoardActivity secondGameBoardActivity = (GameBoardActivity) instrumentation.waitForMonitorWithTimeout(am,2); // get from monitor the current activity

        //check that the int[] of the game has reset to original after reset button has been pressed
        assertEquals(originalIntArray,secondGameBoardActivity.gameBoard.getArrayOfTrays());

    }

}
