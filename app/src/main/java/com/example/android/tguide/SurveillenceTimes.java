package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

// For surveillance time information
public class SurveillenceTimes extends Fragment {

    private OnFragmentInteractionListener mListener;

    // Set Variables for layouts
    LinearLayout mPAP;

    // Set text view variables
    TextView mPAPtext;

    // Set save state variables
    String PAPsave;

    // Variables to hold states
    Boolean PAPstate;

    // Hold time set
    long mPAPtime;

    // Service
    //private  static final String TAG = "SurveillenceTimes";
    //private Intent intent;

    public SurveillenceTimes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //intent = new Intent(getActivity(), surveillenceSurvice.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surveillence_times, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction("Surveillance Times");
        }
        // Set LinearLayout variables
        mPAP = (LinearLayout) view.findViewById(R.id.PAP);

        // Set Text variables
        mPAPtext = (TextView) view.findViewById(R.id.PAPtext);

        // Load saved states
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mPAPtext.setText(sharedPref.getString("PAP", getString(R.string.overdue)));
        mPAPtime = sharedPref.getLong("PAPtime", -1);

        // Check appropiate colours
        checkTime();

        // Make text appropiate colours
        managecolours();

        mPAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                PAPstate = true;

                // Set time stamp
                mPAPtime = Calendar.getInstance().getTimeInMillis();

                // Call date setter
                setDate(v);



               //getActivity().startService(intent);
               //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        // To change menu
        setHasOptionsMenu(true);


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
    }

    // Saving Date when Paused
    // Save data on pause
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        PAPsave = mPAPtext.getText().toString();
        editor.putString("PAP",PAPsave);
        editor.putLong("PAPtime",mPAPtime);
        editor.apply();
    }

    // Inflate the menu options from the res/menu/menu_editor.xml file.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // This adds menu items to the app bar.
        menu.clear();
        inflater.inflate(R.menu.surveillence_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // For creating info box
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showInformationDialog();
        return super.onOptionsItemSelected(item);
    }

    // Set date that is specified
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog v, int year, int monthOfYear, int dayOfMonth) {
                        String mDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        mPAPtext.setText(mDate);
                        TextViewCompat.setTextAppearance(mPAPtext, R.style.DateInsert);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    // Set the appropiate colours depending on value
    public void managecolours() {
        // Compare each once to overdue to check
        if (PAPstate) {
            TextViewCompat.setTextAppearance(mPAPtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mPAPtext, R.style.overdueText);
        }
    }

    // Check time and see if need to change colours
    public void checkTime() {
        if (Calendar.getInstance().getTimeInMillis() - mPAPtime > 5000 || mPAPtime != -1) {
            PAPstate = false;
        } else {
            PAPstate = true;
        }

        // Change colour if needed
        managecolours();
    }

    // Dialog to show information and if want to download chart
    private void showInformationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.surveillance_dialogDescrip);
        builder.setNeutralButton(R.string.surveillance_tableDownload, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String URL = "http://www.turnersyndrome.ca/wp-content/uploads/TS_Surveillance_Chart_Colour.pdf";
                // Download passed URL
                new DownLoadTask(getContext(), URL);

            }
        });
        builder.setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Close" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /*
    // Broadcast Reciever to recieve from service and will just check times
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkTime();
        }
    };

    private void updateUI(Intent intent) {
        Toast.makeText(getContext(), "HI", Toast.LENGTH_SHORT).show();
        // Change to red
        TextViewCompat.setTextAppearance(mPAPtext, R.style.overdueText);

        Log.d(TAG, "update sharedpreferences; ");

        PAPstate = false;
    }
    */


}