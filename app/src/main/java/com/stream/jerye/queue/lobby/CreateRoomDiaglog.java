package com.stream.jerye.queue.lobby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stream.jerye.queue.PreferenceUtility;
import com.stream.jerye.queue.R;
import com.stream.jerye.queue.firebase.FirebaseEventBus;
import com.stream.jerye.queue.room.RoomActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jerye on 7/1/2017.
 */

public class CreateRoomDiaglog extends DialogFragment {
    @BindView(R.id.room_title)
    EditText roomTitleEditText;
    @BindView(R.id.room_password)
    EditText roomPasswordEditText;

    private String mRoomTitle;
    private String mRoomPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.create_room_dialog, null);
        ButterKnife.bind(this, dialogView);
        PreferenceUtility.initialize(getActivity());

        roomTitleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });
        roomPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });
        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        return builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button negativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRoomTitle = roomTitleEditText.getText().toString();
                    mRoomPassword = roomPasswordEditText.getText().toString();

                    if (!mRoomTitle.equals("")) {

                        FirebaseEventBus.RoomDatabaseAccess roomDatabaseAccess = new FirebaseEventBus.RoomDatabaseAccess(getActivity());
                        roomDatabaseAccess.push(mRoomTitle, mRoomPassword);

                        PreferenceUtility.setPreference(PreferenceUtility.ROOM_TITLE, mRoomTitle);
                        PreferenceUtility.setPreference(PreferenceUtility.ROOM_PASSWORD, mRoomPassword);

                        Intent intent = new Intent(getActivity(), RoomActivity.class)
                                .setAction(LobbyActivity.ACTION_NEW_USER)
                                .putExtra("room title", mRoomTitle)
                                .putExtra("room password", mRoomPassword);
                        startActivity(intent);

                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.dialog_room_title_blank), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
