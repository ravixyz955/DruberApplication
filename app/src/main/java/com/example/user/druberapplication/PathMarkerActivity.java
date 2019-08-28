package com.example.user.druberapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.user.druberapplication.network.model.PathMarker;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class PathMarkerActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {
    private static final String PATH_GEOJSON_SOURCE = "path_geojson_source";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    public MapboxMap map;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.styleMapView)
    ImageView styleMapView;
    @BindView(R.id.progress_loader)
    ContentLoadingProgressBar progressLoader;
    @BindView(R.id.progress_bar)
    FrameLayout progressBar;
    @Nullable
    @BindView(R.id.numberpicker)
    NumberPicker numberPicker;
    @BindView(R.id.length_txt)
    TextView length_txt;
    @BindView(R.id.time_txt)
    TextView time_txt;
    @BindView(R.id.my_location)
    ImageView my_location;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private UserAPIService userAPIService;
    private Unbinder unbinder;
    private PathMarker pathMarker;
    private Location location;
    private PopupWindow settingsPopupWindow, batteyPopupWindow;
    private boolean isSettings, isBattey;
    private int numberPickerLength = 7;
    private int startVal = 40;
    private String[] numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_path_marker);
        ButterKnife.bind(this);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        styleMapView.setTag("older");
        mapView.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mapView.getMapAsync(this);
        numbers = new String[numberPickerLength];

        for (int i = 0; i < numberPickerLength; i++) {
            if (i == 0)
                startVal = 40;
            else
                startVal += 10;
            numbers[i] = startVal + " " + "m";
        }
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMaxValue(6);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.settings:
                if (batteyPopupWindow != null && batteyPopupWindow.isShowing()) {
                    batteyPopupWindow.dismiss();
                    isBattey = false;
                }
                if (!isSettings) {
                    isSettings = true;
                    int width = (int) ((int) displaymetrics.widthPixels * 0.45);
                    int height = (int) ((int) displaymetrics.heightPixels * 0.47);
                    View customView = getLayoutInflater().inflate(R.layout.settings_options_menu, null);
                    TextView angle = customView.findViewById(R.id.angle_txt);
                    TextView overlap = customView.findViewById(R.id.overlap_txt);
                    angle.setText(String.format("angle: \n45%s", (char) 0x00B0));
                    overlap.setText(String.format("overlap: \n%s", String.format("%1$d%%", 80)));
                    settingsPopupWindow = new PopupWindow(customView, width, height);
//                    settingsPopupWindow.setAnimationStyle(R.style.CustomMenuDialogAnimation);
                    settingsPopupWindow.showAtLocation(coordinatorLayout, Gravity.TOP | Gravity.END, 0, getSupportActionBar().getHeight());
                } else {
                    if (settingsPopupWindow.isShowing())
                        settingsPopupWindow.dismiss();
                    isSettings = false;
                }
                return true;
            case R.id.battery:
                if (settingsPopupWindow != null && settingsPopupWindow.isShowing()) {
                    settingsPopupWindow.dismiss();
                    isSettings = false;
                }
                if (!isBattey) {
                    isBattey = true;
                    int width = (int) ((int) displaymetrics.widthPixels * 0.22);
                    int height = (int) ((int) displaymetrics.heightPixels * 0.49);
                    View customView = getLayoutInflater().inflate(R.layout.batery_options_menu, null);
                    batteyPopupWindow = new PopupWindow(customView, width, height);
//                    batteyPopupWindow.setAnimationStyle(R.style.CustomMenuDialogAnimation);
                    batteyPopupWindow.showAtLocation(coordinatorLayout, Gravity.TOP | Gravity.RIGHT, 0, getSupportActionBar().getHeight());
                } else {
                    if (batteyPopupWindow.isShowing())
                        batteyPopupWindow.dismiss();
                    isBattey = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        progressBar.setVisibility(View.VISIBLE);
        map = mapboxMap;
        map.setStyle(Style.MAPBOX_STREETS, new MapboxMap.OnStyleLoadedListener() {
            @Override
            public void onStyleLoaded(@NonNull String style) {
                enableLocationPlugin();
                Location location = new Location("");
                location.setLatitude(17.46924927447053);
                location.setLongitude(78.39971931864206);
                setCameraPosition(location);
                getPathMarker();
            }
        });
    }

    private void getPathMarker() {
        userAPIService.getPathMarkerList().enqueue(new Callback<PathMarker>() {
            @Override
            public void onResponse(Response<PathMarker> response, Retrofit retrofit) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    pathMarker = response.body();
                    List<List<Double>> vGridlists = pathMarker.getFeatures().get(0).getGeometry().getVgrid().getCoordinates();
                    List<List<Double>> Hridlists = pathMarker.getFeatures().get(0).getGeometry().getHgrid().getCoordinates();
                    ArrayList<Point> points = new ArrayList<>();
                    ArrayList<LatLng> vGridLatLng = new ArrayList<>();
                    ArrayList<LatLng> hGridLatLng = new ArrayList<>();
                    ArrayList<LatLng> routeCoordinates = new ArrayList<>();

                    for (List<Double> vGridlist : vGridlists) {
                        double lat, lng;
                        lat = vGridlist.get(1);
                        lng = vGridlist.get(0);
                        vGridLatLng.add(new LatLng(lat, lng));
                        points.add(Point.fromLngLat(lat, lng));
                    }
                    for (List<Double> hGridlist : Hridlists) {
                        double lat, lng;
                        lat = hGridlist.get(1);
                        lng = hGridlist.get(0);
                        hGridLatLng.add(new LatLng(lat, lng));
                        points.add(Point.fromLngLat(lat, lng));
                    }

                    routeCoordinates.addAll(vGridLatLng);
                    routeCoordinates.addAll(hGridLatLng);

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(routeCoordinates)
                            .color(Color.BLUE)
                            .width(2f);
                    map.addPolyline(polylineOptions);

                    LatLngBounds bounds = new LatLngBounds.Builder().includes(routeCoordinates).build();
                    double height = TurfMeasurement.distance(Point.fromLngLat(bounds.getNorthWest().getLatitude(), bounds.getNorthWest().getLongitude()),
                            Point.fromLngLat(bounds.getSouthWest().getLatitude(), bounds.getSouthWest().getLongitude()), TurfConstants.UNIT_METERS);
                    double width = TurfMeasurement.distance(Point.fromLngLat(bounds.getSouthWest().getLatitude(), bounds.getSouthWest().getLongitude()),
                            Point.fromLngLat(bounds.getSouthEast().getLatitude(), bounds.getSouthEast().getLongitude()), TurfConstants.UNIT_METERS);
                    String widthheight = Math.round(width) + " x " + Math.round(height) + " " + "m";

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (widthheight != null && !widthheight.trim().isEmpty())
                                length_txt.setText(widthheight);
                            time_txt.setText(String.valueOf((5870 / 5870) * 60) + " mins");
                        }
                    });

                    getElavation(routeCoordinates);

                    /* if (points.size() > 0) {
                        ArrayList<Feature> features = new ArrayList<>();
                        for (int i = 0; i < points.size(); i++) {
                            int p1 = i;
                            int p2 = i + 1;
                            if (p2 < points.size()) {
                                double distance = Math.round(TurfMeasurement.distance(points.get(p1), points.get(p2), TurfConstants.UNIT_METERS));
                                if (distance > 5) {
                                    JSONObject featureJson = new JSONObject();
                                    JSONObject emptyJson = new JSONObject();
                                    JSONObject geometry = new JSONObject();
                                    JSONArray coordsJson = new JSONArray();
                                    try {
                                        featureJson.put("type", "Feature");
                                        featureJson.put("properties", emptyJson);
                                        geometry.put("type", "Point");
                                        coordsJson.put(points.get(i).latitude());
                                        coordsJson.put(points.get(i).longitude());
                                        geometry.put("coordinates", coordsJson);
                                        featureJson.put("geometry", geometry);
                                        features.add(Feature.fromJson(featureJson.toString()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
                        GeoJsonSource polesGeoJsonSource = new GeoJsonSource(PATH_GEOJSON_SOURCE, featureCollection);
                        map.addSource(polesGeoJsonSource);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapbox_marker_icon_default);
                        map.addImage("camera", bitmap);
                        SymbolLayer myLayer = new SymbolLayer("my.layer.id", PATH_GEOJSON_SOURCE);
                        myLayer.withProperties(PropertyFactory.iconImage("camera"));
                        map.addLayer(myLayer);
                    }
*/
                } else {
                    progressBar.setVisibility(View.GONE);
                    Utils.getSnackbar(coordinatorLayout, "Unable to fetch data.");
                    Log.d("Error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                Utils.getSnackbar(coordinatorLayout, "Unable to fetch data.");
                Log.d("Failure", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getElavation(ArrayList<LatLng> routeCoordinates) {
        ArrayList<Feature> features = new ArrayList<>();
        HashMap<String, Bitmap> elevs1Map = null;
        HashMap<String, Bitmap> elevsMap = new HashMap<>();
        for (LatLng routeCoordinate : routeCoordinates) {
            features.add(Feature.fromGeometry(Point.fromLngLat(routeCoordinate.getLongitude(), routeCoordinate.getLatitude())));
        }

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource polesGeoJsonSource = new GeoJsonSource(PATH_GEOJSON_SOURCE, featureCollection);
        map.addSource(polesGeoJsonSource);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_callout, null);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.mapbox_marker_icon_default);
//        Bitmap bitmap2 = SymbolGenerator.generate(view);

        map.addImage("camera", bitmap1);

//        map.addImage("camera1", bitmap2);
        SymbolLayer myLayer = new SymbolLayer("my.layer.id", PATH_GEOJSON_SOURCE);
//        SymbolLayer myLayer1 = new SymbolLayer("my1.layer.id", PATH_GEOJSON_SOURCE);
        myLayer.withProperties(iconImage("camera"),
                iconAllowOverlap(true)/*,
                            iconOffset(new Float[]{-10f, -25f}),
                            iconAnchor(ICON_ANCHOR_TOP_RIGHT)*/);
/*
        myLayer1.withProperties(iconImage("camera1"),
                iconAllowOverlap(true),
                iconOffset(new Float[]{-2f, -25f}),
                iconAnchor(ICON_ANCHOR_BOTTOM));
*/
        map.addLayer(myLayer);
//        map.addLayer(myLayer1);


        for (int i = 0; i < routeCoordinates.size(); i++) {

            LatLng latLng = routeCoordinates.get(i);
            int finalI = i;
            int finalI1 = i;
            userAPIService.getElevation(String.valueOf(latLng.getLatitude()), String.valueOf(latLng.getLongitude())).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Response<Object> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
//                        Drawable drawable = ContextCompat.getDrawable(PathMarkerActivity.this, R.drawable.icon);
//                        Bitmap icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//                        Canvas canvas = new Canvas(icon);
//                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//                        drawable.draw(canvas);
//                        HashMap<String, String> elev = new HashMap<>();
                        FeatureCollection featureCollection = FeatureCollection.fromJson(response.body().toString());
                        List<Feature> features = featureCollection.features();
                        View view = getLayoutInflater().inflate(R.layout.layout_callout, null);
                        String name = null;
                        if (features.get(features.size() - 1).properties().has("ele")) {
                            name = features.get(features.size() - 1).properties().get("ele").getAsString();
                            TextView titleTv = view.findViewById(R.id.title);
                            titleTv.setText(name);
                            Bitmap bitmap = SymbolGenerator.generate(view);
                            elevsMap.put(String.valueOf(finalI1), bitmap);
                            if (elevsMap.size() == routeCoordinates.size() - 1) {
                                /*runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        map.addImages(elevsMap);
                                    }
                                });*/
                                Log.d("MSPSPSP", "getElavation: " + finalI1);
                            }
                        }

/*
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(IconFactory.getInstance(PathMarkerActivity.this).fromBitmap(icon))
                                .title(features.get(features.size() - 1).properties().get("ele").getAsString()));
*/

                    } else {
                        Log.d("Error", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Failure: ", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setRenderMode(RenderMode.COMPASS);
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
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
//            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16), 2000);
//        getPathMarker();
    }

    @OnClick(R.id.numberpicker)
    public void scollNumber() {

    }

    @OnClick(R.id.styleMapView)
    public void applyStyle() {
        if (styleMapView.getTag().equals("older")) {
            styleMapView.setTag("newer");
            getPathMarker();
            styleMapView.setBackgroundResource(R.drawable.map_sample);
            map.setStyle(Style.SATELLITE);

        } else {
            styleMapView.setTag("older");
            getPathMarker();
            styleMapView.setBackgroundResource(R.drawable.street_view);
            map.setStyle(Style.MAPBOX_STREETS);
        }
    }

    @OnClick(R.id.my_location)
    public void currentlocation() {
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16), 2000);
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})

    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.location = location;
//            setCameraPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
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
}