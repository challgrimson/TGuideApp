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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Locale;

// For surveillance time information
public class SurveillenceTimes extends Fragment {
    /**
     * Class used to track the surveillence times of client. TextView class will show a date that is either
     * green or red corresponding to if the appointment is due or not. On click of textview,
     * dialog appears for input of date and time. Date and time is then stored in SQL database.
     * Time is tracked to determine if date has passed or not
     */

    private OnFragmentInteractionListener mListener;

    // Set Variables for layouts
    LinearLayout mPAP, mTHY, mCEL, mECG, mECH, mCT, mPRO, mVIS, mHEA, mBON;

    // Set text view variables
    TextView mPAPtext, mTHYtext, mCELtext, mECGtext, mECHtext, mCTtext, mPROtext, mVIStext, mHEAtext, mBONtext;

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

    // Store time text
    String timeText;

    // Handler to put times in DB database
    ReminderDBHelper handler;

    // Create temporary time when cancelling
    String temptime;

    // Convert date to put in database and for time text
    private SimpleDateFormat dbDateFormate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormate = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    // Time intervals
    private static final long milMonth = 2592000000L;
    private static final long milYear = (milMonth *12L);

    // Define variable to store hearing and heart times - may change
    long heartTime, heartTimeRem, hearingTime, hearingTimeRem;

    // Set Reminder times
    private static final long milYearRem = (milMonth *10L);

    //heartbox/hearing box boolean
    private boolean heartBoxC;
    private boolean hearingBoxC;

    public SurveillenceTimes() {
        // Required empty public constructor
    }

    ViewGroup parent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surveillence_times, container, false);

        parent = container;

        // Send Main Page as a title parameter
        if (mListener != null) {
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
        mBON = view.findViewById(R.id.boneDensity);

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
        mBONtext = view.findViewById(R.id.boneDensitytext);

        // Check to see if new user or if app on new phone and if so then update data
        //if (sharedPref.getBoolean("NEWUSER", true)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("mCELtime")) {
                                // Get user value
                                User user = dataSnapshot.getValue(User.class);

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

                                // Check appropiate colours
                                checkTime();

                                // Make text appropiate colours
                                managecolours();

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
        /*} else {
            // Check appropiate colours
            checkTime();

            // Make text appropiate colours
            managecolours();
        }*/

        // Initilize Calendar variables
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DATE);

        mPAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                PAPstate = true;

                // Set Date and Time
                enterSurveillance(1, mPAPtext);


            }
        });

        mTHY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                THYstate = true;

                enterSurveillance(6, mTHYtext);
            }
        });

        mCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                CELstate = true;

                enterSurveillance(10, mCELtext);
            }
        });

        mECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                ECGstate = true;

                enterSurveillance(11, mECGtext);
            }
        });

        mECH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                ECHstate = true;

                enterSurveillance(12, mECHtext);
            }
        });

        mCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                CTstate = true;

                enterSurveillance(13, mCTtext);
            }
        });

        mPRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                PROstate = true;

                enterSurveillance(14, mPROtext);
            }
        });

        mVIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                VISstate = true;

                enterSurveillance(15, mVIStext);
            }
        });

        mHEA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                HEAstate = true;

                enterSurveillance(16, mHEAtext);
            }
        });

        mBON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                BONstate = true;

                enterSurveillance(17, mBONtext);
            }
        });


        // To change menu
        setHasOptionsMenu(true);

        // Grab DB handler to put in information
        handler = new ReminderDBHelper(getContext());

        // Initilize heart and hearing times
        heartTime = 3L*milYear;
        heartTimeRem = 3L*milYearRem;
        hearingTime = 2L*milYear;
        heartTimeRem = 2L*milYearRem;
        final View view2 = view;
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("heartBox") || dataSnapshot.hasChild("hearingBox")) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            heartBoxC = user.getheartBox();
                            hearingBoxC = user.gethearingBox();


                                if (heartBoxC) {
                                // Change the heart value to have abnormalities and change text
                                heartTime = milYear;
                                heartTimeRem = milYearRem;

                                TextView ECGTime = view2.findViewById(R.id.ECGtime);
                                TextView ECHOTime = view2.findViewById(R.id.EchoTime);
                                TextView CTMRITime = view2.findViewById(R.id.CTMRITime);

                                ECGTime.setText(R.string.yearly);
                                ECHOTime.setText(R.string.yearly);
                                CTMRITime.setText(R.string.yearly);
                            }

                            if (hearingBoxC) {
                                // Change hearing text and time for abnormalities
                                hearingTime = milYear;
                                hearingTimeRem = milYearRem;

                                TextView HearTime = view2.findViewById(R.id.hearingTime);

                                HearTime.setText(R.string.yearly);

                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

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

    public interface OnFragmentInteractionListener {
        //Passed needed title for fragment to MainActivity
        void onFragmentInteraction(String title);

        boolean begin_notifications(int ID, String notificationTitle, String notificationDesp, long alarmTime, long repeatTime, String mActive);

        String generateUniqueID();
    }

    // Saving Date when Paused
    // Save data on pause
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        /*
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
        editor.putString("PAP", PAPsave);
        editor.putString("THY", THYsave);
        editor.putString("CEL", CELsave);
        editor.putString("ECG", ECGsave);
        editor.putString("ECH", ECHsave);
        editor.putString("CT", CTsave);
        editor.putString("PRO", PROsave);
        editor.putString("VIS", VISsave);
        editor.putString("HEA", HEAsave);
        editor.putString("BON", BONsave);


        // Save bool
        editor.putLong("PAPtime", mPAPtime);
        editor.putLong("THYtime", mTHYtime);
        editor.putLong("CELtime", mCELtime);
        editor.putLong("ECGtime", mECGtime);
        editor.putLong("ECHtime", mECHtime);
        editor.putLong("CTtime", mCTtime);
        editor.putLong("PROtime", mPROtime);
        editor.putLong("VIStime", mVIStime);
        editor.putLong("HEAtime", mHEAtime);
        editor.putLong("BONtime", mBONtime);

        // Not a new user
        editor.putBoolean("NEWUSER", false);
        */

        editor.apply();
        fireBaseSave();
    }

    public void fireBaseSave() {
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
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mPAPtext").setValue(PAPsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mTHYtext").setValue(THYsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mCELtext").setValue(CELsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mECGtext").setValue(ECGsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mECHtext").setValue(ECHsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mCTtext").setValue(CTsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mPROtext").setValue(PROsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mVIStext").setValue(VISsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mHEAtext").setValue(HEAsave);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mBONtext").setValue(BONsave);

        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mPAPtime").setValue(mPAPtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mTHYtime").setValue(mTHYtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mCELtime").setValue(mCELtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mECGtime").setValue(mECGtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mECHtime").setValue(mECHtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mCTtime").setValue(mCTtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mPROtime").setValue(mPROtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mVIStime").setValue(mVIStime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mHEAtime").setValue(mHEAtime);
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("mBONtime").setValue(mBONtime);

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
    public void setDate(final TextView datedialog) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog v, int year, int monthOfYear, int dayOfMonth) {
                        String mDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        temptime = mDate;
                        datedialog.setText(mDate);

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        mCalendar.set(Calendar.YEAR, mYear);
                        mCalendar.set(Calendar.MONTH, mMonth);
                        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        mCalendar.set(Calendar.MINUTE, 0);
                        mCalendar.set(Calendar.SECOND, 0);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    // Set the appropiate colours depending on if date has passed or not
    public void managecolours() {
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
        //Yearly
        if (Calendar.getInstance().getTimeInMillis() - mPAPtime > milYear || mPAPtime == -1) {
            PAPstate = false;
        } else {
            PAPstate = true;
        }

        // Yearly
        if (Calendar.getInstance().getTimeInMillis() - mTHYtime > milYear || mTHYtime == -1) {
            THYstate = false;
        } else {
            THYstate = true;
        }


        // 2-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mCELtime > 2L*milYear || mCELtime == -1) {
            CELstate = false;
        } else {
            CELstate = true;
        }

        //3-5 Years
        // MAY NEED TO CHANGE IF DETERMINE PERSON HAS HEART ABNORMALITIES
        if (Calendar.getInstance().getTimeInMillis() - mECGtime > heartTime || mECGtime == -1) {
            ECGstate = false;
        } else {
            ECGstate = true;
        }

        // 3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mECHtime > heartTime || mECHtime == -1) {
            ECHstate = false;
        } else {
            ECHstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mCTtime > heartTime || mCTtime == -1) {
            CTstate = false;
        } else {
            CTstate = true;
        }

        // Yearly
        if (Calendar.getInstance().getTimeInMillis() - mPROtime > milYear || mPROtime == -1) {
            PROstate = false;
        } else {
            PROstate = true;
        }

        // 1-2 Years
        if (Calendar.getInstance().getTimeInMillis() - mVIStime > milYear || mVIStime == -1) {
            VISstate = false;
        } else {
            VISstate = true;
        }

        // 2-3 Years
        if (Calendar.getInstance().getTimeInMillis() - mHEAtime > heartTime || mHEAtime == -1) {
            HEAstate = false;
        } else {
            HEAstate = true;
        }

        //3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mBONtime > 3L*milYear || mBONtime == -1) {
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
                intent.setData(Uri.parse("https://firebasestorage.googleapis.com/v0/b/tguide-d15dc.appspot.com/o/TS_Surveillance_Chart.pdf?alt=media&token=08e584b5-33f3-4669-b61a-e2d63ea7fbb4"));

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

    // Create time dialog to input time
    public void setTime(final TextView dialogText) {
        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                                @Override
                                                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                                                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                                    mCalendar.set(Calendar.MINUTE, minute);
                                                                    timeText = timeFormate.format(mCalendar.getTime());
                                                                    dialogText.setText(timeText);
                                                                }
                                                            },
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                false
        );

        tpd.setThemeDark(false);
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    // Create dialog to input date and time and set reminder
    public void enterSurveillance(final int timeView, final TextView tv) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        mbuilder.setTitle(R.string.surveillancetimes);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.surveillancetimedate, parent, false);

        final RelativeLayout dateLinear = dialogView.findViewById(R.id.dateSurveillacne);
        final RelativeLayout timeLinear = dialogView.findViewById(R.id.timeSurveillacne);

        final TextView dateDialogText = dialogView.findViewById(R.id.dateIdSurveillance);
        final TextView timeDialogText = dialogView.findViewById(R.id.timeIdSurveillance);

        dateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(dateDialogText);

            }
        });

        timeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(timeDialogText);

            }

        });

        mbuilder.setView(dialogView);

        mbuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(dateDialogText.getText().toString()) || TextUtils.isEmpty(timeDialogText.getText().toString())) {
                    Toast.makeText(getContext(), getString(R.string.missinginformation), Toast.LENGTH_LONG).show();
                } else {
                    tv.setText(temptime);
                    TextViewCompat.setTextAppearance(tv, R.style.DateInsert);
                    setReminder(timeView);
                }
            }
        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismess on button click
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alertDialog = mbuilder.create();
        alertDialog.show();
    }

    // Insert reminder
    public void setReminder(final int timeView) {
        // REMINDER TO ALSO SWTICH IN bootReceiver
        Integer uniqueid = Integer.parseInt(mListener.generateUniqueID());
        Integer uniqueid2 = Integer.parseInt(mListener.generateUniqueID());

        switch (timeView) {
            case 1:
                mPAPtime = mCalendar.getTimeInMillis();
                // Set notification for future
                mListener.begin_notifications(uniqueid, getString(R.string.physicalexamTitle),
                        getString(R.string.physicalexamdesp),  mPAPtime + milYearRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.physicalExamApp),
                        getString(R.string.physicalexamdesp), mPAPtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.physicalExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mPAPtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in future
                handler.insertReminder(getString(R.string.physicalexamTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mPAPtime + milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 6:
                mTHYtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.screenbloodTitle),
                        getString(R.string.physicalexamdesp), mTHYtime + milYearRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.screenExamApp),
                        getString(R.string.examRemindDesp), mTHYtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.screenExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mTHYtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.screenbloodTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mTHYtime + milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 10:
                mCELtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.celiacTitle),
                        getString(R.string.physicalexamdesp), mCELtime + 2L*milYearRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.celiacExamApp),
                        getString(R.string.examRemindDesp), mCELtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.celiacExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mCELtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in future
                handler.insertReminder(getString(R.string.celiacTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mCELtime + 2L*milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 11:
                mECGtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.ECGTitle),
                        getString(R.string.physicalexamdesp), mECGtime + heartTimeRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.ECGExamApp),
                        getString(R.string.examRemindDesp), mECGtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.ECGExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mECGtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in future
                handler.insertReminder(getString(R.string.ECGTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mECGtime + heartTime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 12:
                mECHtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.ECHOTitle),
                        getString(R.string.physicalexamdesp), mECHtime + heartTimeRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.ECHOExamApp),
                        getString(R.string.examRemindDesp), mECHtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.ECHOExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mECHtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.ECHOTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mECHtime + heartTime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 13:
                mCTtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.CTMRITitle),
                        getString(R.string.physicalexamdesp), mCTtime + heartTimeRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.CTMRIExamApp),
                        getString(R.string.examRemindDesp), mCTtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.CTMRIExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mCTtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.CTMRITitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mCTtime + heartTime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 14:
                mPROtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.antiproTitle),
                        getString(R.string.physicalexamdesp), mPROtime + milYearRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.antiproApp),
                        getString(R.string.examRemindDesp), mPROtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.antiproApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mPROtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.antiproTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mPROtime + milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 15:
                mVIStime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.visionTitle),
                        getString(R.string.physicalexamdesp), mVIStime + milYearRem + 2L*milMonth, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.visionExamApp),
                        getString(R.string.examRemindDesp), mVIStime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.visionExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mVIStime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.visionTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mVIStime + milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 16:
                mHEAtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.hearingTitle),
                        getString(R.string.physicalexamdesp), mHEAtime + hearingTimeRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.hearingExamApp),
                        getString(R.string.examRemindDesp), mHEAtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.hearingExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mHEAtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.hearingTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mHEAtime + hearingTime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
            case 17:
                mBONtime = mCalendar.getTimeInMillis();
                mListener.begin_notifications(Integer.parseInt(mListener.generateUniqueID()), getString(R.string.bonedensityTitle),
                        getString(R.string.physicalexamdesp), mBONtime + 3L*milYearRem, -1, "false");

                // Set notification for current appointment
                mListener.begin_notifications(uniqueid2, getString(R.string.boneExamApp),
                        getString(R.string.examRemindDesp), mBONtime, -1, "false");

                // Add Current Appoint to Calendar
                handler.insertReminder(getString(R.string.boneExamApp), getString(R.string.examRemindDesp),
                        dbDateFormate.format(mBONtime), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid2));

                // Create event in two days
                handler.insertReminder(getString(R.string.bonedensityTitle), getString(R.string.physicalexamdesp),
                        dbDateFormate.format(mBONtime + 3L*milYear), timeText, "false",
                        String.valueOf(-1), "Minute(s)", "true", String.valueOf(uniqueid));

                break;
        }
        // Toast: Event added to calendar
        Toast.makeText(getContext(), getString(R.string.timetocalendar), Toast.LENGTH_LONG).show();
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }


}