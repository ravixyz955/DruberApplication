package com.example.user.druberapplication;

import android.app.Application;
import android.content.res.Configuration;

import com.google.android.gms.security.ProviderInstaller;
import com.squareup.leakcanary.LeakCanary;

import javax.net.ssl.SSLContext;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this))
            return;
        LeakCanary.install(this);
        initializeSSLContext();
    }

    private void initializeSSLContext() {
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}

