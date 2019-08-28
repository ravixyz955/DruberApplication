package com.example.user.druberapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.user.druberapplication.network.model.FinalPath;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FlightPathMapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.progress_loader)
    ContentLoadingProgressBar progressLoader;
    @BindView(R.id.progress_bar)
    FrameLayout progressBar;
    @BindView(R.id.path_start)
    FloatingActionButton pathStart;
    @BindView(R.id.path_stop)
    FloatingActionButton pathStop;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.path_pause)
    FloatingActionButton pathPause;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private MapboxMap map;
    private Mission mission;
    private FinalPath finalPath;
    private ArrayList<LatLng> coordinates;
    private Unbinder unbinder;
    private String latlng;
    private UserAPIService userAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_flight_path_map);
        ButterKnife.bind(this);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        mapView.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent().hasExtra(LatLng.class.getName())) {
            coordinates = getIntent().getParcelableArrayListExtra(LatLng.class.getName());
        }
        if (getIntent().hasExtra(Mission.class.getName())) {
            mission = getIntent().getExtras().getParcelable(Mission.class.getName());
        }
        if (getIntent().hasExtra(FinalPath.class.getName())) {
            finalPath = getIntent().getExtras().getParcelable(FinalPath.class.getName());
        }
        if (getIntent().hasExtra("resume_action_latlng")) {
            latlng = getIntent().getStringExtra("resume_action_latlng");
        }
        mapView.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})

    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Location loc = new Location("");
            loc.setLatitude(coordinates.get(0).getLatitude());
            loc.setLongitude(coordinates.get(0).getLongitude());
            setCameraPosition(loc);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        progressBar.setVisibility(View.GONE);
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 50), 2000);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));
        map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
        addMarkers(finalPath);
    }

    private void addMarkers(FinalPath finalPath) {
        Drawable drawable = ContextCompat.getDrawable(FlightPathMapActivity.this, R.drawable.icon);
        Bitmap icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(icon);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        if (coordinates.size() > 0) {
            map.addPolyline(new PolylineOptions().addAll(coordinates)
                    .color(Color.RED)
                    .width(2f));
            /*for (LatLng coordinate : coordinates) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()))
                        .icon(IconFactory.getInstance(FlightPathMapActivity.this).fromBitmap(icon)));
            }*/
        }
    }

    @OnClick(R.id.path_start)
    public void startPath() {
        doPathAction(mission.get_id(), finalPath.getId(), "START", null, "Started Successfully!");
    }

    @OnClick(R.id.path_stop)
    public void stopPath() {
        doPathAction(mission.get_id(), finalPath.getId(), "END", null, "Completed Successfully!");
    }

    @OnClick(R.id.path_pause)
    public void pausePath() {
       /* String latlng = "17.4275148,78.4105523";
        doPathAction(mission.get_id(), finalPath.getId(), "PAUSE", latlng, "Completed Successfully!");*/
    }

    private void doPathAction(String jobId, String pathId, String actionType, String latlng, String msg) {
        userAPIService.getMissionPathAction(jobId, pathId, actionType, latlng).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Utils.getSnackbar(coordinatorLayout, msg);
                } else {
                    Log.d("Error:", "onError: " + response.errorBody());
                    Utils.getSnackbar(coordinatorLayout, "Unable to process request");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Failure:", "onFailure: " + t.getMessage());
                Utils.getSnackbar(coordinatorLayout, "Unable to process request");
            }
        });
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unbinder.unbind();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        progressBar.setVisibility(View.VISIBLE);
        map = mapboxMap;
//        mapboxMap.setStyle(Style.SATELLITE);
        enableLocationPlugin();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setLocationLayerEnabled(true);
            locationPlugin.setCameraMode(CameraMode.TRACKING);
            locationPlugin.setRenderMode(RenderMode.COMPASS);
            getLifecycle().addObserver(locationPlugin);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeLocationEngine() {
        LocationEngineProvider locationEngineProvider = new LocationEngineProvider(this);
        locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.BALANCED_POWER_ACCURACY);
        locationEngine.addLocationEngineListener(this);
        locationEngine.setInterval(5000);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (latlng == null) {
            setCameraPosition(lastLocation);
        } else if (latlng != null && !latlng.isEmpty()) {
            String[] latlong = latlng.split(",");
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(latlong[0]));
            location.setLongitude(Double.parseDouble(latlong[1]));
            setCameraPosition(location);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }
}