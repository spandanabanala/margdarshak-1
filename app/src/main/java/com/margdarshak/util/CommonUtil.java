package com.margdarshak.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.margdarshak.R;

public class CommonUtil {

    public static void updateSignInResult(GoogleSignInAccount account, int screenID, FragmentActivity activity) {
        if(account != null) {
            // Modify App navigation bar
            ((TextView) activity.findViewById(R.id.username)).setText(account.getDisplayName());
            ((TextView) activity.findViewById(R.id.usermail)).setText(account.getEmail());
            ((ImageView) activity.findViewById(R.id.userpicture)).setImageURI(account.getPhotoUrl());

            // Modify login screen
            // TODO
        } else if(screenID == CommonConstants.ACTIVITY_SIGN_IN){
            Toast.makeText(activity.getApplicationContext(), "Error signing in to Google account", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity.getApplicationContext(), "Not signed in to Google", Toast.LENGTH_SHORT).show();
        }
    }
}
