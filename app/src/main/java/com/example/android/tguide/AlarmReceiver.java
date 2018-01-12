package com.example.android.tguide;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;


public class AlarmReceiver  {
    /* Old Alarm stuff -> needed to extend BroadcastReciever
    // Class creates a notification to be used
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
    }*/

}

