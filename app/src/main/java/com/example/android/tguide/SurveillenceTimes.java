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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

// For surveillance time information
public class SurveillenceTimes extends Fragment {

    private OnFragmentInteractionListener mListener;

    // Set Variables for layouts
    LinearLayout mPAP, mBP, mBMI, mLYPH, mMOLE, mTHY, mDIA, mKID, mCHOL, mCEL, mECG, mECH, mCT, mPRO, mVIS, mHEA, mBON ;

    // Set text view variables
    TextView mPAPtext, mBPtext, mBMItext, mLYPHtext, mMOLEtext,
            mTHYtext, mDIAtext, mKIDtext, mCHOLtext, mCELtext, mECGtext, mECHtext, mCTtext, mPROtext, mVIStext, mHEAtext, mBONtext;

    // Set save state variables
    String PAPsave, BPsave, BMIsave, LYPHsave, MOLEsave,
        THYsave, DIAsave, KIDsave, CHOLsave, CELsave, ECGsave, ECHsave, CTsave, PROsave, VISsave, HEAsave, BONsave;

    // Variables to hold states
    Boolean PAPstate, BPstate, BMIstate, LYPHstate, MOLEstate, THYstate, DIAstate, KIDstate, CHOLstate,
            CELstate, ECGstate, ECHstate, CTstate, PROstate, VISstate, HEAstate, BONstate;

    // Hold time set
    long mPAPtime, mBPtime, mBMItime, mLYPHtime, mMOLEtime, mTHYtime, mDIAtime,
            mKIDtime, mCHOLtime, mCELtime, mECGtime, mECHtime, mCTtime, mPROtime, mVIStime, mHEAtime, mBONtime;

    // Create Calendar to set dates
    Calendar mCalendar;

    // temp variables for inputting date
    int mYear, mMonth, mDay;

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
        mPAP = view.findViewById(R.id.PAP);
        mBP = view.findViewById(R.id.bloodPressure);
        mBMI = view.findViewById(R.id.BMI);
        mLYPH = view.findViewById(R.id.lymphedema);
        mMOLE = view.findViewById(R.id.moleAssess);
        mTHY = view.findViewById(R.id.thyroidDisease);
        mDIA = view.findViewById(R.id.diabetes);
        mKID = view.findViewById(R.id.kidneyDisease);
        mCHOL = view.findViewById(R.id.highCholesterol);
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
        mBPtext = view.findViewById(R.id.bloodPressuretext);
        mBMItext = view.findViewById(R.id.BMItext);
        mLYPHtext = view.findViewById(R.id.lymphedemaText);
        mMOLEtext = view.findViewById(R.id.moleAssesstext);
        mTHYtext = view.findViewById(R.id.thyroidDiseasetext);
        mDIAtext = view.findViewById(R.id.diabetestext);
        mKIDtext = view.findViewById(R.id.kidneyDiseasetext);
        mCHOLtext = view.findViewById(R.id.highCholesteroltext);
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

        // Set text
        mPAPtext.setText(sharedPref.getString("PAP", getString(R.string.overdue)));
        mBPtext.setText(sharedPref.getString("BP", getString(R.string.overdue)));
        mBMItext.setText(sharedPref.getString("BMI", getString(R.string.overdue)));
        mLYPHtext.setText(sharedPref.getString("LYPH", getString(R.string.overdue)));
        mMOLEtext.setText(sharedPref.getString("MOLE", getString(R.string.overdue)));
        mTHYtext.setText(sharedPref.getString("THY", getString(R.string.overdue)));
        mDIAtext.setText(sharedPref.getString("DIA", getString(R.string.overdue)));
        mKIDtext.setText(sharedPref.getString("KID", getString(R.string.overdue)));
        mCHOLtext.setText(sharedPref.getString("CHOL", getString(R.string.overdue)));
        mCELtext.setText(sharedPref.getString("CEL", getString(R.string.overdue)));
        mECGtext.setText(sharedPref.getString("ECG", getString(R.string.overdue)));
        mECHtext.setText(sharedPref.getString("ECH", getString(R.string.overdue)));
        mCTtext.setText(sharedPref.getString("CT", getString(R.string.overdue)));
        mPROtext.setText(sharedPref.getString("PRO", getString(R.string.overdue)));
        mVIStext.setText(sharedPref.getString("VIS", getString(R.string.overdue)));
        mHEAtext.setText(sharedPref.getString("HEA", getString(R.string.overdue)));
        mBONtext.setText(sharedPref.getString("BON", getString(R.string.overdue)));
        // Set time
        mPAPtime = sharedPref.getLong("PAPtime", -1);
        mBPtime = sharedPref.getLong("BPtime", -1);
        mBMItime = sharedPref.getLong("BMItime", -1);
        mLYPHtime = sharedPref.getLong("LYPHtime", -1);
        mMOLEtime = sharedPref.getLong("MOLEtime", -1);
        mTHYtime = sharedPref.getLong("THYtime", -1);
        mDIAtime = sharedPref.getLong("DIAtime", -1);
        mKIDtime = sharedPref.getLong("KIDtime", -1);
        mCHOLtime = sharedPref.getLong("CHOLtime", -1);
        mCELtime = sharedPref.getLong("CELtime", -1);
        mECGtime = sharedPref.getLong("ECGtime", -1);
        mECHtime = sharedPref.getLong("ECHtime", -1);
        mCTtime = sharedPref.getLong("CTtime", -1);
        mPROtime = sharedPref.getLong("PROtime", -1);
        mVIStime = sharedPref.getLong("VIStime", -1);
        mHEAtime = sharedPref.getLong("HEAtime", -1);
        mBONtime = sharedPref.getLong("BONtime", -1);

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

        mBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                BPstate = true;

                // Call date setter
                setDate(v, mBPtext, 2);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                BMIstate = true;

                // Call date setter
                setDate(v, mBMItext, 3);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mLYPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                LYPHstate = true;

                // Call date setter
                setDate(v, mLYPHtext, 4);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mMOLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                MOLEstate = true;

                // Call date setter
                setDate(v, mMOLEtext, 5);

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

        mDIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                DIAstate = true;

                // Call date setter
                setDate(v, mDIAtext, 7);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mKID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                KIDstate = true;

                // Call date setter
                setDate(v, mKIDtext, 8);

                //getActivity().startService(intent);
                //getActivity().registerReceiver(broadcastReceiver, new IntentFilter((surveillenceSurvice.BROADCAST_ACTION)));
            }
        });

        mCHOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To turn green on reload
                CHOLstate = true;

                // Call date setter
                setDate(v, mCHOLtext, 9);

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

        // Grab text
        PAPsave = mPAPtext.getText().toString();
        BPsave = mBPtext.getText().toString();
        LYPHsave = mLYPHtext.getText().toString();
        BMIsave = mBMItext.getText().toString();
        MOLEsave = mMOLEtext.getText().toString();
        THYsave = mTHYtext.getText().toString();
        DIAsave = mDIAtext.getText().toString();
        KIDsave = mKIDtext.getText().toString();
        CHOLsave = mCHOLtext.getText().toString();
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
        editor.putString("BP",BPsave);
        editor.putString("LYPH",LYPHsave);
        editor.putString("BMI",BMIsave);
        editor.putString("MOLE",MOLEsave);
        editor.putString("THY",THYsave);
        editor.putString("DIA",DIAsave);
        editor.putString("KID",KIDsave);
        editor.putString("CHOL",CHOLsave);
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
        editor.putLong("BPtime",mBPtime);
        editor.putLong("LYPHtime",mLYPHtime);
        editor.putLong("BMItime",mBMItime);
        editor.putLong("MOLEtime",mMOLEtime);
        editor.putLong("THYtime",mTHYtime);
        editor.putLong("DIAtime",mDIAtime);
        editor.putLong("KIDtime",mKIDtime);
        editor.putLong("CHOLtime",mCHOLtime);
        editor.putLong("CELtime",mCELtime);
        editor.putLong("ECGtime",mECGtime);
        editor.putLong("ECHtime",mECHtime);
        editor.putLong("CTtime",mCTtime);
        editor.putLong("PROtime",mPROtime);
        editor.putLong("VIStime",mVIStime);
        editor.putLong("HEAtime",mHEAtime);
        editor.putLong("BONtime",mBONtime);


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

                        switch (timeView) {
                            case 1:
                                mPAPtime = mCalendar.getTimeInMillis();
                                break;
                            case 2:
                                mBPtime = mCalendar.getTimeInMillis();
                                break;
                            case 3:
                                mBMItime = mCalendar.getTimeInMillis();
                                break;
                            case 4:
                                mLYPHtime = mCalendar.getTimeInMillis();
                                break;
                            case 5:
                                mMOLEtime = mCalendar.getTimeInMillis();
                                break;
                            case 6:
                                mTHYtime = mCalendar.getTimeInMillis();
                                break;
                            case 7:
                                mDIAtime = mCalendar.getTimeInMillis();
                                break;
                            case 8:
                                mKIDtime = mCalendar.getTimeInMillis();
                                break;
                            case 9:
                                mCHOLtime = mCalendar.getTimeInMillis();
                                break;
                            case 10:
                                mCELtime = mCalendar.getTimeInMillis();
                                break;
                            case 11:
                                mECGtime = mCalendar.getTimeInMillis();
                                break;
                            case 12:
                                mECHtime = mCalendar.getTimeInMillis();
                                break;
                            case 13:
                                mCTtime = mCalendar.getTimeInMillis();
                                break;
                            case 14:
                                mPROtime = mCalendar.getTimeInMillis();
                                break;
                            case 15:
                                mVIStime = mCalendar.getTimeInMillis();
                                break;
                            case 16:
                                mHEAtime = mCalendar.getTimeInMillis();
                                break;
                            case 17:
                                mBONtime = mCalendar.getTimeInMillis();
                                break;
                        }

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

        if (BPstate) {
            TextViewCompat.setTextAppearance(mBPtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mBPtext, R.style.overdueText);
        }

        if (BMIstate) {
            TextViewCompat.setTextAppearance(mBMItext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mBMItext, R.style.overdueText);
        }

        if (LYPHstate) {
            TextViewCompat.setTextAppearance(mLYPHtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mLYPHtext, R.style.overdueText);
        }

        if (MOLEstate) {
            TextViewCompat.setTextAppearance(mMOLEtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mMOLEtext, R.style.overdueText);
        }

        if (THYstate) {
            TextViewCompat.setTextAppearance(mTHYtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mTHYtext, R.style.overdueText);
        }

        if (DIAstate) {
            TextViewCompat.setTextAppearance(mDIAtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mDIAtext, R.style.overdueText);
        }

        if (KIDstate) {
            TextViewCompat.setTextAppearance(mKIDtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mKIDtext, R.style.overdueText);
        }

        if (CHOLstate) {
            TextViewCompat.setTextAppearance(mCHOLtext, R.style.DateInsert);
        } else {
            TextViewCompat.setTextAppearance(mCHOLtext, R.style.overdueText);
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

        //1-2 Years
        if (Calendar.getInstance().getTimeInMillis() - mPAPtime > 2*24*60*1000 || mPAPtime == -1) {
            PAPstate = false;
        } else {
            PAPstate = true;
        }

        // Yearly
        if (Calendar.getInstance().getTimeInMillis() - mBPtime > 24*60*1000 || mBPtime == -1) {
            BPstate = false;
        } else {
            BPstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mBMItime > 24*60*1000 || mBMItime == -1) {
            BMIstate = false;
        } else {
            BMIstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mLYPHtime > 24*60*1000 || mLYPHtime == -1) {
            LYPHstate = false;
        } else {
            LYPHstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mMOLEtime > 24*60*1000 || mMOLEtime == -1) {
            MOLEstate = false;
        } else {
            MOLEstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mTHYtime > 24*60*1000 || mTHYtime == -1) {
            THYstate = false;
        } else {
            THYstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mDIAtime > 24*60*1000 || mDIAtime == -1) {
            DIAstate = false;
        } else {
            DIAstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mKIDtime > 24*60*1000 || mKIDtime == -1) {
            KIDstate = false;
        } else {
            KIDstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mCHOLtime > 24*60*1000 || mCHOLtime == -1) {
            CHOLstate = false;
        } else {
            CHOLstate = true;
        }

        // 2-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mCELtime > 5*24*60*1000 || mCELtime == -1) {
            CELstate = false;
        } else {
            CELstate = true;
        }

        //3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mECGtime > 5*24*60*1000 || mECGtime == -1) {
            ECGstate = false;
        } else {
            ECGstate = true;
        }


        if (Calendar.getInstance().getTimeInMillis() - mECHtime > 5*24*60*1000 || mECHtime == -1) {
            ECHstate = false;
        } else {
            ECHstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mCTtime > 5*24*60*1000 || mCTtime == -1) {
            CTstate = false;
        } else {
            CTstate = true;
        }

        if (Calendar.getInstance().getTimeInMillis() - mPROtime > 24*60*1000 || mPROtime == -1) {
            PROstate = false;
        } else {
            PROstate = true;
        }

        // 1-2 Years
        if (Calendar.getInstance().getTimeInMillis() - mVIStime > 2*24*60*1000 || mVIStime == -1) {
            VISstate = false;
        } else {
            VISstate = true;
        }

        // 2-3 Years
        if (Calendar.getInstance().getTimeInMillis() - mHEAtime > 3*24*60*1000 || mHEAtime == -1) {
            HEAstate = false;
        } else {
            HEAstate = true;
        }

        //3-5 Years
        if (Calendar.getInstance().getTimeInMillis() - mBONtime > 5*24*60*1000 || mBONtime == -1) {
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