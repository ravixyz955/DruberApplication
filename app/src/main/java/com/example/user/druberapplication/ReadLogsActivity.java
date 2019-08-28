package com.example.user.druberapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.druberapplication.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadLogsActivity extends AppCompatActivity {
    private TextView read_logs;
    private String[] permissions;
    private int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_logs);
        setTitle("Log");
        read_logs = findViewById(R.id.log_txt);

        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!hasPermissions(this, permissions))
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        else {
//            writeToFile();
            read_logs.setText(Utils.readLogs().toString());
        }

    }

    private void writeToFile() {
        File path = getExternalFilesDir(null);
        File file = new File(path, "internalapp_log.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(Utils.readLogs().toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFile() {
        File path = getExternalFilesDir(null);
        File file = new File(path, "internalapp_log.txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);
            String content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
//                writeToFile();
                read_logs.setText(Utils.readLogs().toString());
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
