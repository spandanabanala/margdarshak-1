package com.margdarshak.routing;

import com.mapbox.geojson.Point;
import com.margdarshak.callback.OSRMCallback;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpCallHandler {
    static OkHttpClient client = new OkHttpClient();
    public static void getOSRMRoute(Point origin, Point destination){
        Request request = new Request.Builder()
                .url("http://34.93.158.237:5000/route/v1/driving/-6.259345,53.351471;-6.250504,53.342658?steps=true")
                .build();

        client.newCall(request).enqueue(new OSRMCallback());
    }
}
