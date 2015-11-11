package uk.ac.kcl.teamraccoon.sungka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import uk.ac.kcl.teamraccoon.sungka.online.SetIPAddressFragment;

public class MultiplayerMenu extends AppCompatActivity implements SetIPAddressFragment.OnSetIPListener {

    public final static String GAME_OPTION = "com.uk.ac.kcl.teamraccoon.sungka.OPTION_P1_P2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String option = data.getStringExtra(MainActivity.GAME_EXIT);
                if(option.equals("HostConnectFail")) {
                    Toast toast = Toast.makeText(this,"Server not initialised or incorrect IP address",Toast.LENGTH_LONG);
                    toast.show();
                } else if(option.equals("ServerInitialiseFail")) {
                    Toast toast = Toast.makeText(this,"Server could not be initialised. Try again",Toast.LENGTH_LONG);
                    toast.show();
                } else if(option.equals("ConnectionLostToClient") || option.equals("ConnectionLostToServer")) {
                    Toast toast = Toast.makeText(this,"Sorry, connection with opponent was lost",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }

    }

    public void startAsHost(View view ){
        if(isConnectedToWifi()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GAME_OPTION, "Server");
            startActivityForResult(intent, 1);
        } else {
            Toast toast = Toast.makeText(this,"You are not currently connected to WiFi",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void startAsClient(View view){
        if(isConnectedToWifi()) {
            displayIPDialog();
        } else {
            Toast toast = Toast.makeText(this,"You are not currently connected to WiFi",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void displayIPDialog() {

        SetIPAddressFragment setIPDialog = new SetIPAddressFragment();
        setIPDialog.show(getFragmentManager(), "fragment_set_ipaddress");

    }

    @Override
    public void OnDialogDismissed(String serverIP) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(GAME_OPTION, "Client");
        startActivityForResult(intent,1);
        MainActivity.setServerIP(serverIP);
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

    public void returnToMainMenu(View view) {
        finish();
    }

    private boolean isConnectedToWifi() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentActiveNetwork = connectivityManager.getActiveNetworkInfo();
        if(currentActiveNetwork != null) {

            if(currentActiveNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }

        }

        return false;

    }

}
