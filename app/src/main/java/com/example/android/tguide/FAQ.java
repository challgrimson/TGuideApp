package com.example.android.tguide;
// Code for questions

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAQ#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQ extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Set variables
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    Boolean state1, state2, state3, state4, state5, state6;

    public FAQ() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAQ.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQ newInstance(String param1, String param2) {
        FAQ fragment = new FAQ();
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
        final View view = inflater.inflate(R.layout.fragment_faq, container, false);

        // Send Main Page as a title parameter
        if (mListener != null) {
            mListener.onFragmentInteraction("FAQ");
        }

        //current implementation of setting clicking for FAQ buttons, sure will be changed
        //------
        final Button btn1 = (Button) view.findViewById(R.id.button1);
        final Button btn2 = (Button) view.findViewById(R.id.button2);
        final Button btn3 = (Button) view.findViewById(R.id.button3);
        final Button btn4 = (Button) view.findViewById(R.id.button4);
        final Button btn5 = (Button) view.findViewById(R.id.button5);
        final Button btn6 = (Button) view.findViewById(R.id.button6);

        tv1 = (TextView) view.findViewById(R.id.text1);
        tv2 = (TextView) view.findViewById(R.id.text2);
        tv3 = (TextView) view.findViewById(R.id.text3);
        tv4 = (TextView) view.findViewById(R.id.text4);
        tv5 = (TextView) view.findViewById(R.id.text5);
        tv6 = (TextView) view.findViewById(R.id.text6);

        state1 = false;
        state2 = false;
        state3 = false;
        state4 = false;
        state5 = false;
        state6 = false;

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state1) {
                    tv1.setVisibility(View.GONE);
                    state1 = false;
                } else {
                    tv1.setVisibility(View.VISIBLE);
                    state1 = true;
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state2) {
                    tv2.setVisibility(View.GONE);
                    state2 = false;
                } else {
                    tv2.setVisibility(View.VISIBLE);
                    state2 = true;
                }

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state3) {
                    tv3.setVisibility(View.GONE);
                    state3 = false;
                } else {
                    tv3.setVisibility(View.VISIBLE);
                    state3 = true;
                }

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state4) {
                    tv4.setVisibility(View.GONE);
                    state4 = false;
                } else {
                    tv4.setVisibility(View.VISIBLE);
                    state4 = true;
                }

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state5) {
                    tv5.setVisibility(View.GONE);
                    state5 = false;
                } else {
                    tv5.setVisibility(View.VISIBLE);
                    state5 = true;
                }

            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state6) {
                    tv6.setVisibility(View.GONE);
                    state6 = false;
                } else {
                    tv6.setVisibility(View.VISIBLE);
                    state6 = true;
                }

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
}
