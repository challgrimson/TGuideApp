package com.example.android.tguide;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Reflection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Reflection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reflection extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Set variables
    MediaPlayer reflectionTrack;
    SeekBar seekBar;
    Handler seekHandler = new Handler();

    public Reflection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reflection.
     */
    public static Reflection newInstance(String param1, String param2) {
        Reflection fragment = new Reflection();
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
       View view = inflater.inflate(R.layout.fragment_reflection, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction("Reflection");
        }

        // Grab mp3 file
        reflectionTrack = MediaPlayer.create(getContext(), R.raw.guided_reflection);

        // Grab button and seek bar
        final ImageButton playSound = (ImageButton) view.findViewById(R.id.playAudio);
        final ImageButton pauseSound = (ImageButton) view.findViewById(R.id.pauseAudio);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        // Set max value of seek bar
        seekBar.setMax(reflectionTrack.getDuration());

        // Run seekUpdating to update Seek
        seekUpdating();

        // When click play audio
        playSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        reflectionTrack.start();
                        playSound.setVisibility(View.INVISIBLE);
                        pauseSound.setVisibility(View.VISIBLE);
                }
        });

        // When click play audio
        pauseSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reflectionTrack.pause();
                playSound.setVisibility(View.VISIBLE);
                pauseSound.setVisibility(View.INVISIBLE);
            }
        });

        // If seek bar changed
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //If to prevent blimp in audio due to updating
                if(reflectionTrack.isPlaying() && fromUser) {
                    reflectionTrack.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        void onFragmentInteraction(String title);
    }

    // For Running seekBar
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdating();
        }
    };
    // To Update seekBar
    public void seekUpdating() {
        seekBar.setProgress(reflectionTrack.getCurrentPosition());
        // Delay post by 1 second
        seekHandler.postDelayed(run, 1000);

    }
}
