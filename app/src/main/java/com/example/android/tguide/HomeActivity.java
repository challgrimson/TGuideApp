package com.example.android.tguide;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.tguide.ReminderDBHelper;

import java.util.Date;

public class HomeActivity extends AppCompatActivity

        implements
        MainPage.OnFragmentInteractionListener,
        FAQ.OnFragmentInteractionListener,
        AboutTurner.OnFragmentInteractionListener,
        Reminders.OnFragmentInteractionListener,
        HomePage.OnFragmentInteractionListener,
        Add_Reminder.OnFragmentInteractionListener,
        WeeklyCheckList.OnFragmentInteractionListener,
        Reflection.OnFragmentInteractionListener,

        NavigationView.OnNavigationItemSelectedListener {

    // Unique Id for notifications
    private static int NOTIFICATION_UNIQUE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.frag_homepage);

        // Open faq fragment initially
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomePage());
        ft.commit();

        // Grab unique ID value and store in NOTIFICATION_UNIQUE_ID
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        NOTIFICATION_UNIQUE_ID = sharedPref.getInt("uniqueIDValue",0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String title) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
    }

    // Save notification unique id value on pause
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // Save NOTIFICATION_UNIQUE_ID
        editor.putInt("uniqueIDValue",NOTIFICATION_UNIQUE_ID);

        // Commit Change
        editor.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.frag_homepage) {
            fragment = new HomePage();
        } else if (id == R.id.frag_mainPage) {
            fragment = new MainPage();
        } else if (id == R.id.frag_faq) {
            fragment = new FAQ();
        } else if (id == R.id.frag_aboutTurner) {
            fragment = new AboutTurner();
        } else if (id == R.id.frag_reminders) {
            fragment = new Reminders();
        } else if (id == R.id.frag_checklist) {
            fragment = new WeeklyCheckList();
        } else if (id == R.id.fraq_reflect) {
            fragment = new Reflection();
        }

        // Change Fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Build notification
    public Notification sendNotification(String notificationTitle, String notificationDesp){
        // Builder Object for notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle(notificationTitle)
                                                    .setContentText(notificationDesp);

        // Create intent for this notification
        //Intent resultIntent = new Intent(this, Reminders.class);
        //PendingIntent resultPendingIntnt = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add intent to notification builder object
        //mBuilder.setContentIntent(resultPendingIntnt);

        return mBuilder.build();
    }

    // Create notification
    public boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime){
        // Create Notificiton
        Notification notification = sendNotification(notificationTitle, notificationDesp);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID,(int) ID);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this,ID, myIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // See if repeat and so, set
        if (repeatTime == -1) {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, resultPendingIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, repeatTime, resultPendingIntent);
        }

        return true;
    }

    // Delete the notification
    public boolean deleteNotification(int ID, String notificationTitle, String notificationDesp) {
        Notification notification = sendNotification(notificationTitle, notificationDesp);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, ID);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this,ID, myIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(resultPendingIntent);
        return true;
    }

    // Change to add reminder fragment when clicked
    public void change_AddReminder () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Add_Reminder());
        ft.commit();
    }

    // Change to reminder activity when clicked
    public void change_Reminder () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Reminders());
        ft.commit();
    }

    // Restore reminder for editing
    public void restoreReminder (AdapterView reminderListView, int position) {

        // Get person ID and populate view
        Cursor cursor = (Cursor) reminderListView.getItemAtPosition(position);
        int reminderID = cursor.getInt(cursor.getColumnIndex(ReminderDBHelper.REMINDER_COLUMN_ID));
        // Get handler for data base

        ReminderDBHelper handler = new ReminderDBHelper(this);

        // Give cursor view data
        cursor = handler.getReminder(reminderID);
        // Grab data from cursor
        // Extra properties from cursor
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DESCRIPTION));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TIME));
        String repeat = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEAT));
        String repeatNu = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATNUM));
        String repeatTy = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATTYPE));
        String active = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_SOUND));
        String uniqueID = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_UNIQUE_ID));
        // Pass data to fragment using bundle

        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("description",description);
        bundle.putString("date",date);
        bundle.putString("time",time);
        bundle.putString("repeat",repeat);
        bundle.putString("repeatNu",repeatNu);
        bundle.putString("repeatTy",repeatTy);
        bundle.putString("active",active);
        bundle.putInt("reminderID",reminderID);
        bundle.putString("uniqueID",uniqueID);

        // close database
        //cursor.close();
        // Change Fragmentow();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Add_Reminder fragobj = new Add_Reminder();
        fragobj.setArguments(bundle);
        //fragobj.newInstance("true", title, description, date, time, repeat, repeatNu, repeatTy, active, reminderID);
        fragmentTransaction.replace(R.id.mainFrame,fragobj);
        fragmentTransaction.commit();
    }

    public String generateUniqueID() {
        ++NOTIFICATION_UNIQUE_ID;
        return String.valueOf(NOTIFICATION_UNIQUE_ID);
    }
}
