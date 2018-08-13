package com.turner.android.tguide;
// Code for questions

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FAQ extends Fragment {
    /**
     * FAQ class holds textviews that when clicked reveal a textview below containing information
     * about the question.
     */
    private OnFragmentInteractionListener mListener;

    // Set variables
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    Boolean state1, state2, state3, state4, state5, state6, state7;

    public FAQ() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_faq, container, false);

        // Send Main Page as a title parameter
        if (mListener != null) {
            mListener.onFragmentInteraction(getString(R.string.faqdrawer));
        }

        //current implementation of setting clicking for FAQ buttons, sure will be changed
        //------
        final Button btn1 = view.findViewById(R.id.button1);
        final Button btn2 = view.findViewById(R.id.button2);
        final Button btn3 = view.findViewById(R.id.button3);
        final Button btn4 = view.findViewById(R.id.button4);
        final Button btn5 = view.findViewById(R.id.button5);
        final Button btn6 = view.findViewById(R.id.button6);
        final Button btn7 = view.findViewById(R.id.button7);


        tv1 = view.findViewById(R.id.text1);
        tv2 = view.findViewById(R.id.text2);
        tv3 = view.findViewById(R.id.text3);
        tv4 = view.findViewById(R.id.text4);
        tv5 = view.findViewById(R.id.text5);
        tv6 = view.findViewById(R.id.text6);
        tv7 = view.findViewById(R.id.text7);


        state1 = false;
        state2 = false;
        state3 = false;
        state4 = false;
        state5 = false;
        state6 = false;
        state7 = false;


        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state1) {
                    tv1.setVisibility(View.GONE);
                    state1 = false;
                    btn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv1.setVisibility(View.VISIBLE);
                    state1 = true;
                    btn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state2) {
                    tv2.setVisibility(View.GONE);
                    state2 = false;
                    btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv2.setVisibility(View.VISIBLE);
                    state2 = true;
                    btn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                }

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state3) {
                    tv3.setVisibility(View.GONE);
                    state3 = false;
                    btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv3.setVisibility(View.VISIBLE);
                    state3 = true;
                    btn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state4) {
                    tv4.setVisibility(View.GONE);
                    state4 = false;
                    btn4.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv4.setVisibility(View.VISIBLE);
                    state4 = true;
                    btn4.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state5) {
                    tv5.setVisibility(View.GONE);
                    state5 = false;
                    btn5.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv5.setVisibility(View.VISIBLE);
                    state5 = true;
                    btn5.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }

            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state6) {
                    tv6.setVisibility(View.GONE);
                    state6 = false;
                    btn6.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv6.setVisibility(View.VISIBLE);
                    state6 = true;
                    btn6.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }

            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (state7) {
                    tv7.setVisibility(View.GONE);
                    state7 = false;
                    btn7.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    tv7.setVisibility(View.VISIBLE);
                    state7 = true;
                    btn7.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
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
