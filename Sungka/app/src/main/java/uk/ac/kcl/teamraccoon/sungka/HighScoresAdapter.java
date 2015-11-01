package uk.ac.kcl.teamraccoon.sungka;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

public class HighScoresAdapter extends CursorAdapter {

    public HighScoresAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_score, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvPlayerName = (TextView) view.findViewById(R.id.list_item_player);
        TextView tvPlayerScore = (TextView) view.findViewById(R.id.list_item_scores);

        String playerName = cursor.getString(
                cursor.getColumnIndexOrThrow(SungkaContract.HighScoresEntry.COLUMN_PLAYER));
        int playerScore = cursor.getInt(
                cursor.getColumnIndexOrThrow(SungkaContract.HighScoresEntry.COLUMN_SCORE));

        tvPlayerName.setText(playerName);
        tvPlayerScore.setText(Integer.toString(playerScore));
    }
}
