package com.amazon.reminder;

import android.app.Application;

import com.google.inject.Injector;
import com.google.inject.Stage;

import roboguice.RoboGuice;

public class ReminderApplication extends Application {
    private Injector injector;
    @Override
    public void onCreate() {
        super.onCreate();
        RoboGuice.getOrCreateBaseApplicationInjector(this,
                Stage.DEVELOPMENT,
                RoboGuice.newDefaultRoboModule(this),
                new ReminderModule(getApplicationContext()));
        injector = RoboGuice.getInjector(this);
    }

    public Injector getInjector() {
        return injector;
    }
}
