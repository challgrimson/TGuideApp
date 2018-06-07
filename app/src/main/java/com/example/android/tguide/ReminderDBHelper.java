package com.example.android.tguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


// To put in database
/**
 * Created by zacha_000 on 2018-01-10.
 * Holds data for reminders and surveillance time in SQL database that is backed up online.
 * The reminder database is used in the calendar and the surveillance database is used to check
 * the colours in surveillance times.
 */

public class ReminderDBHelper extends SQLiteOpenHelper {

    // Define table parameters
    public static final String DATABASE_NAME = "reminder_tguide.db";
    private static final int DATABASE_VERSION = 1;

    // Reminder Table
    public static final String REMINDER_TABLE_NAME = "reminder";
    public static final String REMINDER_COLUMN_ID = "_id";
    public static final String REMINDER_COLUMN_TITLE = "title";
    public static final String REMINDER_COLUMN_DESCRIPTION = "description";
    public static final String REMINDER_COLUMN_DATE = "date";
    public static final String REMINDER_COLUMN_TIME = "time";
    public static final String REMINDER_COLUMN_REPEAT = "repeat";
    public static final String REMINDER_COLUMN_REPEATNUM = "repeatnum";
    public static final String REMINDER_COLUMN_REPEATTYPE = "repeatTYPE";
    public static final String REMINDER_COLUMN_SOUND = "repeatactive";
    public static final String REMINDER_UNIQUE_ID = "uniqueID";

    // Welcome Notification Table
    public static final String SURV_TABLE_NAME = "surveillance";
    public static final String SURV_COLUMN_ID = "_sid";
    public static final String SURV_ALARM_TIME = "surveillanceTime";
    public static final String SURV_ALARM_ID = "alarmtype";
    public static final String SURV_UNIQE_ID = "survuniqueid";


    public ReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + REMINDER_TABLE_NAME + "(" +
                REMINDER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                REMINDER_COLUMN_TITLE + " TEXT NOT NULL, " +
                REMINDER_COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                REMINDER_COLUMN_DATE + " TEXT NOT NULL, " +
                REMINDER_COLUMN_TIME + " TEXT NOT NULL, " +
                REMINDER_COLUMN_REPEAT + " TEXT NOT NULL, " +
                REMINDER_COLUMN_REPEATNUM + " TEXT NOT NULL, " +
                REMINDER_COLUMN_REPEATTYPE + " TEXT NOT NULL, " +
                REMINDER_COLUMN_SOUND + " TEXT NOT NULL, " +
                REMINDER_UNIQUE_ID + " TEXT NOT NULL)"
        );


        db.execSQL("CREATE TABLE " + SURV_TABLE_NAME + "(" +
                SURV_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                SURV_ALARM_ID + " TEXT NOT NULL, " +
                SURV_UNIQE_ID + " TEXT NOT NULL, " +
                SURV_ALARM_TIME + " TEXT NOT NULL)"
        );

        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("usersRem").child(userr.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            usersRem user = snapshot.getValue(usersRem.class);
                            insertReminderFromFire(user.gettitleText(),user.getdescription(), user.getdateText(), user.gettimeText(), user.getrepeat(), user.getrepeatNum(), user.getrepeatType(), user.getsound(), user.getuniqueID());
                            Log.i("Surveillance","good");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.i("Surveillance","doublegood");

        FirebaseDatabase.getInstance().getReference().child("usersSurv")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            usersSurv user = snapshot.getValue(usersSurv.class);
                            insertSurveillanceFirebase(user.getSURV_ALARM_TIME() ,user.getSURV_UNIQE_ID() , user.getid());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SURV_TABLE_NAME);
        onCreate(db);
    }

    // Insert Reminder into database
    public long insertReminder(String titleText, String description, String dateText, String timeText, String repeat,
                                  String repeatNum, String repeatType, String sound, String uniqueID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_COLUMN_TITLE, titleText);
        contentValues.put(REMINDER_COLUMN_DESCRIPTION, description);
        contentValues.put(REMINDER_COLUMN_DATE, dateText);
        contentValues.put(REMINDER_COLUMN_TIME, timeText);
        contentValues.put(REMINDER_COLUMN_REPEAT, repeat);
        contentValues.put(REMINDER_COLUMN_REPEATNUM, repeatNum);
        contentValues.put(REMINDER_COLUMN_REPEATTYPE, repeatType);
        contentValues.put(REMINDER_COLUMN_SOUND, sound);
        contentValues.put(REMINDER_UNIQUE_ID, uniqueID);

        // Insert into database
        long reminderID = db.insert(REMINDER_TABLE_NAME, null, contentValues);

        //add to firebase
        //titletext is the header of which rest of info is under
        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("titleText").setValue(titleText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("description").setValue(description);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("dateText").setValue(dateText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("timeText").setValue(timeText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeat").setValue(repeat);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeatNum").setValue(repeatNum);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeatType").setValue(repeatType);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("sound").setValue(sound);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("uniqueID").setValue(uniqueID);

        return reminderID;
    }

    // Insert Reminder into database from firebase
    //just no adding to firebase afterwards
    public long insertReminderFromFire(String titleText, String description, String dateText, String timeText, String repeat,
                                       String repeatNum, String repeatType, String sound, String uniqueID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_COLUMN_TITLE, titleText);
        contentValues.put(REMINDER_COLUMN_DESCRIPTION, description);
        contentValues.put(REMINDER_COLUMN_DATE, dateText);
        contentValues.put(REMINDER_COLUMN_TIME, timeText);
        contentValues.put(REMINDER_COLUMN_REPEAT, repeat);
        contentValues.put(REMINDER_COLUMN_REPEATNUM, repeatNum);
        contentValues.put(REMINDER_COLUMN_REPEATTYPE, repeatType);
        contentValues.put(REMINDER_COLUMN_SOUND, sound);
        contentValues.put(REMINDER_UNIQUE_ID, uniqueID);

        // Insert into database
        long reminderID = db.insert(REMINDER_TABLE_NAME, null, contentValues);

        return reminderID;
    }

    // Grab all reminders from database
    public Cursor getAllReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + REMINDER_TABLE_NAME, null );
    }

    public Cursor getReminder(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + REMINDER_TABLE_NAME + " WHERE " +
                REMINDER_COLUMN_ID + " = ?", new String[] { Integer.toString(id) } );
        return res;
    }

    // Return res to
    public int deletePerson(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(REMINDER_TABLE_NAME, REMINDER_COLUMN_ID + " = ?", new String[]{Integer.toString(id)});
    }

    // Update Reminder
    // Insert Reminder into database
    public boolean updateReminder(int reminderID, String titleText, String description, String dateText, String timeText, String repeat,
                                  String repeatNum, String repeatType, String sound) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_COLUMN_TITLE, titleText);
        contentValues.put(REMINDER_COLUMN_DESCRIPTION, description);
        contentValues.put(REMINDER_COLUMN_DATE, dateText);
        contentValues.put(REMINDER_COLUMN_TIME, timeText);
        contentValues.put(REMINDER_COLUMN_REPEAT, repeat);
        contentValues.put(REMINDER_COLUMN_REPEATNUM, repeatNum);
        contentValues.put(REMINDER_COLUMN_REPEATTYPE, repeatType);
        contentValues.put(REMINDER_COLUMN_SOUND, sound);

        // Insert into database
        db.update(REMINDER_TABLE_NAME, contentValues, REMINDER_COLUMN_ID + " = ?", new String[] { Integer.toString(reminderID) } );

        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("titleText").setValue(titleText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("description").setValue(description);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("dateText").setValue(dateText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("timeText").setValue(timeText);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeat").setValue(repeat);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeatNum").setValue(repeatNum);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("repeatType").setValue(repeatType);
        reff.child("usersRem").child(userr.getUid()).child(titleText).child("sound").setValue(sound);
        return true;
    }

    // Grab all reminders for reseting them in reboot
    public Cursor fetchAllReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(REMINDER_TABLE_NAME, new String[] {REMINDER_COLUMN_TITLE, REMINDER_COLUMN_DESCRIPTION, REMINDER_COLUMN_DATE,
            REMINDER_COLUMN_TIME, REMINDER_COLUMN_REPEAT, REMINDER_COLUMN_REPEATNUM, REMINDER_COLUMN_REPEATTYPE, REMINDER_COLUMN_SOUND, REMINDER_UNIQUE_ID},
                null, null, null, null, null);
    }

    // Insert Reminder time
    // Insert Surveillance times into database
    public void insertSurveillance(String id, String time, String uniqueid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();

        if (exists(id)) {
            contentValues.put(SURV_ALARM_TIME, time);
            contentValues.put(SURV_UNIQE_ID, uniqueid);
            // Update existent time
            db.update(SURV_TABLE_NAME, contentValues, SURV_ALARM_ID + " = ?", new String[] { id } );
        } else {
            contentValues.put(SURV_ALARM_ID, id);
            contentValues.put(SURV_ALARM_TIME, time);
            contentValues.put(SURV_UNIQE_ID, uniqueid);
            // Insert into database
            db.insert(SURV_TABLE_NAME, null, contentValues);}

        reff.child("usersSurv").child(userr.getUid()).child(id).child("SURV_ALARM_TIME").setValue(time);
        reff.child("usersSurv").child(userr.getUid()).child(id).child("SURV_UNIQE_ID").setValue(uniqueid);
        reff.child("usersSurv").child(userr.getUid()).child(id).child("id").setValue(id);
    }

    // Same as above but for firebase (used when updating)
    public void insertSurveillanceFirebase(String id, String time, String uniqueid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (exists(id)) {
            contentValues.put(SURV_ALARM_TIME, time);
            contentValues.put(SURV_UNIQE_ID, uniqueid);
            // Update existent time
            db.update(SURV_TABLE_NAME, contentValues, SURV_ALARM_ID + " = ?", new String[] { id } );
        } else {
            contentValues.put(SURV_ALARM_ID, id);
            contentValues.put(SURV_ALARM_TIME, time);
            contentValues.put(SURV_UNIQE_ID, uniqueid);
            // Insert into database
            db.insert(SURV_TABLE_NAME, null, contentValues);}
    }

    // Check to see if alread inserted
    public boolean exists(String id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + SURV_ALARM_ID + " FROM " + SURV_TABLE_NAME + " WHERE " +
                        SURV_ALARM_ID + " = ?",
                new String[] { id });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Grab all surveillance times
    public Cursor fetchsurveillance() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(SURV_TABLE_NAME, new String[] {SURV_ALARM_ID, SURV_ALARM_TIME, SURV_UNIQE_ID},
                null, null, null, null, null);
    }

    // Fetch dates for cursor to set calendar events
    public Cursor fetchEvents() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(REMINDER_TABLE_NAME, new String[] {REMINDER_COLUMN_DATE},
                null, null, null, null, null);
    }

    // Fetch entries with corresponding date
    public Cursor getDateEvents(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM " + REMINDER_TABLE_NAME +" WHERE " + REMINDER_COLUMN_DATE + " = ?", new String[] { date });
    }


    /*
    // Insert welcome alarm time into database - alarm time in ms
    public void insertAlarmTime(String alarmTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WALARM_ALARN_DATE, alarmTime);

        // Insert into database
        db.insert(WALARM_TABLE_NAME, null, contentValues);
    }

    // Grab welcome alarm time
    public Cursor getAlarmTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + WALARM_TABLE_NAME, null);
        return res;
    }
    */
}
