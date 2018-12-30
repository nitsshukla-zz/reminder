package com.amazon.reminder.helper;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.amazon.reminder.model.ReminderModel;
import com.amazon.reminder.service.ReminderService;
import com.google.inject.Inject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReminderJobHelper {
    public static final String TITLE_TAG = "title";
    private final ComponentName jobServiceComponent;
    private final JobScheduler jobScheduler;

    @Inject
    public ReminderJobHelper(JobScheduler jobScheduler, Context mContext) {
        this.jobServiceComponent = new ComponentName(mContext, ReminderService.class);
        this.jobScheduler = jobScheduler;
    }

    public void triggerReminder(ReminderModel reminderModel) {
        JobInfo.Builder jobBuilder = getTemplateBuilder(reminderModel);
        long diff = reminderModel.getCompleteDate().getTime().getTime() - Calendar.getInstance().getTime().getTime();
        if (diff < 0 ) {
            throw new RuntimeException("Back dated entry not allowed.");
        }
        jobBuilder.setOverrideDeadline(diff);
        jobBuilder.setMinimumLatency(TimeUnit.MINUTES.toMillis(1));
        jobScheduler.schedule(jobBuilder.build());
    }
    private JobInfo.Builder getTemplateBuilder(ReminderModel reminderModel) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(TITLE_TAG, reminderModel.getTitle());
        return new JobInfo.Builder(reminderModel.getId(), jobServiceComponent)
                .setPersisted(true)
                .setExtras(bundle);
    }
    public void cancelJob(ReminderModel reminderModel) {
        jobScheduler.cancel(reminderModel.getId());
    }
}
