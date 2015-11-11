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
        saveStatistics("Dominik", 13);
        saveStatistics("Tahmidul", 14);
        saveStatistics("Ana", 11);
        saveStatistics("Ben", 11);
        saveStatistics("Rosen", 12);
        saveStatistics("Tim Cook", 12);
        saveStatistics("Peter Thiel", 16);
        saveStatistics("Elon Musk", 12);
        saveStatistics("Larry Page", 12);
        saveStatistics("HRH Reggie the Lion", 11);
        saveStatistics("Virginia Woolf", 17);
        saveStatistics("John Keats", 15);
        saveStatistics("Peter Higgs", 17);
        saveStatistics("James Clerk Maxwell", 18);

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

    private void saveStatistics(String playerName, int counter) {
        Random rnd = new Random();
        for(int i=0; i<counter; ++i) {
            int randomInt = rnd.nextInt(90);
            int[] mScores = {randomInt, 98 - randomInt};
            insertScore(playerName, mScores[0]);
            updateUserData(playerName, 0, mScores);

            insertScore(playerName, mScores[1]);
            updateUserData(playerName, 1, mScores);
        }
    }

    private void updateUserData(String playerName, int playerIndex, int[] mScores) {
        int[] userData = PlayerData.retrieveUserData(playerName, context);
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

            context.getContentResolver().update(
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

        context.getContentResolver().insert(
                SungkaContract.PlayerEntry.CONTENT_URI,
                values
        );
    }
}
