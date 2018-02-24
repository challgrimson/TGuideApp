package com.example.android.tguide;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zacha_000 on 2018-02-21.
 * bootReceiver to handle booting and reset alarms after they were disabled.
 */

public class bootReceiver extends BroadcastReceiver {
    Context mContext;
    String TAG = "bootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        Log.i(TAG, "Reseting Welcome Alarm");
        // Grab time from database
        /*
        ReminderDBHelper handler = new ReminderDBHelper(mContext);
        Cursor cursor = handler.getAlarmTime();
        cursor.moveToFirst();
        String time = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.WALARM_ALARN_DATE));
        */
        //Log.i(TAG, time);

        //Reset welcomeAlarm
        begin_notifications(1, mContext.getString(R.string.welcomeNotificationTitle), mContext.getString(R.string.welcomeNotificationDescrip), determineNextTime(), 5* 60 * 1000, "true");
    }

    // Build notification
    public Notification sendNotification(String notificationTitle, String notificationDesp, String mActive) {
        // Set sound for notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (mActive.equals("true")) {
            // Builder Object for notification sound on
            return new Notification.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .setSound(soundUri)
                    .build();
        } else {
            // Builder Object for notification sound off
            return new Notification.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .build();
        }

        // Create intent for this notification
        //Intent resultIntent = new Intent(this, Reminders.class);
        //PendingIntent resultPendingIntnt = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add intent to notification builder object
        //mBuilder.setContentIntent(resultPendingIntnt);
    }

    // Set alarm
    public boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime, String mActive) {
        // Create Notificiton
        Notification notification = sendNotification(notificationTitle, notificationDesp, mActive);
        Intent myIntent = new Intent(mContext, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, ID);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(mContext, ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        // See if repeat and so, set
        if (repeatTime == -1) {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, resultPendingIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, repeatTime, resultPendingIntent);
        }

        return true;
    }

    public long determineNextTime() {
        return (System.currentTimeMillis() + System.currentTimeMillis() % (5*60*1000));
    }

}
