package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import com.example.android.tguide.ReminderDBHelper;

import java.util.Calendar;


public class Add_Reminder extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateChangedListener {
        //LoaderManager.LoaderCallbacks<Cursor> {
    // If need to restore get reminder ID and initilize not to restore
    private static int reminderID;
    private Boolean mRestore = false;

    // Listener to interact with HomeActivity
    private OnFragmentInteractionListener mListener;

    // Create Values
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText, mDescription;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mDescrip;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private String uniqueID;

    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    // For alarm ID
    long alarmID;

    // Do something when touched
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    public Add_Reminder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestore = true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);

        // Send Add Reminder as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction("Add Reminder");
        }

        // Initialize Views
        mTitleText = (EditText) view.findViewById(R.id.reminder_add_titleText);
        mDescription = (EditText) view.findViewById(R.id.reminder_add_descrip);
        mDateText = (TextView) view.findViewById(R.id.reminder_set_date);
        mTimeText = (TextView) view.findViewById(R.id.reminder_time_date);
        mRepeatText = (TextView) view.findViewById(R.id.reminder_repeat_time);
        mRepeatNoText = (TextView) view.findViewById(R.id.reminder_interval_time);
        mRepeatTypeText = (TextView) view.findViewById(R.id.reminder_typeRepition_time);
        mRepeatSwitch = (Switch) view.findViewById(R.id.repeat_switch);
        // Floating action button for when sound is off
        mFAB1 = (FloatingActionButton) view.findViewById(R.id.fab_soundOff);
        // Floating action button for when sound is on
        mFAB2 = (FloatingActionButton) view.findViewById(R.id.fab_soundOn);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";
        // Initialize date value so that they first appear as current data
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        // Create the date layout to be displayed
        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;


        // Setup Reminder Title To Inputted Title Text
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setip Reminder Description to inputed text
        // Setup Reminder Title To Inputted Title Text
        mDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDescrip = s.toString().trim();
                mDescription.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup TextViews using default reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        // Initilize floating action buttons to sound on
        mFAB1.setVisibility(View.GONE);
        mFAB2.setVisibility(View.VISIBLE);

        // To change menu
        setHasOptionsMenu(true);

        // Make date relative layout clickable
        RelativeLayout dateRelative = (RelativeLayout) view.findViewById(R.id.reminder_date);
        dateRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                setDate(v);
            }
        });

        // Make Time relative layout clickable
        RelativeLayout timeRelative = (RelativeLayout) view.findViewById(R.id.reminder_time);
        timeRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                setTime(v);
            }
        });

        // Give switch functionality
        mRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                // On clicking the repeat switch
                if (isChecked) {
                        mRepeat = "true";
                        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                } else {
                        mRepeat = "false";
                        mRepeatText.setText(R.string.reminder_switch_off);
                    }
                }
        });

        // Give repetition interval layout functionality
        RelativeLayout intervalRelative = (RelativeLayout) view.findViewById(R.id.reminder_repeat_interval);
        intervalRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                setRepeatNo(v);
            }
        });

        // Give repetition type layout functionality
        RelativeLayout typeRelative = (RelativeLayout) view.findViewById(R.id.reminder_type_repitiion);
        typeRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectRepeatType(v);
            }
        });

        // onClick for floating action button sound on
        mFAB2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectFab2(v);
            }
        });

        // onClick for floating action button sound off
        mFAB1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectFab1(v);
            }
        });

        // Restore values
        if (mRestore) {
                // Grab bundle if passed
                reminderID = getArguments().getInt("reminderID");
                mTitleText.setText(getArguments().getString("title"));
                mDescription.setText(getArguments().getString("description"));
                mDateText.setText(getArguments().getString("date"));
                mTimeText.setText(getArguments().getString("time"));
                uniqueID = getArguments().getString("uniqueID");
                String isRepeat = getArguments().getString("repeat");
                if (isRepeat.equals("true")) {
                    mRepeatText.setText("Every " + getArguments().getString("repeatNu") + " " + getArguments().getString("repeatTy") + "(s)");
                    mRepeatNoText.setText(getArguments().getString("repeatNu"));
                    mRepeatTypeText.setText(getArguments().getString("repeatTy"));
                }else {
                    mRepeatText.setText("Repeat Off");
                    mRepeatSwitch.setChecked(false);
                }
                mActive = getArguments().getString("active");

                if (mActive.equals("false")) {
                    mFAB1.setVisibility(View.VISIBLE);
                    mFAB2.setVisibility(View.GONE);
                }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        //Passed needed title for fragment to MainActivity
        void onFragmentInteraction(String title);
        void change_Reminder();
        boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime);
        boolean deleteNotification(int ID, String notifictionTitle, String notificationDesp);
        String generateUniqueID();
    }

    // Save data on pause
    // UPDATE FROM GITHUB SCRIPT
    @Override
    public void onPause(){
        super.onPause();
        /*
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor outState = sharedPref.edit();
        outState.putString(KEY_TITLE, mTitleText.getText().toString());
        outState.putString(KEY_TIME, mTimeText.getText().toString());
        outState.putString(KEY_DATE, mDateText.getText().toString());
        outState.putString(KEY_REPEAT, mRepeatText.getText().toString());
        outState.putString(KEY_REPEAT_NO, mRepeatNoText.getText().toString());
        outState.putString(KEY_REPEAT_TYPE, mRepeatTypeText.getText().toString());
        outState.putString(KEY_ACTIVE, mActive);
        outState.apply();
        */
    }

    // On clicking Time picker
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                // UNSURE IF THIS WILL WORK
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog v, int year, int monthOfYear, int dayOfMonth) {
                        mDate = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
                        mDateText.setText(mDate);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // On click make sound on visible
    public void selectFab1(View v) {
        mFAB1.setVisibility(View.GONE);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    // On click make sound off visible
    public void selectFab2(View v) {
        mFAB2.setVisibility(View.GONE);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    // On clicking repeat type button
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Enter Repition Interval Number");

        // Create EditText box to input repeat number
        // MAY CREATE ISSUE
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                        else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menu.clear();
        inflater.inflate(R.menu.reminder_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    /*
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new reminder, hide the "Delete" menu item.
        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save_reminder:
                if (mTitleText.getText().toString().length() == 0){
                    Toast.makeText(getContext(), R.string.title_blank,
                            Toast.LENGTH_LONG).show();
                }

                else {
                    saveReminder();

                    // Move to reminder fragment
                    mListener.change_Reminder();

                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.discard_reminder:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.reminder_MenuTrash, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void deleteReminder() {
        // Only perform the delete if this is an existing reminder.
            // Call the ContentResolver to delete the reminder at the given content URI.
            if (mRestore) {
                ReminderDBHelper handler = new ReminderDBHelper(getContext());
                // Delete the alarm
                boolean alarmCheck = mListener.deleteNotification(Integer.parseInt(uniqueID), mTitle, mDescrip);

                // Delete reminder view
                int check = handler.deletePerson(reminderID);

                if (check == 0 & alarmCheck) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(getActivity(), getString(R.string.editor_delete_reminder_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(getActivity(), getString(R.string.editor_delete_reminder_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        // Move to remidner fragment
        mListener.change_Reminder();
    }

    // On clicking the save button
    public void saveReminder() {

     /*if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }*/

     // Check if description blank and if so set to blank
        if (mDescrip == null) {
            mDescrip = "";
        }

        // Get Handler for database
        ReminderDBHelper handler = new ReminderDBHelper(getContext());

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp = mCalendar.getTimeInMillis();

        // Check repeat type
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
        } else {
            mRepeatTime = -1;
        }

        // If resotring then just updating, if not then create new
        if (mRestore) {
            // Insert into dataebase
            boolean saved = handler.updateReminder(reminderID,mTitle, mDescrip, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive);
            // Delete the alarm
            // NEED TO UPDATE NOTIFICATION
            boolean alarmCheck = mListener.deleteNotification(Integer.parseInt(uniqueID), mTitle, mDescrip);
            // Re-create Notification
            boolean checkAlarm = mListener.begin_notifications(Integer.parseInt(uniqueID), mTitle, mDescrip, selectedTimestamp, mRepeatTime);

            if (saved & alarmCheck & checkAlarm) {
                Toast.makeText(getContext(), R.string.updateComplete, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), R.string.updateFailed, Toast.LENGTH_LONG).show();
            }

        } else {

            String uniqueID = mListener.generateUniqueID();
            // Insert into dataebase
            handler.insertReminder(mTitle, mDescrip, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive, uniqueID);
            // Create notification
            boolean checkAlarm = mListener.begin_notifications(Integer.parseInt(uniqueID), mTitle, mDescrip, selectedTimestamp, mRepeatTime);

            if (checkAlarm) {
                Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), R.string.savedFailed, Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onDateChanged(){
    }
}
