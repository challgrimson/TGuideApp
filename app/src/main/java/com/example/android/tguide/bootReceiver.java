package com.example.android.tguide;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private static final long milYear = (milMonth *12L);

    ReminderDBHelper handler;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        handler = new ReminderDBHelper(mContext);

        // Initialize mRpeartTime
        mRepeatTime = -1;

        Log.i(TAG, "Reseting Welcome Alarm");

        //Reset welcomeAlarm
        begin_notifications(1, mContext.getString(R.string.welcomeNotificationTitle), mContext.getString(R.string.welcomeNotificationDescrip), determineNextTime(), 60 * 1000, "true");
        // Grab time from database
        /*
        ReminderDBHelper handler = new ReminderDBHelper(mContext);
        Cursor cursor = handler.getAlarmTime();
        cursor.moveToFirst();
        String time = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.WALARM_ALARN_DATE));
        */
        //Log.i(TAG, time);
        // Initilize calendar
        mCalendar = Calendar.getInstance();

        // Restore set reminders
        restoreReminder();

        // Restore surveillance times
        ///
        // SHOULD BE SET UNDER SURVEILLANCE TIMES BUT CONFIRM
        ///
        //
        //
        //surveillancetimes();
    }

    // Build notification
    public Notification sendNotification(String notificationTitle, String notificationDesp, String mActive) {
        // Set sound for notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(mContext, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (mActive.equals("true")) {
            // Builder Object for notification sound on
            return new Notification.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .setSound(soundUri)
                    .setContentIntent(resultPendingIntent)
                    .build();
        } else {
            // Builder Object for notification sound off
            return new Notification.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .setContentIntent(resultPendingIntent)
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
        Log.i(TAG, "Restoring Reminders");

        Cursor cursor = handler.fetchAllReminders();

        // Iterate through cursor data and restore set alarms

        // Grab data from cursor
        // Extra properties from cursor
        if (cursor != null) {
            cursor.moveToFirst();

            int titleint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TITLE);
            int despint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DESCRIPTION);
            int dateeint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DATE);
            int timeint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TIME);
            int repeatint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEAT);
            int repeatnoint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATNUM);
            int repeattypeint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATTYPE);
            int activeint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_SOUND);
            int idint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_UNIQUE_ID);

            while (!cursor.isAfterLast()) {
                String title = cursor.getString(titleint);
                String description = cursor.getString(despint);
                String date = cursor.getString(dateeint);
                String time = cursor.getString(timeint);
                String mRepeat = cursor.getString(repeatint);
                String mRepeatNo = cursor.getString(repeatnoint);
                String mRepeatType = cursor.getString(repeattypeint);
                String active = cursor.getString(activeint);
                String uniqueID = cursor.getString(idint);

                // Get time in milliseconds

                //Grab dash values for index
                int dash1 = date.indexOf("/");
                int dash2 = date.indexOf("/", dash1 + 1);

                // For storing hour value
                String hour;
                // Grab time values
                if (time.length() == 7) {
                    hour = time.substring(0,1);
                } else {
                    hour = time.substring(0,2);
                }

                // Set am or pm
                int ampm;
                if (time.substring(time.length() - 2).equals("AM")) {
                    ampm = 0;
                } else {
                    ampm = 1;
                }

                Log.i(TAG, uniqueID);
                Log.i(TAG, date);
                Log.i(TAG, date.substring(dash2 + 1));
                Log.i(TAG, date.substring(dash1 + 1, dash2));
                Log.i(TAG, date.substring(0, dash1));
                Log.i(TAG,time);
                Log.i(TAG, hour);
                Log.i(TAG, time.substring(time.length()-5, time.length()-3));
                Log.i(TAG, String.valueOf(mRepeat));
                Log.i(TAG, mRepeatNo);
                Log.i(TAG,mRepeatType);

                mCalendar.set(Calendar.YEAR, Integer.valueOf(date.substring(dash2 + 1)));
                mCalendar.set(Calendar.MONTH, Integer.valueOf(date.substring(dash1 + 1, dash2)) - 1); //-1 b/c one month behind
                mCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.substring(0, dash1)));
                mCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
                mCalendar.set(Calendar.MINUTE, Integer.valueOf(time.substring(time.length()-5, time.length()-3)));
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.AM_PM, ampm);

                long timeInMs = mCalendar.getTimeInMillis();

                // Check repeat type
                if (mRepeat.equals("true")) {
                    if (mRepeatType.equals("Minute(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                    } else if (mRepeatType.equals("Hour(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                    } else if (mRepeatType.equals("Day(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                    } else if (mRepeatType.equals("Week(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                    } else if (mRepeatType.equals("Month(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                    } else if (mRepeatType.equals("Year(s)")) {
                        mRepeatTime = Integer.parseInt(mRepeatNo) * milYear;
                    }
                }

                // Pass data to set alarm
                begin_notifications(Integer.valueOf(uniqueID), title, description, timeInMs, mRepeatTime, active);

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    /*
    public void surveillancetimes() {
        Log.i(TAG, "Reseting Surveillance");

        // Update the serveilant times
        Cursor cursor = handler.fetchsurveillance();

        if (cursor != null) {
            cursor.moveToFirst();

            int id = cursor.getColumnIndexOrThrow(ReminderDBHelper.SURV_ALARM_ID);
            int time = cursor.getColumnIndexOrThrow(ReminderDBHelper.SURV_ALARM_TIME);
            int uniqueid = cursor.getColumnIndexOrThrow(ReminderDBHelper.SURV_UNIQE_ID);

            Log.i(TAG, String.valueOf(id) + String.valueOf(time) + String.valueOf(uniqueid));

            while (!cursor.isAfterLast()) {
                String idvalue = cursor.getString(id);
                String timevalue = cursor.getString(time);
                String uniqueidvalue = cursor.getString(uniqueid);

                Log.i(TAG, idvalue);
                Log.i(TAG, timevalue);
                Log.i(TAG, uniqueidvalue);

                switch (Integer.valueOf(idvalue)) {
                    case 1:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.physicalexamTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue), -1, "false");
                        break;
                    case 6:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.screenbloodTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue), -1, "false");
                        break;
                    case 10:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.celiacTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 11:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.ECGTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 12:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.ECHOTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 13:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.CTMRITitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 14:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.antiproTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 15:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.visionTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 16:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.hearingTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                    case 17:
                        begin_notifications(Integer.valueOf(uniqueidvalue), mContext.getString(R.string.bonedensityTitle),
                                mContext.getString(R.string.physicalexamdesp), Long.valueOf(timevalue) + 30000, -1, "false");
                        break;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
    */
}
