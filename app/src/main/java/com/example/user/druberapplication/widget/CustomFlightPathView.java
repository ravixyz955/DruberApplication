package com.example.user.druberapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.druberapplication.FlightPathMapActivity;
import com.example.user.druberapplication.FlightPathsActivity;
import com.example.user.druberapplication.R;
import com.example.user.druberapplication.network.model.FinalPath;
import com.example.user.druberapplication.network.model.FlightPaths;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.service.UserAPIService;
import com.example.user.druberapplication.utils.NetworkUtils;
import com.example.user.druberapplication.utils.Utils;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.turf.TurfMeta;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@SuppressLint("ViewConstructor")
public class CustomFlightPathView extends LinearLayout {

    private static final float ROTATE_FROM = 30.0f;
    private static final float ROTATE_TO = 360.0f;
    @BindView(R.id.altitude_txt)
    TextView altitude_txt;
    @BindView(R.id.gridtype)
    TextView gridType;
    @BindView(R.id.refresh)
    ImageView refresh;
    @BindView(R.id.card_view)
    CardView cardView;
    private String typeSting;
    private Context mContext;
    private FlightPaths flightPaths;
    private ArrayList<FlightPaths> rootFlightPaths;
    private FinalPath finalPath;
    private View itemView;
    private Mission mission;
    private boolean completed, isPending;
    private RotateAnimation r;
    private UserAPIService userAPIService;

    public CustomFlightPathView(Context context, ArrayList<FlightPaths> rootFlightPaths, String typeSting) {
        super(context);
        this.mContext = context;
        this.typeSting = typeSting;
        this.rootFlightPaths = rootFlightPaths;
        init();
    }

    @SuppressLint({"RestrictedApi", "NewApi"})
    private void init() {
        itemView = inflate(mContext, R.layout.flightpath_item_layout, this);
        ButterKnife.bind(this, itemView);
        userAPIService = NetworkUtils.provideUserAPIService(mContext, "https://missions.");
        r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration(2000);
        r.setRepeatCount(Animation.INFINITE);
        refresh.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
        refresh.startAnimation(r);
        /*if (typeSting.contains(Constant.PENDING)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
            } else {
                altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
            }
        } else if (typeSting.contains(Constant.COMPLETE)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            } else {
                altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            }
        }*/
    }

    public void setData(Mission mission, FlightPaths flightPaths, int position) {
        this.flightPaths = flightPaths;
        this.mission = mission;
        ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(VISIBLE);
        userAPIService.getMissionPath(flightPaths.getPath()).enqueue(new Callback<FinalPath>() {
            @SuppressLint({"RestrictedApi", "NewApi"})
            @Override
            public void onResponse(Response<FinalPath> response, Retrofit retrofit) {
                ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(GONE);
                if (response.isSuccess()) {
                    refresh.setVisibility(GONE);
                    r.cancel();
                    finalPath = response.body();
                    if (flightPaths.getEnd_time() != null && !flightPaths.getEnd_time().isEmpty()
                            && flightPaths.getStart_time() != null && !flightPaths.getStart_time().isEmpty()) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                        }
                    } else if (flightPaths.getLatlng() != null && !flightPaths.getLatlng().isEmpty()
                            && flightPaths.getEnd_time() == null) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.mapbox_navigation_route_layer_congestion_red)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.mapbox_navigation_route_layer_congestion_red)));
                        }
                    } else if (flightPaths.getStart_time() == null && flightPaths.getEnd_time() == null || flightPaths.getStart_time() != null) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
                        }
                    }
                    gridType.setText(finalPath.getGridtype());
                    gridType.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(R.color.colorOrange));
                    altitude_txt.setText(String.format("altitude: %s m", String.valueOf(response.body().getAltitude())));
                } else {
                    Log.d("Error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(GONE);
                Log.d("Failure", "onFailure: " + t.getMessage());
            }
        });

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean not_completed = false;
                boolean not_started = false;
                for (FlightPaths rootFlightPath : rootFlightPaths) {
                    if (rootFlightPath.getStart_time() != null && !rootFlightPath.getStart_time().isEmpty()
                            && rootFlightPath.getEnd_time() == null) {
                        not_completed = true;
                    }
                }
                /*if (not_completed) {
                    if (flightPaths.getLatlng() != null && !flightPaths.getLatlng().isEmpty()) {
                        not_completed = true;
                        isPending = true;
                    }
                }*/
                if (flightPaths.getStart_time() != null && !flightPaths.getStart_time().isEmpty() &&
                        flightPaths.getEnd_time() != null && !flightPaths.getEnd_time().isEmpty())
                    completed = true;

                if (flightPaths.getStart_time() == null && flightPaths.getEnd_time() == null)
                    not_started = true;

                if (completed) {
                    Utils.showToast(mContext, "Path already completed!");
                } else if (not_completed || not_started) {
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    if (finalPath != null) {
                        String geometryType = finalPath.getGeometry().getType();
                        List<Point> points = null;
                        if (geometryType.equalsIgnoreCase("LineString")) {
                            points = TurfMeta.coordAll(LineString.fromJson(Utils.getLineStringJson(finalPath.getGeometry().getCoordinates().toString())));
                        } else if (geometryType.equalsIgnoreCase("Polygon") || geometryType.equalsIgnoreCase("MultiLineString")) {
                            points = TurfMeta.coordAll(Polygon.fromJson(Utils.getNonLineStringJson(finalPath.getGeometry().getCoordinates().toString())), true);
                        }
                        if (points.size() > 0) {
                            for (Point point : points) {
                                latLngs.add(new LatLng(point.latitude(), point.longitude()));
                            }
                        }
                        Intent flightPathIntent = new Intent(mContext, FlightPathMapActivity.class);
                        flightPathIntent.putExtra(Mission.class.getName(), mission);
                        flightPathIntent.putExtra(FinalPath.class.getName(), finalPath);
                        flightPathIntent.putParcelableArrayListExtra(LatLng.class.getName(), latLngs);
                        if (flightPaths.getLatlng() != null)
                            flightPathIntent.putExtra("resume_action_latlng", flightPaths.getLatlng());
                        (mContext).startActivity(flightPathIntent);
                    } else {
                        Utils.showToast(mContext, "No paths found");
                    }
                }/* else
                    Utils.showToast(mContext, "Please complete existing path!");*/
            }
        });
    }
}