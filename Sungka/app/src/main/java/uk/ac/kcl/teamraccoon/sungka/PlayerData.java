package uk.ac.kcl.teamraccoon.sungka;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

public class PlayerData {

    public PlayerData() { }

    public int[] retrieveUserData(String playerName, Context context) {

        String selectionClause = SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME
                + " = ?";

        String[] selectionArgs = {playerName};

        Cursor cursor = context.getContentResolver().query(
                SungkaContract.PlayerEntry.CONTENT_URI,
                null,
                selectionClause,
                selectionArgs,
                null);

        if (cursor == null) {
            Log.e(AddScoreFragment.class.getName(), "An internal error occurred");
        } else if (cursor.getCount() < 1) {
            return null;
        } else if (cursor.moveToNext()) {
            int[] returnValues = {
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_WON)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE))
            };
            return returnValues;
        }

        return null;
    }
}
