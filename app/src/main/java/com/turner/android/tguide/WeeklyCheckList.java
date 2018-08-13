package com.turner.android.tguide;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyCheckList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyCheckList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyCheckList extends Fragment {
    /**
     * WeeklyChecklist class holst 4 check boxes that are used to help the user
     * stap up to date with their condition. On clicking box 2, dialog appears that will
     * direct user to the calendar fragment if they have no inputted their appointments
     * into the calendar yet.
     */
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Create variables for checkBoxes and refresh
    CheckBox box1;
    CheckBox box2;
    CheckBox box3;
    CheckBox box4;

    public WeeklyCheckList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyCheckList.
     */
    public static WeeklyCheckList newInstance(String param1, String param2) {
        WeeklyCheckList fragment = new WeeklyCheckList();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_check_list, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction(getString(R.string.checklist));
        }

        // Grab views
        box1 = view.findViewById(R.id.checkBox1);
        box2 = view.findViewById(R.id.checkBox2);
        box3 = view.findViewById(R.id.checkBox3);
        box4 = view.findViewById(R.id.checkBox4);

        // To change menu
        setHasOptionsMenu(true);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("clBox1")) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);
                            // Save current state of checked items
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

                            Boolean state1 = sharedPref.getBoolean("box1",user.getClBox1());
                            Boolean state2 = sharedPref.getBoolean("box2",user.getClBox2());
                            Boolean state3 = sharedPref.getBoolean("box3",user.getClBox3());
                            Boolean state4 = sharedPref.getBoolean("box4",user.getClBox4());

                            box1.setChecked(state1);
                            box2.setChecked(state2);
                            box3.setChecked(state3);
                            box4.setChecked(state4);
                        } else  {
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

                            Boolean state1 = sharedPref.getBoolean("box1",false);
                            Boolean state2 = sharedPref.getBoolean("box2",false);
                            Boolean state3 = sharedPref.getBoolean("box3",false);
                            Boolean state4 = sharedPref.getBoolean("box4",false);

                            box1.setChecked(state1);
                            box2.setChecked(state2);
                            box3.setChecked(state3);
                            box4.setChecked(state4);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox1").setValue(false);
                            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox2").setValue(false);
                            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox3").setValue(false);
                            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox4").setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On click ask if they have inputted events to calender

                // Check to see if clicking if unclicked
                if (box2.isChecked() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.appConfirm);
                    builder.setMessage(R.string.appCalendar);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Dismess on button click
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Change to calendar fragment to allow input of events
                            mListener.change_calendar();

                            // Dismess on button click
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });

                    // Create and show the AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menu.clear();
        inflater.inflate(R.menu.checklistmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // If select refresh button then refresh list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        box1.setChecked(false);
        box2.setChecked(false);
        box3.setChecked(false);
        box4.setChecked(false);

        return super.onOptionsItemSelected(item);
    }

    // Save data on pause
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Boolean state1 = box1.isChecked();
        Boolean state2 = box2.isChecked();
        Boolean state3 = box3.isChecked();
        Boolean state4 = box4.isChecked();

        editor.putBoolean("box1",state1);
        editor.putBoolean("box2",state2);
        editor.putBoolean("box3",state3);
        editor.putBoolean("box4",state4);

        editor.apply();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox1").setValue(state1.booleanValue());
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox2").setValue(state2.booleanValue());
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox3").setValue(state3.booleanValue());
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("clBox4").setValue(state4.booleanValue());
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
        void onFragmentInteraction(String title);
        void change_calendar();
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}
