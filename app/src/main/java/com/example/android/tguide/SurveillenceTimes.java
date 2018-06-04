package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

// For surveillance time information
public class SurveillenceTimes extends Fragment {

    private OnFragmentInteractionListener mListener;

    // Set Variables for layouts
    LinearLayout mPAP, mTHY, mCEL, mECG, mECH, mCT, mPRO, mVIS, mHEA, mBON ;

    // Set text view variables
    TextView mPAPtext, mTHYtext,mCELtext, mECGtext, mECHtext, mCTtext, mPROtext, mVIStext, mHEAtext, mBONtext;

    // Set save state variables
    String PAPsave, THYsave, CELsave, ECGsave, ECHsave, CTsave, PROsave, VISsave, HEAsave, BONsave;

    // Variables to hold states
    Boolean PAPstate, THYstate, CELstate, ECGstate, ECHstate, CTstate, PROstate, VISstate, HEAstate, BONstate;

    // Hold time set
    long mPAPtime, mTHYtime, mCELtime, mECGtime, mECHtime, mCTtime, mPROtime, mVIStime, mHEAtime, mBONtime;

    // Create Calendar to set dates
    Calendar mCalendar;

    // temp variables for inputting date
    int mYear, mMonth, mDay;

    // Handler to put times in DB database
    ReminderDBHelper handler;

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
            mListener.onFragmentInteraction(getString(R.string.surveillancetimes));
        }
        // Set LinearLayout variables
        mPAP = view.findViewById(R.id.PAP);
        mTHY = view.findViewById(R.id.thyroidDisease);
        mCEL = view.findViewById(R.id.celiacDisease);
        mECG = view.findViewById(R.id.ECG);
        mECH = view.findViewById(R.id.Echo);
        mCT = view.findViewById(R.id.CTMRI);
        mPRO = view.findViewById(R.id.prophylaxisAnti);
        mVIS = view.findViewById(R.id.vision);
        mHEA = view.findViewById(R.id.hearing);
        mBON= view.findViewById(R.id.boneDensity);

        // Set Text variables
        mPAPtext = view.findViewById(R.id.PAPtext);
        mTHYtext = view.findViewById(R.id.thyroidDiseasetext);
        mCELtext = view.findViewById(R.id.celiacDiseasetext);
        mECGtext = view.findViewById(R.id.ECGtext);
        mECHtext = view.findViewById(R.id.Echotext);
        mCTtext = view.findViewById(R.id.CTMRItext);
        mPROtext = view.findViewById(R.id.prophylaxisAntitext);
        mVIStext = view.findViewById(R.id.visiontext);
        mHEAtext = view.findViewById(R.id.hearingtext);
        mBONtext= view.findViewById(R.id.boneDensitytext);


        // Load saved states
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("mCELtime")) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);
                            // Save current state of checked items
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


                            mPAPtext.setText(user.getmPAPtext());
                            mTHYtext.setText(user.getmTHYtext());
                            mCELtext.setText(user.getmCELtext());
                            mECGtext.setText(user.getmECGtext());
                            mECHtext.setText(user.getmECHtext());
                            mCTtext.setText(user.getmCTtext());
                            mPROtext.setText(user.getmPROtext());
                            mVIStext.setText(user.getmVIStext());
                            mHEAtext.setText(user.getmHEAtext());
                            mBONtext.setText(user.getmBONtext());

                            mPAPtime = user.getmPAPtime();
                            mTHYtime = user.getmTHYtime();
                            mCELtime = user.getmCELtime();
                            mECGtime = user.getmECGtime();
                            mECHtime = user.getmECHtime();
                            mCTtime = user.getmCTtime();
                            mPROtime = user.getmPROtime();
                            mVIStime = user.getmVIStime();
                            mHEAtime = user.getmHEAtime();
                            mBONtime = user.getmBONtime();

                        } else  {
                            // Set text
                            mPAPtext.setText(getString(R.string.overdue));
                            mTHYtext.setText(getString(R.string.overdue));
                            mCELtext.setText(getString(R.string.overdue));
                            mECGtext.setText(getString(R.string.overdue));
                            mECHtext.setText(getString(R.string.overdue));
                            mCTtext.setText(getString(R.string.overdue));
                            mPROtext.setText(getString(R.string.overdue));
                            mVIStext.setText(getString(R.string.overdue));
                            mHEAtext.setText(getString(R.string.overdue));
                            mBONtext.setText(getString(R.string.overdue));
                            // Set time
                            mPAPtime = -1;
                            mTHYtime = -1;
                            mCELtime = -1;
                            mECGtime = -1;
                            mECHtime = -1;
                            mCTtime = -1;
                            mPROtime = -1;
                            mVIStime = -1;
                            mHEAtime = -1;
                            mBONtime = -1;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        // Initilize Calendar variables
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DATE);

        // Check appropiate colours
        checkTime();

        // Make text appropiate colours
        managecolours();

        mPAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                PAPstate = true;

                // Call date setter and grab time
                setDate(v, mPAPtext, 1);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mTHY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                THYstate = true;

                // Call date setter
                setDate(v, mTHYtext, 6);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                CELstate = true;

                // Call date setter
                setDate(v, mCELtext, 10);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                ECGstate = true;

                // Call date setter
                setDate(v, mECGtext, 11);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mECH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                ECHstate = true;

                // Call date setter
                setDate(v, mECHtext, 12);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                CTstate = true;

                // Call date setter
                setDate(v, mCTtext, 13);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mPRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                PROstate = true;

                // Call date setter
                setDate(v, mPROtext, 14);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mVIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                VISstate = true;

                // Call date setter
                setDate(v, mVIStext, 15);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mHEA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                HEAstate = true;

                // Call date setter
                setDate(v, mHEAtext, 16);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mBON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                BONstate = true;

                // Call date setter
                setDate(v, mBONtext, 17);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });


        // To change menu
        setHasOptionsMenu(true);

        // Grab DB handler to put in information
        handler = new ReminderDBHelper(getContext());

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
        boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime, String mActive);
        String generateUniqueID();
    }

    // Saving Date when Paused
    // Save data on pause
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Grab text
        PAPsave = mPAPtext.getText().toString();
        THYsave = mTHYtext.getText().toString();
        CELsave = mCELtext.getText().toString();
        ECGsave = mECGtext.getText().toString();
        ECHsave = mECHtext.getText().toString();
        CTsave = mCTtext.getText().toString();
        PROsave = mPROtext.getText().toString();
        VISsave = mVIStext.getText().toString();
        HEAsave = mHEAtext.getText().toString();
        BONsave = mBONtext.getText().toString();

        // Save string
        editor.putString("PAP",PAPsave);
        editor.putString("THY",THYsave);
        editor.putString("CEL",CELsave);
        editor.putString("ECG",ECGsave);
        editor.putString("ECH",ECHsave);
        editor.putString("CT",CTsave);
        editor.putString("PRO",PROsave);
        editor.putString("VIS",VISsave);
        editor.putString("HEA",HEAsave);
        editor.putString("BON",BONsave);


        // Sve bool
        editor.putLong("PAPtime",mPAPtime);
        editor.putLong("THYtime",mTHYtime);
        editor.putLong("CELtime",mCELtime);
        editor.putLong("ECGtime",mECGtime);
        editor.putLong("ECHtime",mECHtime);
        editor.putLong("CTtime",mCTtime);
        editor.putLong("PROtime",mPROtime);
        editor.putLong("VIStime",mVIStime);
        editor.putLong("HEAtime",mHEAtime);
        editor.putLong("BONtime",mBONtime);


        editor.apply();
        fireBaseSave();
    }

    public void fireBaseSave(){
        PAPsave = mPAPtext.getText().toString();
        THYsave = mTHYtext.getText().toString();
        CELsave = mCELtext.getText().toString();
        ECGsave = mECGtext.getText().toString();
        ECHsave = mECHtext.getText().toString();
        CTsave = mCTtext.getText().toString();
        PROsave = mPROtext.getText().toString();
        VISsave = mVIStext.getText().toString();
        HEAsave = mHEAtext.getText().toString();
        BONsave = mBONtext.getText().toString();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(user.getUid()).child("mPAPtext").setValue(PAPsave);
        ref.child("users").child(user.getUid()).child("mTHYtext").setValue(THYsave);
        ref.child("users").child(user.getUid()).child("mCELtext").setValue(CELsave);
        ref.child("users").child(user.getUid()).child("mECGtext").setValue(ECGsave);
        ref.child("users").child(user.getUid()).child("mECHtext").setValue(ECHsave);
        ref.child("users").child(user.getUid()).child("mCTtext").setValue(CTsave);
        ref.child("users").child(user.getUid()).child("mPROtext").setValue(PROsave);
        ref.child("users").child(user.getUid()).child("mVIStext").setValue(VISsave);
        ref.child("users").child(user.getUid()).child("mHEAtext").setValue(HEAsave);
        ref.child("users").child(user.getUid()).child("mBONtext").setValue(BONsave);

        ref.child("users").child(user.getUid()).child("mPAPtime").setValue(mPAPtime);
        ref.child("users").child(user.getUid()).child("mTHYtime").setValue(mTHYtime);
        ref.child("users").child(user.getUid()).child("mCELtime").setValue(mCELtime);
        ref.child("users").child(user.getUid()).child("mECGtime").setValue(mECGtime);
        ref.child("users").child(user.getUid()).child("mECHtime").setValue(mECHtime);
        ref.child("users").child(user.getUid()).child("mCTtime").setValue(mCTtime);
        ref.child("users").child(user.getUid()).child("mPROtime").setValue(mPROtime);
        ref.child("users").child(user.getUid()).child("mVIStime").setValue(mVIStime);
        ref.child("users").child(user.getUid()).child("mHEAtime").setValue(mHEAtime);
        ref.child("users").child(user.getUid()).child("mBONtime").setValue(mBONtime);

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
    public void setDate(View v, final TextView tv, final int timeView){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog v, int year, int monthOfYear, int dayOfMonth) {
                        String mDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tv.setText(mDate);
                        TextViewCompat.setTextAppearance(tv, R.style.DateInsert);

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        mCalendar.set(Calendar.YEAR, mYear);
                        mCalendar.set(Calendar.MONTH, mMonth);
                        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);

                        Log.i("Year",String.valueOf(mYear));
                        Log.i("Month",String.valueOf(mMonth));
                        Log.i("Day",String.valueOf(mDay));

                        // REMINDER TO ALSO SWTICH IN bootReceiver
                        Integer uniqueid = Integer.parseInt(mListener.generateUniqueID());
                        switch (timeView) {
                            case 1:
                                mPAPtime = mCalendar.getTimeInMillis();
                                // Set notification
                                mListener.begin_notifications(uniqueid,getString(R.string.physicalexamTitle),
                                        getString(R.string.physicalexamdesp),mPAPtime + 11*60*60*1000,-1,"false");
                                Log.i("bootReceiver", String.valueOf(mPAPtime));
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mPAPtime), String.valueOf(uniqueid));
                                break;
                            case 6:
                                mTHYtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.screenbloodTitle),
                                        getString(R.string.physicalexamdesp),mTHYtime + 11*60*60*1000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mTHYtime), String.valueOf(uniqueid));
                                break;
                            case 10:
                                mCELtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.celiacTitle),
                                        getString(R.string.physicalexamdesp),mCELtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mCELtime), String.valueOf(uniqueid));
                                break;
                            case 11:
                                mECGtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.ECGTitle),
                                        getString(R.string.physicalexamdesp),mECGtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mECGtime), String.valueOf(uniqueid));
                                break;
                            case 12:
                                mECHtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.ECHOTitle),
                                        getString(R.string.physicalexamdesp),mECHtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mECHtime), String.valueOf(uniqueid));
                                break;
                            case 13:
                                mCTtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.CTMRITitle),
                                        getString(R.string.physicalexamdesp),mCTtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mCTtime), String.valueOf(uniqueid));
                                break;
                            case 14:
                                mPROtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.antiproTitle),
                                        getString(R.string.physicalexamdesp),mPROtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mPROtime), String.valueOf(uniqueid));
                                break;
                            case 15:
                                mVIStime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.visionTitle),
                                        getString(R.string.physicalexamdesp),mVIStime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mVIStime), String.valueOf(uniqueid));
                                break;
                            case 16:
                                mHEAtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.hearingTitle),
                                        getString(R.string.physicalexamdesp),mHEAtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mHEAtime), String.valueOf(uniqueid));
                                break;
                            case 17:
                                mBONtime = mCalendar.getTimeInMillis();
                                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()),getString(R.string.bonedensityTitle),
                                        getString(R.string.physicalexamdesp),mBONtime + 30000,-1,"false");
                                handler.insertSurveillance(String.valueOf(timeView), String.valueOf(mBONtime), String.valueOf(uniqueid));
                                break;
                        }
//j
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
        if (THYstate) {
            TextViewCompat.setTextAppearance(mTHYtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mTHYtext, R.style.overdueText);
        }
        if (CELstate) {
            TextViewCompat.setTextAppearance(mCELtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mCELtext, R.style.overdueText);
        }

        if (ECGstate) {
            TextViewCompat.setTextAppearance(mECGtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mECGtext, R.style.overdueText);
        }

        if (ECHstate) {
            TextViewCompat.setTextAppearance(mECHtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mECHtext, R.style.overdueText);
        }

        if (CTstate) {
            TextViewCompat.setTextAppearance(mCTtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mCTtext, R.style.overdueText);
        }

        if (PROstate) {
            TextViewCompat.setTextAppearance(mPROtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mPROtext, R.style.overdueText);
        }

        if (VISstate) {
            TextViewCompat.setTextAppearance(mVIStext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mVIStext, R.style.overdueText);
        }

        if (HEAstate) {
            TextViewCompat.setTextAppearance(mHEAtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mHEAtext, R.style.overdueText);
        }

        if (BONstate) {
            TextViewCompat.setTextAppearance(mBONtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mBONtext, R.style.overdueText);
        }
    }

    // Check time and see if need to change colours
    public void checkTime() {
        Log.i("PAP time", String.valueOf(mPAPtime));
        Log.i("Current time", String.valueOf(Calendar.getInstance().getTimeInMillis()));

        //Yearly
        if (Calendar.getInstance().getTimeInMillis() - mPAPtime > 10000 || mPAPtime == -1) {
            PAPstate = false;
        } else {
            PAPstate = true;
        }

        // Yearly
        if (Calendar.getInstance().getTimeInMillis() - mTHYtime > 10000 || mTHYtime == -1) {
            THYstate = false;
        } else {
            THYstate = true;
        }


        // 2-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mCELtime > 10000 || mCELtime == -1) {
            CELstate = false;
        } else {
            CELstate = true;
        }

        //3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mECGtime > 10000 || mECGtime == -1) {
            ECGstate = false;
        } else {
            ECGstate = true;
        }


        if (Calendar.getInstance().getTimeInMillis() - mECHtime > 10000 || mECHtime == -1) {
            ECHstate = false;
        } else {
            ECHstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mCTtime > 10000 || mCTtime == -1) {
            CTstate = false;
        } else {
            CTstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mPROtime > 10000 || mPROtime == -1) {
            PROstate = false;
        } else {
            PROstate = true;
        }

        // 1-2 Years
        if (Calendar.getInstance().getTimeInMillis() - mVIStime > 10000 || mVIStime == -1) {
            VISstate = false;
        } else {
            VISstate = true;
        }

        // 2-3 Years
        if (Calendar.getInstance().getTimeInMillis() - mHEAtime > 10000 || mHEAtime == -1) {
            HEAstate = false;
        } else {
            HEAstate = true;
        }

        //3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mBONtime > 10000 || mBONtime == -1) {
            BONstate = false;
        } else {
            BONstate = true;
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
                //downloadFile();

                // Open and show turner webpage
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.turnersyndrome.ca/wp-content/uploads/TS_Surveillance_Chart_Colour.pdf"));
                startActivity(intent);
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
    // Firebase reference
    FirebaseStorage mStorageRef;
    StorageReference mChartRef;
    File localFile;

    private void downloadFile() {

        mStorageRef = FirebaseStorage.getInstance();
        mChartRef = mStorageRef.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/tguide-4d702.appspot.com/o/jpg-icon.png?alt=media&token=5c9368dd-7fcd-41cc-be2e-1032ed6c236a");

        try {
           localFile = File.createTempFile("images", "png");

            Log.d("SurveillanceTimes:","Get message");
            mChartRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Log.d("SurveillanceTimes:","Download complete");
                            Toast.makeText(getContext(), "Download Complete!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    Toast.makeText(getContext(), "Download Error.", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (IOException ex) {}

        }

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