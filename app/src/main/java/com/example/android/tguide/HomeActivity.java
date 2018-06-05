package com.example.android.tguide;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        SurveillenceTimes.OnFragmentInteractionListener,
        accountinfo.OnFragmentInteractionListener,
        calendar.OnFragmentInteractionListener,


        NavigationView.OnNavigationItemSelectedListener {

    // Unique Id for notifications
    private static int NOTIFICATION_UNIQUE_ID;

    // Set shared preference
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme_NoActionBar);
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

        // Set shared preference values
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Grab unique ID value and store in NOTIFICATION_UNIQUE_ID
        NOTIFICATION_UNIQUE_ID = sharedPref.getInt("uniqueIDValue",1);

        // Check if first time opening app: if so then run introduction dialog
        if (sharedPref.getBoolean("firstvisit", true)) {
            Log.i("HomeActivity","Start Welcome");
            // Set introduction dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.welcomeDialog);
            builder.setPositiveButton(R.string.welcomeDialogBegin, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dismess on button click
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            // Create welcome alarm
            welcomeAlarm();

            // Put date into database
            // Get Handler for database
            //Log.i("bootReceiver", "Putting time into database");
            //Log.i("bootReceiver", String.valueOf(System.currentTimeMillis()) + 10000);
            //ReminderDBHelper handler = new ReminderDBHelper(this);
            //handler.insertAlarmTime(String.valueOf(System.currentTimeMillis()) + 10000);

            // Set to not first time
            editor.putBoolean("firstvisit",false);
            editor.apply();
        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = new HomePage();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
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
        // Save NOTIFICATION_UNIQUE_ID
        editor.putInt("uniqueIDValue",NOTIFICATION_UNIQUE_ID);
        // Apply Change
        editor.apply();
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
        }
        // REMOVED FOR TESTING REMINDER
        /*else if (id == R.id.frag_reminders) {
            fragment = new Reminders();
        }*/ else if (id == R.id.frag_checklist) {
            fragment = new WeeklyCheckList();
        } else if (id == R.id.fraq_reflect) {
            fragment = new Reflection();
        } else if (id == R.id.fraq_surv) {
            fragment = new SurveillenceTimes();
        } else if (id == R.id.frag_accountinfo) {
            fragment = new accountinfo();
        } else if (id == R.id.fraq_calendar) {
            fragment = new calendar();
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
    public Notification sendNotification(String notificationTitle, String notificationDesp, String mActive){
        // Set sound for notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (mActive.equals("true")) {
            // Builder Object for notification sound on
            return new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .setSound(soundUri)
                    .setContentIntent(resultPendingIntent)
                    .build();
        } else {
            // Builder Object for notification sound off
            return new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesp)
                    .setContentIntent(resultPendingIntent)
                    .build();
        }
    }

    // Create notification
    public boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime, String mActive){
        // Create Notificiton
        Notification notification = sendNotification(notificationTitle, notificationDesp, mActive);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, ID);
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
    public boolean deleteNotification(int ID, String notificationTitle, String notificationDesp, String mActive) {
        Notification notification = sendNotification(notificationTitle, notificationDesp, mActive);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, ID);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this,ID, myIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(resultPendingIntent);
        return true;
    }

    // Change to add reminder fragment when clicked
    public void change_AddReminder (String date) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // CHANGED FOR CALENDAR
        //ft.replace(R.id.mainFrame, new Reminders());

        // Send date so that starts with correct date
        Bundle bundle = new Bundle();
        bundle.putString("date",date);

        // Change Fragmentow();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Add_Reminder fragobj = new Add_Reminder();
        fragobj.setArguments(bundle);
        //fragobj.newInstance("true", title, description, date, time, repeat, repeatNu, repeatTy, active, reminderID);
        fragmentTransaction.replace(R.id.mainFrame,fragobj);
        fragmentTransaction.commit();
    }

    // Change to reminder activity when clicked
    public void change_Reminder () {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // CHANGED FOR CALENDAR
        //ft.replace(R.id.mainFrame, new Reminders());
        ft.replace(R.id.mainFrame, new calendar());
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
        //return String.valueOf(NOTIFICATION_UNIQUE_ID);
        return String.valueOf((int) System.currentTimeMillis());
    }

    public void welcomeAlarm () {
        // Creat alarm: SET FOR 5 MINUTS
        Log.i("Alarm","Creating welcome alarm;");
        begin_notifications(1, getString(R.string.welcomeNotificationTitle), getString(R.string.welcomeNotificationDescrip), System.currentTimeMillis() + 60 * 1000, 5 * 60 * 1000, "true");
    }

}
