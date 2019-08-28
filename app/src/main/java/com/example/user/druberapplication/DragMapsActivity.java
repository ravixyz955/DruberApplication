package com.example.user.druberapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.user.druberapplication.network.model.PathMarker;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DragMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    double distance = 129.57;
    private UserAPIService userAPIService;
    private PathMarker pathMarker;
    private ArrayList<LatLng> routeCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_maps);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        getPathMarker();
    }

    private void getPathMarker() {
        userAPIService.getPathMarkerList().enqueue(new Callback<PathMarker>() {
            @Override
            public void onResponse(Response<PathMarker> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pathMarker = response.body();
                    List<List<Double>> vGridlists = pathMarker.getFeatures().get(0).getGeometry().getVgrid().getCoordinates();
                    List<List<Double>> Hridlists = pathMarker.getFeatures().get(0).getGeometry().getHgrid().getCoordinates();
                    ArrayList<Point> points = new ArrayList<>();
                    ArrayList<LatLng> vGridLatLng = new ArrayList<>();
                    ArrayList<LatLng> hGridLatLng = new ArrayList<>();
                    routeCoordinates = new ArrayList<>();

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
                            .color(Color.WHITE)
                            .width(2f);
                    Polyline polyline = mGoogleMap.addPolyline(polylineOptions);

                    LatLngBounds.Builder initialbuilder = new LatLngBounds.Builder();
                    initialbuilder.include(routeCoordinates.get(0))
                            .include(routeCoordinates.get(routeCoordinates.size() - 1));
                    LatLngBounds initialBounds = initialbuilder.build();

                    LatLng targetNorteast = SphericalUtil.computeOffset(initialBounds.northeast, distance, 90);
                    LatLng targetSouthwest = SphericalUtil.computeOffset(initialBounds.southwest, distance, 225);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    builder.include(targetNorteast).include(targetSouthwest);
//                    LatLngBounds finalBounds = builder.build();

                    double n = SphericalUtil.computeOffset(initialBounds.getCenter(), distance / 2, 300).latitude;
                    double s = SphericalUtil.computeOffset(initialBounds.getCenter(), distance, 180).latitude;
                    double e = SphericalUtil.computeOffset(initialBounds.getCenter(), distance, 50).longitude;
                    double w = SphericalUtil.computeOffset(initialBounds.getCenter(), distance / 2, 270).longitude;

                    builder.include(new LatLng(s, w))
                            .include(new LatLng(n, e));
                    LatLngBounds finalBounds = builder.build();

                    drawBounds(finalBounds, Color.RED);
                    Location startPoint = new Location("");
                    startPoint.setLatitude(routeCoordinates.get(0).latitude);
                    startPoint.setLongitude(routeCoordinates.get(0).longitude);
                    Location endPoint = new Location("");
//                    endPoint.setLatitude(routeCoordinates.get(routeCoordinates.size() - 1).latitude);
//                    endPoint.setLongitude(routeCoordinates.get(routeCoordinates.size() - 1).longitude);
                    endPoint.setLatitude(initialBounds.getCenter().latitude);
                    endPoint.setLongitude(initialBounds.getCenter().longitude);
                    Log.d("DISTANCE", "onResponse: " + startPoint.distanceTo(endPoint));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(targetSouthwest, targetNorteast), 50));
                } else {
                    Log.d("Error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Failure", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(17.46924927447053, 78.39971931864206);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mCurrLocationMarker.setDraggable(true);
//        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16), 1000, null);
    }

    private void drawBounds(LatLngBounds bounds, int color) {
        PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                .strokeColor(color)
                .fillColor(getResources().getColor(R.color.colorDarkCyan));

        Polygon polygon = mGoogleMap.addPolygon(polygonOptions);
        polygon.setGeodesic(false);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DragMapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}