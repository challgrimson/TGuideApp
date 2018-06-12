package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {
    /**
     * HomePage is view when first entering app. It contains twitter in a webview and
     * card with emergency information that when clicked opens a dialog that requires a password to
     * change the information.
     */

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressBar progressBar;
    private WebView webview;
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    // grab view to to used in setting emergency checklist
    View checkitem1, checkitem2, checkitem3;
    TextView checktext1, checktext2, checktext3;
    CheckBox box1, box2, box3;
    Button symptomsbut;

    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Send Main Page as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction(getString(R.string.home));
        }

        // Grab checklist views
        View checkitem1 = view.findViewById(R.id.item1);
        View checkitem2 = view.findViewById(R.id.item2);
        View checkitem3 = view.findViewById(R.id.item3);

        // Grab symptoms
        symptomsbut = view.findViewById(R.id.sympButton);

        // Grab textViews
        TextView checkText1 = checkitem1.findViewById(R.id.checktext);
        TextView checkText2 = checkitem2.findViewById(R.id.checktext);
        TextView checkText3 = checkitem3.findViewById(R.id.checktext);

        // Set texts
        checkText1.setText(R.string.emergencyItem1);
        checkText2.setText(R.string.emergencyItem2);
        checkText3.setText(R.string.emergencyItem3);

        // Grab check box
        box1 = checkitem1.findViewById(R.id.checkbox);
        box2 = checkitem2.findViewById(R.id.checkbox);
        box3 = checkitem3.findViewById(R.id.checkbox);

        // Save current state of checked items
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("usersRdCard").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("box1")) {

                            User userdata = dataSnapshot.getValue(User.class);
                            //figure out why onyl works for User class??????????????
                            box1.setChecked(userdata.getbox1());
                            box2.setChecked(userdata.getbox2());
                            box3.setChecked(userdata.getbox3());
                            //Log.i("notepadCHECK",userNpdata.getNotepadText());
                        } else  {
                            box1.setChecked(false);
                            box2.setChecked(false);
                            box3.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        // Send confirmation before changing
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeConfirmation(getString(R.string.checkedBAV), box1);
            }
        });

        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeConfirmation(getString(R.string.checkedAC), box2);
            }
        });

        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeConfirmation(getString(R.string.checkedBAV), box3);
            }
        });

        // If clicked show list of symptoms
        symptomsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create alert dialoog showing symptoms
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.symbuttonmessage);
                builder.setNeutralButton(R.string.symptombuttonexit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismess on button click
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Grab Image Button
        ImageButton info = view.findViewById(R.id.cardinfo);

        // Set to explanation when clicked
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create alert dialoog showing symptoms
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.cardinfo);
                builder.setNeutralButton(R.string.symptombuttonexit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismess on button click
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        //Grab list view
        /*
        list = view.findViewById(android.R.id.list);

        Log.d("TWITTER","running twitter");

        //mListener.twitterfeed(list);
        // Initilize Twitter feed

        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("DbiYTJs0BCJyK4Q6Q6K7Zjyuz", "nawlqnzP5hoWHIwa4vZ314GTCjhOpnvJvdHrTLY3I7FfWrWMhS"))
                .debug(true)
                .build();
        Twitter.initialize(config);

        Thread thread = new Thread() {
            @Override
            public void run() {
                TweetUi.getInstance();
            }
        };
        thread.start();

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("twitterdev")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        list.setAdapter(adapter);
        */

        // String data taken from twitter publish
        // MIGHT HAVE TO CHANGE FOR FRENCH
        //String data = "<a class=\"twitter-timeline\" href=\"https://twitter.com/TSSCCanada?ref_src=twsrc%5Etfw\">Tweets by TSSCCanada</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        String data = "<a class=\"twitter-timeline\" data-link-color=\"#981CEB\" href=\"https://twitter.com/TSSCCanada?ref_src=twsrc%5Etfw\">Tweets by TSSCCanada</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        webview = view.findViewById(R.id.twitterfeed);
        progressBar = view.findViewById((R.id.simpleProgressBar));
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setBackgroundColor(0);
        startWebView(webview,data);
        //webview.loadUrl("https://twitter.com/TSSCCanada?ref_src=twsrc%5Etfw&ref_url=http%3A%2F%2Fwww.turnersyndrome.ca%2Fabout-turner-syndrome%2F");
        //webview.loadDataWithBaseURL("https://twitter.com",data,"text/html","UTF-8",null);

        return view;
    }

    private void startWebView(final WebView webView, final String data) {
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.INVISIBLE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadDataWithBaseURL("https://twitter.com",data,"text/html","UTF-8",null);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


            }
        });
        webView.loadDataWithBaseURL("https://twitter.com",data,"text/html","UTF-8",null);
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

    // Save data on pause
    @Override
    public void onPause(){
        super.onPause();
        Boolean state1 = box1.isChecked();
        Boolean state2 = box2.isChecked();
        Boolean state3 = box3.isChecked();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("usersRdCard").child("box1").setValue(state1.booleanValue());
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("usersRdCard").child("box2").setValue(state2.booleanValue());
        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("usersRdCard").child("box3").setValue(state3.booleanValue());
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

    // To determine if true or false;
    CheckBox temp;

    public void changeConfirmation(String message, CheckBox box) {
        final String passcode = randomString();
        temp = box;
        // Set confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message + " " + passcode);

        // Crete edit text to input value
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |  InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        builder.setView(input);

        // Keep in original position
        temp.toggle();

        builder.setPositiveButton(R.string.emergConfirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // Determine if correct
                if (!input.getText().toString().equals(passcode)){
                    Toast.makeText(getContext(),R.string.incorrectconfirmtion,Toast.LENGTH_SHORT).show();
                } else {
                    temp.toggle();
                }

                // Dismess on button click
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.emergCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismess on button click
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Method to generate random string for passcode
    public String randomString(){
        String letnum = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder passcode = new StringBuilder();
        Random rand = new Random();

        while (passcode.length() < 8) {
            passcode.append(letnum.charAt(rand.nextInt(letnum.length())));
        }

        return passcode.toString();
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
