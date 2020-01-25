package com.margdarshak.routing;

import android.util.Log;

import com.mapbox.geojson.Point;
import com.margdarshak.callback.OSRMCallback;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpCallHandler {
    static OkHttpClient client = new OkHttpClient();
    public static final String TAG = HttpCallHandler.class.getSimpleName();
    public static void getOSRMRoute(Point origin, Point destination){
        String url="http://34.93.158.237:5000/route/v1/driving/"
                + origin.longitude() + "," + origin.latitude() +
                ";" + destination.longitude() + "," + destination.latitude() + "?steps=true";
        Log.d(TAG, "url created: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new OSRMCallback());
    }
}
