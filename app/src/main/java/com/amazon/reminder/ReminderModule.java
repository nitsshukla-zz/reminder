package com.amazon.reminder;

import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ReminderModule extends AbstractModule {
    private final Context mContext;

    ReminderModule(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Provides
    public JobScheduler getJobScheduler() {
        return  (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public NotificationManager getNotificationManager() {
        return  (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
