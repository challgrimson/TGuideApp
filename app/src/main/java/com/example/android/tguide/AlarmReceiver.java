package com.example.android.tguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;


public class AlarmReceiver extends BroadcastReceiver {
    // Class creates a notification and integer to be used
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID ="notification-id";

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

