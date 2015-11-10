package uk.ac.kcl.teamraccoon.sungka;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import uk.ac.kcl.teamraccoon.sungka.online.SetIPAddressFragment;

public class MultiplayerMenu extends AppCompatActivity implements SetIPAddressFragment.OnSetIPListener {

    public final static String GAME_OPTION = "com.uk.ac.kcl.teamraccoon.sungka.OPTION_P1_P2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);
        Intent intent = getIntent();
        String option = intent.getStringExtra(MainActivity.GAME_EXIT);
        if(option != null && option.equals("HostConnectFail")) {
            Toast toast = Toast.makeText(this,"Server not initialised or incorrect IP address",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void startAsHost(View view ){
        //Something that makes it starts as host
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(GAME_OPTION, "Server");
        startActivity(intent);
    }

    public void startAsClient(View view){
        //Something that makes it start as client
        displayIPDialog();
    }

    private void displayIPDialog() {

        SetIPAddressFragment setIPDialog = new SetIPAddressFragment();
        setIPDialog.show(getFragmentManager(), "fragment_set_ipaddress");

    }

    private void startClient(String serverIP) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(GAME_OPTION, "Client");
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
    }

    @Override
    public void OnDialogDismissed(String serverIP) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(GAME_OPTION, "Client");
        startActivity(intent);
        MainActivity.setServerIP(serverIP);
    }
}
