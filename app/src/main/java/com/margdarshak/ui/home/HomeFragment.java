package com.margdarshak.ui.home;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.MapboxService;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.margdarshak.R;
import com.margdarshak.routing.MargdarshakDirection;
import com.margdarshak.routing.OSRMService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private MapboxMap mapboxMap;
    private FloatingActionButton myLocationButton;
    private FloatingActionButton getDirectionButton;
    private ActivityPermissionListener permissionResultListener;
    private TextInputLayout searchTextBox;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityPermissionListener) {
            permissionResultListener = (ActivityPermissionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ActivityPermissionListener");
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                style -> permissionResultListener.requestLocationPermission(new LocationPermissionCallback(mapboxMap, style)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(ViewModelStore::new).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        getDirectionButton = root.findViewById(R.id.get_directions);
        MapView mapView = root.findViewById(R.id.mapView);
        myLocationButton = root.findViewById(R.id.locationFAB);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        searchTextBox = root.findViewById(R.id.search_box);
        searchTextBox.setEndIconOnClickListener(view -> {
            String searchText = this.searchTextBox.getEditText().getText().toString();
            Log.d(TAG, "You searched for: " + searchText);
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(searchTextBox.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            searchTextBox.clearFocus();
        });
        return root;
    }

    private void moveCameraTo(Location target) {
        // Toggle GPS position updates
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(target))
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
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            final LocationComponent locationComponent = mapboxMap.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(getContext(), loadedMapStyle)
                            .build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
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
            myLocationButton.setOnClickListener(v -> moveCameraTo(mapboxMap.getLocationComponent().getLastKnownLocation()));
            getDirectionButton.setOnClickListener(view -> {
                    mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                        Location currentLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
                        // Set the origin location to the Alhambra landmark in Granada, Spain.
                        Point origin = Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude());

                        // Set the destination location to the Plaza del Triunfo in Granada, Spain.
                        Point destination = Point.fromLngLat(-9.1187862,53.283891);

                        initSource(style, origin, destination);

                        initLayers(style);

                        // Get the directions route from the Mapbox Directions API
                        // getRoute(mapboxMap, origin, destination);
                        getRouteCustom(mapboxMap, origin, destination);

                    });
                    Snackbar.make(view, "Http call complete", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            });
        } else {
            final LocationComponent locationComponent = mapboxMap.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                            .builder(getContext(), loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build());
            locationComponent.setLocationComponentEnabled(false);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.getLocationComponentOptions().trackingInitialMoveThreshold();
            locationComponent.getLocationComponentOptions().trackingMultiFingerMoveThreshold();
        }
    }

    private void initSource(@NonNull Style loadedMapStyle, Point origin, Point destination) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID,
                FeatureCollection.fromFeatures(new Feature[] {})));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

        // Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapFactory.decodeResource(getResources(),
                R.drawable.mapbox_marker_icon_default));

        // Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -9f})));
    }

    private void getRouteCustom(MapboxMap mapboxMap, Point origin, Point destination) {
        MapboxService<DirectionsResponse, OSRMService> client = MargdarshakDirection.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .baseUrl("http://34.93.158.237:5000/")
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                Log.d(TAG, "call success with response: " + response);

                // You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.d(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, "No routes found");
                    return;
                }
                Log.d(TAG, "Response from mapbox: " + response.body().toString());
                // Get the directions route
                DirectionsRoute currentRoute = response.body().routes().get(0);

                // Make a toast which displays the route's distance
                Toast.makeText(getContext(), String.format(
                        getString(R.string.directions_activity_toast_message),
                        currentRoute.distance()), Toast.LENGTH_SHORT).show();

                if (mapboxMap != null) {
                    mapboxMap.getStyle( style -> {
                        // Retrieve and update the source designated for showing the directions route
                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                        // Create a LineString with the directions route's geometry and
                        // reset the GeoJSON source for the route LineLayer source
                        if (source != null) {
                            Log.d(TAG, "onResponse: source != null");
                            source.setGeoJson(FeatureCollection.fromFeature(
                                    Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(),
                                            PRECISION_6))));
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Define the events that the fragment will use to communicate
    public interface ActivityPermissionListener extends PermissionsListener{
        void requestLocationPermission(LocationPermissionCallback locationPermissionCallback);
    }

    public class LocationPermissionCallback {
        MapboxMap mapboxMap;
        Style style;

        public LocationPermissionCallback(MapboxMap mapboxMap, Style style) {
            this.mapboxMap = mapboxMap;
            this.style = style;
        }

        public void onGrant() {
            Log.d(TAG, "granted.. now enabling location component");
            enableLocationComponent(style);
        }
        public void onDenial() {
            Log.d(TAG, "denied.. should show location button");
            enableLocationComponent(style);
            myLocationButton.setOnClickListener(v -> permissionResultListener.requestLocationPermission(this));
            myLocationButton.show();
        }
    }
}