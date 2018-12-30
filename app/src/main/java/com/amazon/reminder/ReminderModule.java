package com.amazon.reminder;

import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ReminderModule extends AbstractModule {
    private final Context mContext;

    ReminderModule(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void configure() {
    }

    @Provides
    public JobScheduler getJobScheduler() {
        return  (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public NotificationManager getNotificationManager() {
        return  (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
