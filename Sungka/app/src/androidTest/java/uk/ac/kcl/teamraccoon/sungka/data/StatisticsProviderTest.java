package uk.ac.kcl.teamraccoon.sungka.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

// In order to test the database we followed an online course provided by Udacity
public class StatisticsProviderTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearData();
    }

    public void clearData() {
        mContext.getContentResolver().delete(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Data has not been removed from the Player table", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Data has not removed been from the High Scores table", 0, cursor.getCount());
        cursor.close();
    }

    public void testPlayerQuery() {
        SungkaDbHelper dbHelper = new SungkaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME, "uk.ac.kcl.teamraccoon.sungka.TEST");
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED, 1);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_WON, 1);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST, 0);
        values.put(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE, 50);

        long playerRowId = db.insert(SungkaContract.PlayerEntry.TABLE_NAME, null, values);
        assertTrue(playerRowId != -1);

        db.close();

        Cursor playerCursor = mContext.getContentResolver().query(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertTrue("No records returned from the Player table", playerCursor.moveToFirst());

        assertFalse("More than one record returned from the Player table", playerCursor.moveToNext());

        playerCursor.close();
    }

    public void testHighScores() {
        SungkaDbHelper dbHelper = new SungkaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, "uk.ac.kcl.teamraccoon.sungka.TEST");
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, 50);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SungkaContract.HighScoresEntry.COLUMN_DATE, dateFormat.format(new Date()));

        long highScoresRowId = db.insert(SungkaContract.HighScoresEntry.TABLE_NAME, null, values);
        assertTrue(highScoresRowId != -1);

        db.close();

        Cursor highScoresCursor = mContext.getContentResolver().query(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertTrue("No records returned from the High Scores table", highScoresCursor.moveToFirst());

        assertFalse("More than one record returned from the High Scores table", highScoresCursor.moveToNext());

        highScoresCursor.close();
    }

}
