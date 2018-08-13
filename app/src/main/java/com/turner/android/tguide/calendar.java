package com.turner.android.tguide;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class calendar extends Fragment {

    String TAG = "calendarView";

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat dbDateFormate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private OnFragmentInteractionListener mListener;

    // Calendar Variables
    FloatingActionButton addevent;
    CompactCalendarView compactCalendarView;
    Calendar calendar;
    Toolbar toolbar;

    // Create variables for creating reminders
    ReminderAdapter mCursorAdapter;
    ReminderDBHelper alarmReminderDbHelper = new ReminderDBHelper(getActivity());
    ListView reminderListView;
    ProgressDialog prgDialog;

    // Handler and cursor for database
    ReminderDBHelper handler;
    Cursor ReminderCursor;

    // Store which date is selected
    String currentdate;


    public static boolean loadedFirebase = false;

    public calendar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // CALENDAR STUFF

        addevent = view.findViewById(R.id.addevent);
        compactCalendarView = view.findViewById(R.id.compactCalendarView);
        calendar = Calendar.getInstance();
        toolbar = view.findViewById(R.id.toolbar2);

        // Set first day of week to Saturday
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        // Set defaultetoolbar to month and year
        toolbar.setTitle(dateFormatMonth.format(System.currentTimeMillis()));
        currentdate = dbDateFormate.format(System.currentTimeMillis());

        // Set date descroption
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        // set handler for data base
        handler = new ReminderDBHelper(getContext());
        //if (loadedFirebase == false){
       // }

        // Set date

        /*
        calendar.set(2018,3,20);
        Event eve1 = new Event(Color.BLACK, calendar.getTimeInMillis());
        Event eve2 = new Event(Color.BLACK, calendar.getTimeInMillis());
        Event eve3 = new Event(Color.BLACK, calendar.getTimeInMillis());
        compactCalendarView.addEvent(eve1);
        compactCalendarView.addEvent(eve2);
        compactCalendarView.addEvent(eve3);
        */

        // Create events based on database
        createEvents();

        // Initilize List View with EmptyView (what to show when empty)
        reminderListView = view.findViewById(R.id.calendar_ListView);
        View emptyView = view.findViewById(R.id.reminder_Hint);
        reminderListView.setEmptyView(emptyView);

        // Inililze the AlarmReview
        mCursorAdapter = new ReminderAdapter(getContext(), null);
        reminderListView.setAdapter(mCursorAdapter);

        // Populate listView with current date items
        ReminderCursor = handler.getDateEvents(dbDateFormate.format(System.currentTimeMillis()));
        mCursorAdapter.changeCursor(ReminderCursor);



        // Swap curcor - load data
        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView reminderListView, View view, int position, long id) {
                // Populate enew fragment using HomeActivity method
                mListener.restoreReminder(reminderListView, position);

            }
        });

        // Calendar click on date then load stuff pertaining to that time
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //SQLiteDatabase db = handler.getWritableDatabase();
                ReminderCursor = handler.getDateEvents(dbDateFormate.format(dateClicked));

                // Populate ListView
                mCursorAdapter.changeCursor(ReminderCursor);

                // Store which date is selected
                currentdate = dbDateFormate.format(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Switch to Add Reminder Activity
                mListener.change_AddReminder(currentdate);

            }
        });

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
        void onFragmentInteraction(String title);
        void restoreReminder (AdapterView reminderListView, int position);
        void change_AddReminder(String date);
    }

    public void createEvents() {
        // Grab dates and store in crusor
        Cursor cursor = handler.fetchEvents();

        // Iterate through dates and set events
        if (cursor != null) {
            cursor.moveToFirst();

            int dateeint = cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DATE);

            while (!cursor.isAfterLast()) {
                String date = cursor.getString(dateeint);

                //Grab dash values for index
                int dash1 = date.indexOf("/");
                int dash2 = date.indexOf("/", dash1 + 1);


                calendar.set(Calendar.YEAR, Integer.valueOf(date.substring(dash2 + 1)));
                calendar.set(Calendar.MONTH, Integer.valueOf(date.substring(dash1 + 1, dash2)) - 1); //-1 b/c one month behind
                calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.substring(0, dash1)));

                Event eve = new Event(Color.BLACK, calendar.getTimeInMillis());

                compactCalendarView.addEvent(eve);

                /*
        calendar.set(2018,3,20);
        Event eve1 = new Event(Color.BLACK, calendar.getTimeInMillis());
        Event eve2 = new Event(Color.BLACK, calendar.getTimeInMillis());
        Event eve3 = new Event(Color.BLACK, calendar.getTimeInMillis());
        compactCalendarView.addEvent(eve1);
        compactCalendarView.addEvent(eve2);
        compactCalendarView.addEvent(eve3);
        */

                cursor.moveToNext();

                }
            }
    }
}
