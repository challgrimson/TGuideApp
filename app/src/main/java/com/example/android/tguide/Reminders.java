package com.example.android.tguide;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.android.tguide.ReminderDBHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Reminders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Reminders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reminders extends Fragment {
//public class Reminders extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Reminders() {
        // Required empty public constructor
    }

    // Create variables for creating reminders
    private FloatingActionButton mAddReminderButton;
    ReminderAdapter mCursorAdapter;
    ReminderDBHelper alarmReminderDbHelper = new ReminderDBHelper(getActivity());
    ListView reminderListView;
    ProgressDialog prgDialog;

    private static final int VEHICLE_LOADER = 0;

    public static Reminders newInstance(String param1, String param2) {
        Reminders fragment = new Reminders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction("Reminders");
        }

        // Connect to SQL database
        ReminderDBHelper handler = new ReminderDBHelper(getContext());
        //SQLiteDatabase db = handler.getWritableDatabase();
        Cursor ReminderCursor = handler.getAllReminders();
        //Cursor ReminderCursor = db.rawQuery("SELECT * FROM reminder", null);

        // Initilize List View with EmptyView (what to show when empty)
        reminderListView = (ListView) view.findViewById(R.id.reminder_ListView);
        View emptyView = view.findViewById(R.id.reminder_EmptyView);
        reminderListView.setEmptyView(emptyView);

        // Inililze the AlarmReview
        mCursorAdapter = new ReminderAdapter(getContext(), null);
        reminderListView.setAdapter(mCursorAdapter);

        // Populate ListView
        mCursorAdapter.changeCursor(ReminderCursor);


        // Swap curcor - load data
        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView reminderListView, View view, int position, long id) {
                // Populate enew fragment using HomeActivity method
                mListener.restoreReminder(reminderListView, position);

            }
        });

        // Set Floating Action Button to switch LOADERfragment
        mAddReminderButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Switch to Add_Reminder Fragment
                mListener.change_AddReminder();
            }
        });

        // Add loader to LoaderManager
        //
        //getLoaderManager().initLoader(VEHICLE_LOADER, null, this);

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


    public interface OnFragmentInteractionListener {
        //Passed needed title for fragment to MainActivity
        void onFragmentInteraction(String title);
        void change_AddReminder();
        void restoreReminder (AdapterView reminderListView, int position);
    }
}
