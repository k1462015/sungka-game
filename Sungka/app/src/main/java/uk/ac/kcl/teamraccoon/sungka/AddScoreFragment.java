package uk.ac.kcl.teamraccoon.sungka;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import uk.ac.kcl.teamraccoon.sungka.data.SungkaContract;


/**
 * Display a dialog to save game scores.
 */
public class AddScoreFragment extends DialogFragment {

    private int[] mScores;
    private View rootView;

    public AddScoreFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        rootView = inflater.inflate(R.layout.fragment_add_score, null);

        TextView tvStatistics = (TextView) rootView.findViewById(R.id.dialog_statistics);

        String statistics = "Player One: " + mScores[0] +
                "\nPlayer Two: " + mScores[1];

        tvStatistics.setText(statistics);

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
        mScores = dialogBundle.getIntArray("SCORES");
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
        }

        if(!playerTwoName.isEmpty()) {
            insertScore(playerTwoName, mScores[1]);
        }
    }

    private void insertScore(String playerName, int score) {
        ContentValues values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, playerName);
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, score);

        getActivity().getContentResolver().insert(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                values
        );
    }
}
