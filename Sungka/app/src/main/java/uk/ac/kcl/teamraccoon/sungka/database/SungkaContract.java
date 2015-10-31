package uk.ac.kcl.teamraccoon.sungka.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Sungka database.
 */
public class SungkaContract {

    // Name of the content provider
    public static final String CONTENT_AUTHORITY = "uk.ac.kcl.teamraccoon.sungka";

    // The base of a URI for contacting the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths for looking for game data
    public static final String PATH_PLAYER = "player";
    public static final String PATH_HIGH_SCORES = "high_scores";

    /* Inner class that defines the table contents of the player table */
    public static final class PlayerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        // Table name
        public static final String TABLE_NAME = "player";
        // A unique name of a player
        public static final String PLAYER_NAME = "player_name";
        // The number of games played by a player
        public static final String COLUMN_GAMES_PLAYED = "games_played";
        // The number of games won by a player
        public static final String COLUMN_GAMES_WON = "games_won";
        // The number of games lost by a player
        public static final String COLUMN_GAMES_LOST = "games_lost";
        // The highest scores achieved by a player
        public static final String COLUMN_HIGH_SCORE = "high_score";

        public static Uri buildPlayerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the high scores table */
    public static final class HighScoresEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HIGH_SCORES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGH_SCORES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HIGH_SCORES;

        // Table name
        public static final String TABLE_NAME = "high_scores";
        // A player name
        public static final String COLUMN_PLAYER = "player";
        // Column with scores achieved by players
        public static final String COLUMN_SCORE = "player_score";

        public static Uri buildHighScoresUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}