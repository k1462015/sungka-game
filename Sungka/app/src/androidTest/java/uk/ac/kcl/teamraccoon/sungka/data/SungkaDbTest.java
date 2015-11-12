package uk.ac.kcl.teamraccoon.sungka.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

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
}
