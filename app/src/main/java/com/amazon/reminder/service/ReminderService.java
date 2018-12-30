package com.amazon.reminder.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.util.Log;

import com.amazon.reminder.ReminderApplication;
import com.amazon.reminder.activity.AddReminderActivity;
import com.amazon.reminder.helper.NotificationHelper;
import com.amazon.reminder.helper.ReminderJobHelper;
import com.amazon.reminder.model.ReminderModel;
import com.google.gson.Gson;
import com.google.inject.Injector;

public class ReminderService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("amazon", "It came");
        PersistableBundle extras = jobParameters.getExtras();
        String reminderString = extras.getString(AddReminderActivity.REMINDER_MODEL_STRING);
        Injector injector = ((ReminderApplication)getApplicationContext())
                .getInjector();
        ReminderModel reminderModel = injector.getInstance(Gson.class)
                .fromJson(reminderString, ReminderModel.class);
        NotificationHelper notificationHelper = injector.getInstance(NotificationHelper.class);
        notificationHelper.notify(reminderModel);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
