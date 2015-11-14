package uk.ac.kcl.teamraccoon.sungka;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import uk.ac.kcl.teamraccoon.sungka.highscores.HighScoresActivity;

public class MainMenu extends AppCompatActivity {
    public final static String GAME_OPTION = "com.uk.ac.kcl.teamraccoon.sungka.OPTION_P1_P2";
    private static boolean activityVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startP1P2(View view) {
        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.putExtra(GAME_OPTION, "P1P2");
        startActivity(intent);
    }

    public void startP1Comp(View view) {
        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.putExtra(GAME_OPTION, "P1Comp");
        startActivity(intent);
    }

    public void startMultiplayer(View view) {
        Intent intent = new Intent(this, MultiplayerMenu.class);
        startActivity(intent);
    }

    public void showHighScores(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        activityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    public boolean isActivityVisible(){
        return activityVisible;
    }
}
