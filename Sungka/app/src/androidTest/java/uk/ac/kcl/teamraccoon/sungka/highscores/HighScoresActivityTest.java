package uk.ac.kcl.teamraccoon.sungka.highscores;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.kcl.teamraccoon.sungka.MainMenu;
import uk.ac.kcl.teamraccoon.sungka.R;
import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

public class HighScoresActivityTest extends ActivityInstrumentationTestCase2<MainMenu> {

    /* Scenario:
        1. Clear the High Scores table
        2. Add data for a player
        3. Launch HighScoresActivity
        4. The PlayerStatisticsFragment should be launched in a Master-Detail layout
                and it should contain information about the first list item
        5. Check if the TextView in a detail view displays the player name
     */

    final String PLAYER_NAME = "uk.ac.kcl.teamraccoon.sungka.TEST";

    public HighScoresActivityTest() {
        super(MainMenu.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearData();
        addData();
    }

    public void clearData() {
        getActivity().getContentResolver().delete(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                null
        );

        getActivity().getContentResolver().delete(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                null,
                null
        );
    }

    public void addData() {
        ContentValues values = new ContentValues();
        values.put(SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME, PLAYER_NAME);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED, 1);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_WON, 1);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST, 0);
        values.put(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE, 50);

        getActivity().getContentResolver().insert(
                SungkaContract.PlayerEntry.CONTENT_URI,
                values
        );

        values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, "uk.ac.kcl.teamraccoon.sungka.TEST");
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, 50);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SungkaContract.HighScoresEntry.COLUMN_DATE, dateFormat.format(new Date()));

        getActivity().getContentResolver().insert(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                values
        );
    }

    public void testMasterDetailLayout() throws InterruptedException {
        MainMenu mainActivity = getActivity();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor am = instrumentation.addMonitor(HighScoresActivity.class.getName(), null, false);
        final ImageButton btnHighScores = (ImageButton) mainActivity.findViewById(R.id.main_menu_highscores);
        TouchUtils.clickView(this, btnHighScores);
        Thread.sleep(3000);
        HighScoresActivity scoresActivity = (HighScoresActivity) instrumentation.waitForMonitorWithTimeout(am, 2);
        TextView tvPlayerName = (TextView) scoresActivity.findViewById(R.id.player_name_label);

        assertEquals(PLAYER_NAME, "" + tvPlayerName.getText());
    }

}
