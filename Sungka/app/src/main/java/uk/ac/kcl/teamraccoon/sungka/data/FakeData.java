package uk.ac.kcl.teamraccoon.sungka.data;


import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FakeData {

    Context context;

    public FakeData(Context context) {
        this.context = context;
        Random rnd = new Random();

        String[] players = {"Dominik", "Tahmidul", "Ana", "Rosen", "Ben",
                "Peter Thiel", "Elon Musk", "HRH Reggie the Lion",
                "Virginia Woolf", "John Keats", "Peter Higgs", "James Clerk Maxwell"};

        for (int i = 0; i < players.length; ++i) {
            String playerName = players[i];
            int counter = rnd.nextInt(3) + 5;
            for (int k = 0; k < counter; ++k) {
                int randomInt = rnd.nextInt(50) + 20;
                int[] mScores = {randomInt, 98 - randomInt};
                int randomOpponent = rnd.nextInt(players.length);
                while (randomOpponent == i) {
                    randomOpponent = rnd.nextInt(players.length);
                }

                insertScore(playerName, mScores[0]);
                updateUserData(playerName, 0, mScores);

                insertScore(players[randomOpponent], mScores[1]);
                updateUserData(players[randomOpponent], 1, mScores);
            }
        }

    }

    private void insertScore(String playerName, int score) {
        ContentValues values = new ContentValues();
        values.put(SungkaContract.HighScoresEntry.COLUMN_PLAYER, playerName);
        values.put(SungkaContract.HighScoresEntry.COLUMN_SCORE, score);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SungkaContract.HighScoresEntry.COLUMN_DATE, dateFormat.format(new Date()));

        context.getContentResolver().insert(
                SungkaContract.HighScoresEntry.CONTENT_URI,
                values
        );
    }

    private void updateUserData(String playerName, int playerIndex, int[] mScores) {
        int[] userData = PlayerData.retrieveUserData(playerName, context);
        boolean isWinner = mScores[playerIndex] > mScores[(playerIndex + 1) % 2];
        boolean isLoser = mScores[playerIndex] < mScores[(playerIndex + 1) % 2];

        if (userData == null) {
            createUserRow(playerName, mScores[playerIndex], isWinner, isLoser);
        } else {
            ContentValues values = new ContentValues();

            int gamesWon = isWinner ? userData[PlayerData.INDEX_GAMES_WON] + 1 : userData[PlayerData.INDEX_GAMES_WON];
            int gamesLost = isLoser ? userData[PlayerData.INDEX_GAMES_LOST] + 1 : userData[PlayerData.INDEX_GAMES_LOST];
            int highScore = mScores[playerIndex] > userData[PlayerData.INDEX_HIGH_SCORE]
                    ? mScores[playerIndex] : userData[PlayerData.INDEX_HIGH_SCORE];

            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_PLAYED, userData[PlayerData.INDEX_GAMES_PLAYED] + 1);
            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_WON, gamesWon);
            values.put(SungkaContract.PlayerEntry.COLUMN_GAMES_LOST, gamesLost);
            values.put(SungkaContract.PlayerEntry.COLUMN_HIGH_SCORE, highScore);

            String selectionClause = SungkaContract.PlayerEntry.COLUMN_PLAYER_NAME + " = ?";

            context.getContentResolver().update(
                    SungkaContract.PlayerEntry.CONTENT_URI,
                    values,
                    selectionClause,
                    new String[]{playerName}
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

        context.getContentResolver().insert(
                SungkaContract.PlayerEntry.CONTENT_URI,
                values
        );
    }
}
