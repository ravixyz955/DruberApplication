package com.example.user.druberapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class IconActivity extends AppCompatActivity {
    private static final int PERMISSION_ALL = 1;
    //    private FlightBroadCastReceiver flightBroadCastReceiver;
    /*@BindView(R.id.flight_paths_recyler_view)
    RecyclerView flightpaths_recycler_view;

    private ArrayList<String> items;
    private MyListAdapter myListAdapter;
    private static IconActivity instance;*/
    private String[] permissions;
    private boolean isRationale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icon_activity);
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(this, permissions)) {
            int count = 0;
            for (String permission : permissions) {
                if (count < 1) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            permission)) {
                        count++;
                        new AlertDialog.Builder(this)
                                .setTitle("Permission needed")
                                .setMessage("This permission is needed because of this and that")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(IconActivity.this,
                                                permissions, PERMISSION_ALL);
                                    }
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        isRationale = true;
                                    }
                                })
                                .create().show();
                    }
                } else
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
                if (isRationale)
                    return;
            }
        }
//        ButterKnife.bind(this);
//        flightBroadCastReceiver = new FlightBroadCastReceiver();
//        registerReceiver(flightBroadCastReceiver, null);
        /*items = new ArrayList<>();
        setInstance(this);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.nav_header_vertical_spacing);
        flightpaths_recycler_view.addItemDecoration(itemDecoration);
        flightpaths_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        myListAdapter = new MyListAdapter(this, items);*/

        /*Intent alarm = new Intent(this, FlightBroadCastReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 3000, pendingIntent);
        }*/
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

   /* public static IconActivity getInstance() {
        return instance;
    }

    public static void setInstance(IconActivity instance) {
        IconActivity.instance = instance;
    }

    public void updateUI(String value) {
        if (items.size() == 0) {
            flightpaths_recycler_view.setAdapter(myListAdapter);
        }
        items.add(value);
        myListAdapter.notifyDataSetChanged();
    }*/
}
