package uk.ac.kcl.teamraccoon.sungka.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


// In order to test the database we followed an online course provided by Udacity
public class SungkaDbTest extends AndroidTestCase {

    public void setUp() {
        deleteSungkaDb();
    }

    public void deleteSungkaDb() {
        mContext.deleteDatabase(SungkaDbHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(SungkaContract.PlayerEntry.TABLE_NAME);
        tableNameHashSet.add(SungkaContract.HighScoresEntry.TABLE_NAME);

        mContext.deleteDatabase(SungkaDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new SungkaDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("The database has not been created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while(c.moveToNext());


        assertTrue("The database has been created without required tables", tableNameHashSet.isEmpty());

        // Test the PlayerEntry table
        c = db.rawQuery("PRAGMA table_info(" + SungkaContract.PlayerEntry.TABLE_NAME + ")", null);

        assertTrue("Unable to query the db", c.moveToFirst());

        final HashSet<String> playerColumnsHashSet = new HashSet<String>();
        playerColumnsHashSet.add(SungkaContract.PlayerEntry._ID);
        playerColumnsHashSet.add(SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME);
        playerColumnsHashSet.add(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED);
        playerColumnsHashSet.add(SungkaContract.PlayerEntry.COLUMN_GAMES_WON);
        playerColumnsHashSet.add(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST);
        playerColumnsHashSet.add(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            playerColumnsHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("The database does not contain required PlayerEntry columns", playerColumnsHashSet.isEmpty());

        // Test the HighScoresEntry table
        c = db.rawQuery("PRAGMA table_info(" + SungkaContract.HighScoresEntry.TABLE_NAME + ")", null);

        assertTrue("Unable to query the db", c.moveToFirst());

        final HashSet<String> highScoresColumnsHashSet = new HashSet<String>();
        playerColumnsHashSet.add(SungkaContract.HighScoresEntry._ID);
        playerColumnsHashSet.add(SungkaContract.HighScoresEntry.COLUMN_PLAYER);
        playerColumnsHashSet.add(SungkaContract.HighScoresEntry.COLUMN_SCORE);
        playerColumnsHashSet.add(SungkaContract.HighScoresEntry.COLUMN_DATE);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            highScoresColumnsHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("The database does not contain required HighScoresEntry columns", highScoresColumnsHashSet.isEmpty());


        db.close();
    }

    public void testHighScoresTable() {
        SungkaDbHelper dbHelper = new SungkaDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, "uk.ac.kcl.teamraccoon.sungka.TEST");
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, 50);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SungkaContract.HighScoresEntry.COLUMN_DATE, dateFormat.format(new Date()));

        long highScoresRowId = db.insert(SungkaContract.HighScoresEntry.TABLE_NAME, null, values);
        assertTrue(highScoresRowId != -1);

        Cursor highScoresCursor = db.query(
                SungkaContract.HighScoresEntry.TABLE_NAME,
                null, null, null, null, null, null
        );

        assertTrue("No records returned from the High Scores table", highScoresCursor.moveToFirst());

        assertFalse("More than one record returned from the High Scores table", highScoresCursor.moveToNext());

        highScoresCursor.close();
        dbHelper.close();
    }

    public void testPlayerTable() {
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

        Cursor playerCursor = db.query(
                SungkaContract.PlayerEntry.TABLE_NAME,
                null, null, null, null, null, null
        );

        assertTrue("No records returned from the Player table", playerCursor.moveToFirst());

        assertFalse("More than one record returned from the Player table", playerCursor.moveToNext());

        playerCursor.close();
        dbHelper.close();
    }

}
