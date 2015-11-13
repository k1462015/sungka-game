package uk.ac.kcl.teamraccoon.sungka.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract.PlayerEntry;
import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract.HighScoresEntry;

/**
 * Manages a local database for Sungka statistics data.
 */
public class SungkaDbHelper extends SQLiteOpenHelper {

    // The database version must be incremented when the database schema is changed
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "sungka.db";

    public SungkaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to collect statistics about players
        final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                PlayerEntry._ID + " INTEGER PRIMARY KEY, " +
                PlayerEntry.COLUMN_PLAYER_NAME + " TEXT UNIQUE NOT NULL, " +
                PlayerEntry.COLUMN_GAMES_PLAYED + " INTEGER NOT NULL, " +
                PlayerEntry.COLUMN_GAMES_WON + " INTEGER NOT NULL, " +
                PlayerEntry.COLUMN_GAMES_LOST + " INTEGER NOT NULL, " +
                PlayerEntry.COLUMN_HIGH_SCORE + " INTEGER NOT NULL " +
                " );";

        // Create a table to collect game scores data
        final String SQL_CREATE_HIGH_SCORES_TABLE = "CREATE TABLE " + HighScoresEntry.TABLE_NAME + " (" +
                HighScoresEntry._ID + " INTEGER PRIMARY KEY, " +
                HighScoresEntry.COLUMN_PLAYER + " TEXT NOT NULL, " +
                HighScoresEntry.COLUMN_SCORE + " INTEGER NOT NULL, " +
                HighScoresEntry.COLUMN_DATE + " TEXT, " +

                // Set up the column player as a foreign key to the player table
                " FOREIGN KEY (" + HighScoresEntry.COLUMN_PLAYER + ") REFERENCES " +
                PlayerEntry.TABLE_NAME + " (" + PlayerEntry.COLUMN_PLAYER_NAME + ") " +

                " );";

        db.execSQL(SQL_CREATE_PLAYER_TABLE);
        db.execSQL(SQL_CREATE_HIGH_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
