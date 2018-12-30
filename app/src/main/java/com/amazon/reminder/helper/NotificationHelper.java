package com.amazon.reminder.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.amazon.reminder.activity.MainActivity;
import com.amazon.reminder.R;
import com.google.inject.Inject;

public class NotificationHelper {
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    @Inject
    public NotificationHelper(Context mContext, NotificationManager mNotificationManager) {
        this.mContext = mContext;
        this.mNotificationManager = mNotificationManager;
    }

    public void notify(String title) {
        PendingIntent pendingIntentForNotification = PendingIntent.getActivity(
                mContext.getApplicationContext(),
                0,
                new Intent(mContext.getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle("Reminder")
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_baseline_alarm_on)
                .setAutoCancel(true)
                .setContentIntent(pendingIntentForNotification)
                .build();
        mNotificationManager.notify(1, notification);
    }
}
