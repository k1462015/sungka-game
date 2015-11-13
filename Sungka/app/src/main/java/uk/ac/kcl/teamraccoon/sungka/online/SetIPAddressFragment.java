package uk.ac.kcl.teamraccoon.sungka.online;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import uk.ac.kcl.teamraccoon.sungka.R;

public class SetIPAddressFragment extends DialogFragment {

    private View rootView;
    private String serverIP;
    private OnSetIPListener onSetIPListener;

    public SetIPAddressFragment() {}

    //creates dialog for inputting IP address
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        rootView = inflater.inflate(R.layout.fragment_set_ipaddress, null);

        builder.setView(rootView)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText ipEditText = (EditText) rootView.findViewById(R.id.ip_address_edittext);
                        serverIP = ipEditText.getText().toString();
                        onSetIPListener.OnDialogDismissed(serverIP);
                    }
                });

        return builder.create();
    }

    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.onSetIPListener = (OnSetIPListener) activity;

    }

    //interface for passing serverIP string to another activity
    public static interface OnSetIPListener {
        void OnDialogDismissed(String serverIP);
    }

}

