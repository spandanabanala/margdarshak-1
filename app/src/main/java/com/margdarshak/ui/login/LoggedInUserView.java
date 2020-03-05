package com.margdarshak.ui.login;

import android.net.Uri;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String userName;
    private String userEmail;
    private Uri photoURI;

    LoggedInUserView(String userName, String userEmail, Uri photoURI) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.photoURI = photoURI;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }
}
