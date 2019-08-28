package com.example.user.druberapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.chip.Chip;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import com.example.user.druberapplication.utils.Utils;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PracticeActivity extends AppCompatActivity {

    private static final float ROTATE_FROM = 30.0f;
    private static final float ROTATE_TO = 360.0f;
    Date date;
    //    @BindView(R.id.linlay)
//    TableLayout table;
    private Unbinder unbinder;
    private RotateAnimation r;
    private Button click_btn;
    private Chip email_chip;

    public static Intent getUninstallAppIntent(final String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        unbinder = ButterKnife.bind(this);
        click_btn = findViewById(R.id.click_btn);
        email_chip = findViewById(R.id.email);
        date = new Date();

/*
        for (int i = 0; i < 40; i++) {

            TableRow tr = new TableRow(this);
            for (int j = 0; j < 40; j++) {
                TextView txtGeneric = new TextView(this);
                txtGeneric.setTextSize(18);
                txtGeneric.setBackground(getDrawable(R.drawable.border_table));
                txtGeneric.setText("item " + j);
                tr.addView(txtGeneric);
                */
        /*txtGeneric.setHeight(30); txtGeneric.setWidth(50);   txtGeneric.setTextColor(Color.BLUE);*//*

            }
            table.addView(tr);
        }
*/
//        Intent appDetailsSettingsIntent = getAppDetailsSettingsIntent("com.example.user.druberapplication");
//        startActivity(appDetailsSettingsIntent);

//        Intent uninstallAppIntent = getUninstallAppIntent("com.example.user.druberapplication");
//        startActivity(uninstallAppIntent);

//        startActivity(getShutdownIntent());

//        openGpsSettings(this);
//        openWirelessSettings(this);


        click_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListenerActivity().setClickListener(new ListenerActivity.ClickListener() {
                    @Override
                    public void clicked() {
                        Utils.showToast(PracticeActivity.this, "Listener Success!");
                    }
                });
            }
        });
    }

    public Intent getAppDetailsSettingsIntent(final String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public Intent getShutdownIntent() {
        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void openWirelessSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
