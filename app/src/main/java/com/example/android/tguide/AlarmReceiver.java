package com.example.android.tguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION = "notification";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Finish notification and send it
        // Create id for notification
        int mNotificationId = 001;

        // Fet instant of notification manager
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Grab notification
        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        // Build and issue notification
        mNotifyMgr.notify(mNotificationId, notification);
    }
}
