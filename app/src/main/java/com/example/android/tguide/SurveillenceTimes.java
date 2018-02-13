package com.example.android.tguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

// For surveillance time information
public class SurveillenceTimes extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Set Variables
    LinearLayout mPAP;
    TextView mPAPtext;

    public SurveillenceTimes() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SurveillenceTimes newInstance(String param1, String param2) {
        SurveillenceTimes fragment = new SurveillenceTimes();
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
        View view = inflater.inflate(R.layout.fragment_surveillence_times, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction("Surveillance Times");
        }

        // Set LinearLayout variables
        mPAP = (LinearLayout) view.findViewById(R.id.PAP);

        // Set Text variables
        mPAPtext = (TextView) view.findViewById(R.id.PAPtext);

        mPAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call date setter
                setDate(v);
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
        //Passed needed title for fragment to MainActivity
        void onFragmentInteraction(String title);
    }

    // Set date that is specified
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                // UNSURE IF THIS WILL WORK
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
}