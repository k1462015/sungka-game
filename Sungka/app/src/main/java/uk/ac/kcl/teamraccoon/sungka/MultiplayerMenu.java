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

        //checks for a result when game returns to this activity
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String option = data.getStringExtra(GameBoardActivity.GAME_EXIT);
                if (option.equals("HostConnectFail")) {
                    //displays toast if the client could not find the server
                    Toast.makeText(this, getString(R.string.host_connect_fail_toast), Toast.LENGTH_LONG).show();
                } else if (option.equals("ServerInitialiseFail")) {
                    //displays toast if the server could not be initialised for some reason
                    Toast.makeText(this, getString(R.string.server_initialise_fail_toast), Toast.LENGTH_LONG).show();
                } else if (option.equals("ConnectionLostToClient") || option.equals("ConnectionLostToServer")) {
                    //displays toast for if server or client lost connection to opponent
                    Toast.makeText(this, getString(R.string.lost_connection_toast), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    /**
     * for when start as host button is pressed
     *
     * @param view
     */
    public void startAsHost(View view) {
        if (isConnectedToWifi()) {
            //starts GameBoardActivity as server if connected to WiFi
            Intent intent = new Intent(this, GameBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra(GAME_OPTION, "Server");
            startActivityForResult(intent, 1);
        } else {
            //if not connected to WiFi informs user via a toast
            Toast.makeText(this, getString(R.string.network_connection_toast), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * for when start as client button is pressed
     *
     * @param view
     */
    public void startAsClient(View view) {
        if (isConnectedToWifi()) {
            //displays dialog box for inputting IP address
            displayIPDialog();
        } else {
            //if not connected to WiFi informs user via a toast
            Toast.makeText(this, getString(R.string.network_connection_toast), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * displays the SetIPAddressFragment dialog box
     */
    private void displayIPDialog() {

        SetIPAddressFragment setIPDialog = new SetIPAddressFragment();
        setIPDialog.show(getFragmentManager(), "fragment_set_ipaddress");

    }

    /**
     * start activity when dialog box is dismissed
     *
     * @param serverIP inputted IP address
     */
    @Override
    public void OnDialogDismissed(String serverIP) {
        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(GAME_OPTION, "Client");
        startActivityForResult(intent, 1);
        GameBoardActivity.setServerIP(serverIP);
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

    /**
     * checks whether the device is connected to WiFi
     *
     * @return a boolean of whether it is connected to WiFi or not
     */
    private boolean isConnectedToWifi() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentActiveNetwork = connectivityManager.getActiveNetworkInfo();
        if (currentActiveNetwork != null) {
            if (currentActiveNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

}
