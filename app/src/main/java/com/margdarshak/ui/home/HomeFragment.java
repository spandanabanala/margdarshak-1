package com.margdarshak.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.margdarshak.MainActivity;
import com.margdarshak.R;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private MapboxMap mapboxMap;
    private MapView mapView;
    FloatingActionButton myLocationButton;
    private FusedLocationProviderClient fusedLocationClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        myLocationButton = root.findViewById(R.id.locationFAB);
        myLocationButton.setOnClickListener(v -> {
            // Toggle GPS position updates
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation()))
                    .zoom(14) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000, new MapboxMap.CancelableCallback() {
                @Override
                public void onCancel() {

                }
                @Override
                public void onFinish() {
                    mapboxMap.getLocationComponent().setCameraMode(CameraMode.TRACKING);
                    myLocationButton.hide();
                }
            });
        });

        return root;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                style -> {
                    enableLocationComponent(style);
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this.getContext())) {

            // Get an instance of the component
            final LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(this.getContext(), loadedMapStyle)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                @Override
                public void onCameraTrackingDismissed() {
                    myLocationButton.show();
                }

                @Override
                public void onCameraTrackingChanged(int currentMode) {
                }
            });

            locationComponent.getLocationComponentOptions().trackingInitialMoveThreshold();
            locationComponent.getLocationComponentOptions().trackingMultiFingerMoveThreshold();
        }
    }
}