package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.numberpicker)
    NumberPicker numberPicker;

    @BindView(R.id.gsd_txt)
    TextView gsd_Txt;

    @BindView(R.id.length_txt)
    TextView length_txt;

    @BindView(R.id.time_txt)
    TextView time_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.map_grids_side_menu);
        ButterKnife.bind(this);

        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
    }

    @Optional
    @OnClick(R.id.numberpicker)
    public void numberPicker() {
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
//                view.setBackgroundColor(getResources().getColor(R.color.colorLightGreen));
            }
        });
    }
}
