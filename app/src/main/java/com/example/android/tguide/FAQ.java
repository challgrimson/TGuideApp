package com.example.android.tguide;
// Code for questions
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
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

        final TextView tv1 = (TextView) view.findViewById(R.id.text1);

        final Button[] btnArray = {btn1, btn2, btn3};
        final TextView[] tvArray = {tv1};

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonResponse(1, btnArray, tvArray, view);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonResponse(2, btnArray,tvArray,view);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonResponse(3, btnArray,tvArray,view);
            }
        });


        return view;
    }

    boolean[] posFlag = {false, false, false};
    boolean[] isCollapsed = {false};

    public void buttonResponse(int id, Button[] btnArray, final TextView[] tvArray, View view) {
        float deltaX;
        int[] location = new int[2];



        for (int i = id; i < btnArray.length; i++) {
            btnArray[i].getLocationInWindow(location);

            if (posFlag[id] == true)
                location[1] -= 940;

            ObjectAnimator animX = ObjectAnimator.ofFloat(btnArray[i], "y", location[1]).setDuration(300);
            animX.start();

        }


        if (posFlag[id] == false) {
            posFlag[id] = true;
        } else {
            posFlag[id] = false;
        }

        if(!isCollapsed[0]){
            ValueAnimator va = ValueAnimator.ofInt(0, 432);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    tvArray[0].getLayoutParams().height = value.intValue();
                    tvArray[0].requestLayout();
                }
            });
            va.start();
            isCollapsed[0] = true;
        }
        else{
            ValueAnimator va = ValueAnimator.ofInt(432, 0);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    tvArray[0].getLayoutParams().height = value.intValue();
                    tvArray[0].requestLayout();
                }
            });
            va.start();
            isCollapsed[0] = false;
        }
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
