package uk.ac.kcl.teamraccoon.sungka.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import uk.ac.kcl.teamraccoon.sungka.R;
import uk.ac.kcl.teamraccoon.sungka.highscores.AddScoreFragment;

public class PlayerData {

    static final public int INDEX_GAMES_PLAYED = 0;
    static final public int INDEX_GAMES_WON = 1;
    static final public int INDEX_GAMES_LOST = 2;
    static final public int INDEX_HIGH_SCORE = 3;

    static public int[] retrieveUserData(String playerName, Context context) {

        String selectionClause = SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME + " = ?";

        Cursor cursor = context.getContentResolver().query(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                selectionClause,
                new String[] {playerName},
                null);

        if (cursor == null) {
            Log.e(AddScoreFragment.class.getName(), "An internal error occurred");
        } else if (cursor.getCount() < 1) {
            return null;
        } else if (cursor.moveToNext()) {
            return new int[] {
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_WON)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE))
            };
        }

        return null;
    }

    static public ArrayList<String[]> retrieveUserScores(String playerName, Context context) {

        String selectionClause = SungkaContract.HighScoresEntry.COLUMN_PLAYER + " = ?";

        Cursor cursor = context.getContentResolver().query(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                null,
                selectionClause,
                new String[] {playerName},
                null);

        ArrayList<String[]> alPlayerScores = new ArrayList<>();

        // Determine the column index of the column named "word"

        if (cursor != null) {
            int indexScore = cursor.getColumnIndexOrThrow(SungkaContract.HighScoresEntry.COLUMN_SCORE);
            int indexDate = cursor.getColumnIndexOrThrow(SungkaContract.HighScoresEntry.COLUMN_DATE);
            while (cursor.moveToNext()) {
                String score = cursor.getString(indexScore);
                String opponentScore = "" + (98 - Integer.parseInt(score));
                score = context.getString(R.string.match_score, score, opponentScore);
                String date = cursor.getString(indexDate);
                alPlayerScores.add(new String[] {score, date});
            }
        }

        return alPlayerScores;
    }
}
