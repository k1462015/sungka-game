package uk.ac.kcl.teamraccoon.sungka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity implements HighScoresFragment.CallbackHighScores {

    static final String PLAYER_TAG = "PLAYER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
    }

    @Override
    public void onItemSelected(String playerName) {
        Bundle bundle = new Bundle();
        bundle.putString(PLAYER_TAG, playerName);

        PlayerStatisticsFragment fragment = new PlayerStatisticsFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.player_data_container, fragment, PlayerStatisticsFragment.TAG)
                .commit();
    }
}
