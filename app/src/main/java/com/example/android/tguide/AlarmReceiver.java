package com.example.android.tguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    // Class creates a notification and integer to be used
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID ="notification-id";
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
            // Finish notification and send it

            // Fet instant of notification manager
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Grab notification and ID
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            int ID = intent.getIntExtra(NOTIFICATION_ID, 0);

            // Build and issue notification
            mNotifyMgr.notify(ID, notification);
    }
}

