package com.margdarshak.callback;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.margdarshak.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;

public class OSRMCallback implements Callback {
    public static final String TAG = OSRMCallback.class.getSimpleName();

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "call failed with exception: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, "call success with response: " + response);
        // You can get the generic HTTP info about the response

        Log.d(TAG, "Response code: " + response.code());

        if (response.body() == null) {
            Log.d(TAG, "No routes found, make sure you set the right user and access token.");
            return;
        } /* else if (response.body().routes().size() < 1) {
            Log.d(TAG, "No routes found");
            return;
        }*/
        else {
            Log.d(TAG, "Response body class: " + ((RealResponseBody) response.body()).contentType());
            Log.d(TAG, "Response from mapbox: " + response.body().string());
        }

    }
}
