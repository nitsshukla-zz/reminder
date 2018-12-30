package com.amazon.reminder.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.amazon.reminder.ReminderApplication;
import com.amazon.reminder.helper.NotificationHelper;
import com.amazon.reminder.helper.ReminderJobHelper;

public class ReminderService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("amazon", "It came");
        ((ReminderApplication)getApplicationContext())
                .getInjector()
                .getInstance(NotificationHelper.class)
                .notify(jobParameters.getExtras().getString(ReminderJobHelper.TITLE_TAG));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
