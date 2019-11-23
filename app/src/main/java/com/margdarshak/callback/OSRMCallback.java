package com.margdarshak.callback;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OSRMCallback implements Callback {
    public static final String TAG = OSRMCallback.class.getSimpleName();

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "call failed with exception: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, "call success with response: " + response);
    }
}
