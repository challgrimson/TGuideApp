package com.example.android.tguide;
// Code for questions
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.GridLayout.LayoutParams;
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
        final Button btn4 = (Button) view.findViewById(R.id.button4);
        final Button btn5 = (Button) view.findViewById(R.id.button5);
        final Button btn6 = (Button) view.findViewById(R.id.button6);

        final TextView tv1 = (TextView) view.findViewById(R.id.text1);
        final TextView tv2 = (TextView) view.findViewById(R.id.text2);
        final TextView tv3 = (TextView) view.findViewById(R.id.text3);
        final TextView tv4 = (TextView) view.findViewById(R.id.text4);
        final TextView tv5 = (TextView) view.findViewById(R.id.text5);
        final TextView tv6 = (TextView) view.findViewById(R.id.text6);

        final Button[] btnArray = {btn1, btn2, btn3, btn4,btn5,btn6};
        final TextView[] tvArray = {tv1,tv2,tv3,tv4,tv5,tv6};

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                for (int i = 0; i < btnArray.length-1; i++)
                    btnArray[i].setEnabled(false);
                buttonResponse(1, btnArray, tvArray, view);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < btnArray.length-1; i++)
                    btnArray[i].setEnabled(false);
                buttonResponse(2, btnArray,tvArray,view);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < btnArray.length-1; i++)
                    btnArray[i].setEnabled(false);
                buttonResponse(3, btnArray,tvArray,view);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < btnArray.length-1; i++)
                    btnArray[i].setEnabled(false);
                buttonResponse(4, btnArray,tvArray,view);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < btnArray.length-1; i++)
                    btnArray[i].setEnabled(false);
                buttonResponse(5, btnArray,tvArray,view);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonResponse(6, btnArray,tvArray,view);
            }
        });


        return view;
    }

    boolean[] posFlag = {false, false, false,false,false,false};
    boolean[] isCollapsed = {false,false,false,false,false,false};

    public void buttonResponse(final int id, final Button[] btnArray, final TextView[] tvArray, View view) {
        float deltaX;
        int[] location = new int[2];
        int[] locationT = new int[2];
        final ScrollView myScrollView = view.findViewById(R.id.SV);

        //myScrollView.setLayoutParams(new ViewGroup.LayoutParams(480, 8000));
       // myScrollView.setLayoutParams(new RelativeLayout.LayoutParams(
               // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        for (int i = id; i < btnArray.length; i++) {
            btnArray[i].getLocationInWindow(location);
            tvArray[i].getLocationInWindow(locationT);

            if (posFlag[id-1] == true) {
                location[1] -= 950;
                locationT[1] -= 950;

            }

            ObjectAnimator animX = ObjectAnimator.ofFloat(btnArray[i], "y", location[1]+120).setDuration(300);
            ObjectAnimator animXT = ObjectAnimator.ofFloat(tvArray[i], "y", locationT[1]+120).setDuration(300);
            animX.start();
            animXT.start();

            animXT.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    for (int i = 0; i < btnArray.length-1; i++)
                        btnArray[i].setEnabled(true);
                    //myScrollView.removeAllViews();
                    //myScrollView.addView(view);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }


        if (posFlag[id-1] == false) {
            posFlag[id-1] = true;
        } else {
            posFlag[id-1] = false;
        }

        if(!isCollapsed[id-1]){
            ValueAnimator va = ValueAnimator.ofInt(0, 432);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    tvArray[id-1].getLayoutParams().height = value.intValue();
                    tvArray[id-1].requestLayout();
                }
            });
            va.start();
            isCollapsed[id-1] = true;
        }
        else{
            ValueAnimator va = ValueAnimator.ofInt(432, 0);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    tvArray[id-1].getLayoutParams().height = value.intValue();
                    tvArray[id-1].requestLayout();
                }
            });
            va.start();
            isCollapsed[id-1] = false;
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
