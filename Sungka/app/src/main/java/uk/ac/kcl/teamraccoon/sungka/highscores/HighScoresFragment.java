package uk.ac.kcl.teamraccoon.sungka.highscores;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import uk.ac.kcl.teamraccoon.sungka.R;
import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

public class HighScoresFragment extends Fragment {

    public HighScoresFragment() {}

    public interface CallbackHighScores {
        void onItemSelected(String playerName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_high_scores, container, false);

        ListView lvHighScores = (ListView) rootView.findViewById(R.id.lv_high_scores);

        String[] projection = {
                SungkaContract.HighScoresEntry._ID,
                SungkaContract.HighScoresEntry.COLUMN_PLAYER,
                SungkaContract.HighScoresEntry.COLUMN_SCORE
        };

        final int ROW_LIMIT = 100;

        Cursor cursor = getActivity().getContentResolver().query(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                projection,
                null,
                null,
                SungkaContract.HighScoresEntry.COLUMN_SCORE + " DESC" + " LIMIT " + ROW_LIMIT
        );

        HighScoresAdapter highScoresAdapter = new HighScoresAdapter(getActivity(), cursor) ;
        lvHighScores.setAdapter(highScoresAdapter);

        lvHighScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playerName = "" + ((TextView) view.findViewById(R.id.list_item_player)).getText();
                ((CallbackHighScores) getActivity()).onItemSelected(playerName);
            }
        });

        return rootView;
    }


}
