package com.example.user.druberapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class CheckListActivity extends AppCompatActivity {
    //    @BindView(R.id.connected_to_drone)
//    CheckBox connected_to_drone;
//    @BindView(R.id.camera_is_ready)
//    CheckBox camera_is_ready;
//    @BindView(R.id.home_point)
//    CheckBox home_point;
//    @BindView(R.id.missoin_uploading)
//    CheckBox missoin_uploading;
//    @BindView(R.id.drone_storage)
//    CheckBox drone_storage;
//    @BindView(R.id.drone_gps)
//    CheckBox drone_gps;
//    @BindView(R.id.cancel_btn)
//    Button cancel_btn;
//    @BindView(R.id.resolve_issues_btn)
//    Button resolve_issues_btn;
    @Nullable
    @BindView(R.id.numberpicker)
    NumberPicker numberPicker;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_options_menu);
        unbinder = ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        numberPicker.setMaxValue(100);
//        numberPicker.setMinValue(0);
//        numberPicker.setWrapSelectorWheel(true);

//        popCheckListDialog();
    }

    private void popCheckListDialog() {
        View checkListView = LayoutInflater.from(this).inflate(R.layout.checklist_layout, null);
        unbinder = ButterKnife.bind(this, checkListView);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) ((int) displaymetrics.widthPixels * 0.8);
        int height = (int) ((int) displaymetrics.heightPixels * 0.9);

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(checkListView);
        alertDialog.show();
        alertDialog.getWindow().setLayout(width, height);

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connected_to_drone.setChecked(true);
                connected_to_drone.setText("Connected to drone");

                camera_is_ready.setChecked(true);
                camera_is_ready.setText("Camera is ready");

                home_point.setChecked(true);
                home_point.setText("Homepoint is set");

                missoin_uploading.setChecked(true);
                missoin_uploading.setText("Mission uploading to drone");

                drone_storage.setChecked(true);
                drone_storage.setText("Drone storage status known");

                drone_gps.setChecked(true);
                drone_gps.setText("Drone GPS satillites found");
            }
        }, 2000);
*/
    }

    @Optional
    @OnClick(R.id.numberpicker)
    public void numberPicker() {
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
    }

    @Optional
    @OnClick(R.id.cancel_btn)
    public void CloseDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
