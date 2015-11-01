package uk.ac.kcl.teamraccoon.sungka;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        ListView lvHighScores = (ListView) findViewById(R.id.lv_high_scores);

        String[] projection = {
                SungkaContract.HighScoresEntry._ID,
                SungkaContract.HighScoresEntry.COLUMN_PLAYER,
                SungkaContract.HighScoresEntry.COLUMN_SCORE
        };

        final int ROW_LIMIT = 100;

        Cursor cursor = getContentResolver().query(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                projection,
                null,
                null,
                SungkaContract.HighScoresEntry.COLUMN_SCORE + " DESC" + " LIMIT " + ROW_LIMIT
        );

        HighScoresAdapter highScoresAdapter = new HighScoresAdapter(this, cursor) ;
        lvHighScores.setAdapter(highScoresAdapter);
    }
}
