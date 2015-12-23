package com.example.jonas.firststeps.app;

import android.app.Application;

import timber.log.Timber;

public class FirstStepApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
