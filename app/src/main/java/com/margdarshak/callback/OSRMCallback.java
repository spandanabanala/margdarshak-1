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

public class OSRMCallback implements Callback {
    public static final String TAG = OSRMCallback.class.getSimpleName();

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "call failed with exception: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
