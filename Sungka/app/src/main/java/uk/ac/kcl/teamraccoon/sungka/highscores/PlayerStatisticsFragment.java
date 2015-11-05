package uk.ac.kcl.teamraccoon.sungka.highscores;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.kcl.teamraccoon.sungka.R;
import uk.ac.kcl.teamraccoon.sungka.data.PlayerData;


public class PlayerStatisticsFragment extends Fragment {

    static final String TAG = PlayerStatisticsFragment.class.getSimpleName();
    private View rootView;

    public PlayerStatisticsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_player_statistics, container, false);
        String playerName = getArguments().getString(HighScoresActivity.PLAYER_TAG);
        int[] userData = PlayerData.retrieveUserData(playerName, getActivity());

        displayUserData(userData, playerName);

        return rootView;
    }

    private void displayUserData(int[] userData, String playerName) {
        ((TextView) rootView.findViewById(R.id.player_name_label)).setText(playerName);
        ((TextView) rootView.findViewById(R.id.games_played_number)).setText("" + userData[PlayerData.INDEX_GAMES_PLAYED]);
        ((TextView) rootView.findViewById(R.id.games_won_number)).setText("" + userData[PlayerData.INDEX_GAMES_WON]);
        ((TextView) rootView.findViewById(R.id.games_lost_number)).setText("" + userData[PlayerData.INDEX_GAMES_LOST]);
    }


}
