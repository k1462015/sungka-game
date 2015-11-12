package uk.ac.kcl.teamraccoon.sungka.highscores;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.kcl.teamraccoon.sungka.R;
import uk.ac.kcl.teamraccoon.sungka.data.PlayerData;
import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;

/**
 * Display a dialog to save game scores.
 */
public class AddScoreFragment extends DialogFragment {

    private int[] mScores;
    private View rootView;
    boolean aiChosen;

    static final public String BUNDLE_TAG = "uk.ac.kcl.teamraccoon.sungka.AddScoreFragment.SCORES";

    public AddScoreFragment(boolean aiChosen) {
        this.aiChosen = aiChosen;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        rootView = inflater.inflate(R.layout.fragment_add_score, null);

        TextView tvStatistics = (TextView) rootView.findViewById(R.id.dialog_statistics);

        String statistics =  getString(R.string.add_score_players, mScores[0], mScores[1]);

        tvStatistics.setText(statistics);

        if(aiChosen) {
            EditText etPlayerTwo = (EditText) rootView.findViewById(R.id.player_two_name);
            etPlayerTwo.setText(R.string.ai_name);
            etPlayerTwo.setEnabled(false);
        }

        builder.setView(rootView)
                .setPositiveButton(R.string.save_score, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveStatistics();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do not save statistics, dismiss a dialog
                        AddScoreFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle dialogBundle = this.getArguments();
        mScores = dialogBundle.getIntArray(BUNDLE_TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Display the high scores list
        getActivity().startActivity(new Intent(getActivity(), HighScoresActivity.class));
        super.onDismiss(dialog);
    }

    // Insert statistics to a database
    private void saveStatistics() {
        EditText etPlayerOne = (EditText) rootView.findViewById(R.id.player_one_name);
        EditText etPlayerTwo = (EditText) rootView.findViewById(R.id.player_two_name);
        String playerOneName = "" + etPlayerOne.getText();
        String playerTwoName = "" + etPlayerTwo.getText();

        if(!playerOneName.isEmpty()) {
            insertScore(playerOneName, mScores[0]);
            updateUserData(playerOneName, 0);
        }

        if(!playerTwoName.isEmpty()) {
            insertScore(playerTwoName, mScores[1]);
            updateUserData(playerTwoName, 1);
        }
    }

    private void insertScore(String playerName, int score) {
        ContentValues values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, playerName);
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, score);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SungkaContract.HighScoresEntry.COLUMN_DATE, dateFormat.format(new Date()));

        getActivity().getContentResolver().insert(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                values
        );
    }

    private void updateUserData(String playerName, int playerIndex) {
        int[] userData = PlayerData.retrieveUserData(playerName, getActivity());
        boolean isWinner = mScores[playerIndex] > mScores[(playerIndex + 1) % 2];
        boolean isLoser = mScores[playerIndex] < mScores[(playerIndex + 1) % 2];

        if(userData == null) {
            createUserRow(playerName, mScores[playerIndex], isWinner, isLoser);
        } else {
            ContentValues values = new ContentValues();

            int gamesWon = isWinner ? userData[PlayerData.INDEX_GAMES_WON] + 1 : userData[PlayerData.INDEX_GAMES_WON];
            int gamesLost = isLoser ? userData[PlayerData.INDEX_GAMES_LOST] + 1  : userData[PlayerData.INDEX_GAMES_LOST];
            int highScore = mScores[playerIndex] > userData[PlayerData.INDEX_HIGH_SCORE]
                    ? mScores[playerIndex] : userData[PlayerData.INDEX_HIGH_SCORE];

            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED, userData[PlayerData.INDEX_GAMES_PLAYED] + 1);
            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_WON, gamesWon);
            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST, gamesLost);
            values.put(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE, highScore);

            String selectionClause = SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME + " = ?";

            getActivity().getContentResolver().update(
                    SungkaContract.PlayerEntry.CONTENT_URI,
                    values,
                    selectionClause,
                    new String[] {playerName}
            );
        }
    }

    private void createUserRow(String playerName, int score, boolean isWinner, boolean isLoser) {
        ContentValues values = new ContentValues();
        values.put(SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME, playerName);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED, 1);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_WON, isWinner ? 1 : 0);
        values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST, isLoser ? 1 : 0);
        values.put(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE, score);

        getActivity().getContentResolver().insert(
                SungkaContract.PlayerEntry.CONTENT_URI,
                values
        );
    }

}
