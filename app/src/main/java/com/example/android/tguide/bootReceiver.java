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
import java.util.Calendar;

/**
 * Created by zacha_000 on 2018-02-21.
 * bootReceiver to handle booting and reset alarms after they were disabled.
 */

public class bootReceiver extends BroadcastReceiver {
    Context mContext;
    String TAG = "bootReceiver";

    // Date to store time for alarms
    Calendar mCalendar;

    // Create variable to store repeat time
    long mRepeatTime;

    // Create time variables in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

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
        begin_notifications(1, mContext.getString(R.string.welcomeNotificationTitle), mContext.getString(R.string.welcomeNotificationDescrip), determineNextTime(), 60 * 1000, "true");

        // Initilize calendar
        mCalendar = Calendar.getInstance();

        // Restore set reminders
        restoreReminder();
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
    public void begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime, String mActive) {
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
    }

    public long determineNextTime() {
        return (System.currentTimeMillis() + System.currentTimeMillis() % (5*60*1000));
    }

    // Restore the created reminders
    public void restoreReminder () {

        // Grab DB and all datapoints
        ReminderDBHelper handler = new ReminderDBHelper(mContext);
        Cursor cursor = handler.getAllReminders();

        // Iterate through cursor data and restore set alarms

        // Grab data from cursor
        // Extra properties from cursor
        cursor.moveToFirst();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TIME));
                String mRepeat = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEAT));
                String mRepeatNo = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATNUM));
                String mRepeatType = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATTYPE));
                String active = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_SOUND));
                String uniqueID = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_UNIQUE_ID));

                // Get time in milliseconds

                //Grab dash values for index
                int dash1 = date.indexOf("/");
                int dash2 = date.indexOf("/", dash1 + 1);

                Log.i(TAG, uniqueID);
                Log.i(TAG,date);
                Log.i(TAG,date);
                Log.i(TAG,date.substring(dash2 + 1));
                Log.i(TAG,date.substring(dash1 + 1, dash2));
                Log.i(TAG,date.substring(0, dash1));
                Log.i(TAG,time.substring(0,2));
                Log.i(TAG,time.substring(3));

                mCalendar.set(Calendar.YEAR, Integer.valueOf(date.substring(dash2 + 1)));
                mCalendar.set(Calendar.MONTH, Integer.valueOf(date.substring(dash1 +1, dash2)) - 1); //-1 b/c one month behind
                mCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.substring(0, dash1)));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.substring(0,2)));
                mCalendar.set(Calendar.MINUTE, Integer.valueOf(time.substring(3)));
                mCalendar.set(Calendar.SECOND, 0);

                long timeInMs = mCalendar.getTimeInMillis();

                // Check repeat type
                if (mRepeat.equals("true")) {
                    if (mRepeatType.equals("Minute")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                    } else if (mRepeatType.equals("Hour")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                    } else if (mRepeatType.equals("Day")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                    } else if (mRepeatType.equals("Week")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                    } else if (mRepeatType.equals("Month")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                    }
                }

                // Pass data to set alarm
                begin_notifications(Integer.valueOf(uniqueID), title, description, timeInMs, mRepeatTime, active);
            }
        } finally {
            cursor.close();
        }
    }
}
