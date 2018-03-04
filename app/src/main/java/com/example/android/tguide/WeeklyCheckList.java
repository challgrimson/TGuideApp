package com.example.android.tguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyCheckList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyCheckList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyCheckList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
            mListener.onFragmentInteraction("Weekly Checklist");
        }

        // Grab views
        box1 = view.findViewById(R.id.checkBox1);
        box2 = view.findViewById(R.id.checkBox2);
        box3 = view.findViewById(R.id.checkBox3);
        box4 = view.findViewById(R.id.checkBox4);

        // To change menu
        setHasOptionsMenu(true);

        // Save current state of checked items
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        Boolean state1 = sharedPref.getBoolean("box1",false);
        Boolean state2 = sharedPref.getBoolean("box2",false);
        Boolean state3 = sharedPref.getBoolean("box3",false);
        Boolean state4 = sharedPref.getBoolean("box4",false);

        box1.setChecked(state1);
        box2.setChecked(state2);
        box3.setChecked(state3);
        box4.setChecked(state4);

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
    }
}
