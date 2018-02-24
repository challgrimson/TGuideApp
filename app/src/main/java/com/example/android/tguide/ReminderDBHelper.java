package com.example.android.tguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// To put in database
/**
 * Created by zacha_000 on 2018-01-10.
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
    public static final String WALARM_TABLE_NAME = "welcomeAlarm";
    public static final String WALARM_ALARN_DATE = "Alarmtime";


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
                REMINDER_UNIQUE_ID+ " TEXT NOT NULL)"
        );

        /*
        db.execSQL("CREATE TABLE " + WALARM_TABLE_NAME + "(" +
                WALARM_ALARN_DATE + " TEXT NOT NULL)"
        );
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REMINDER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALARM_TABLE_NAME);
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
        db.update(REMINDER_TABLE_NAME, contentValues, REMINDER_COLUMN_ID + " = ? ", new String[] { Integer.toString(reminderID) } );
        return true;
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
