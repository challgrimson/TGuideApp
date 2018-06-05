package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;


public class accountinfo extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView accountemail, deleteacc, logoutacc;

    private Button bchangeemail, bchangepassword, bresetpassword;

    private FirebaseAuth firebaseAuth;

    public accountinfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_accountinfo, container, false);

        // Send account info as a title parameter
        if (mListener != null){
            mListener.onFragmentInteraction(getString(R.string.accountinfo));
        }

        // Grab views
        accountemail = view.findViewById(R.id.emailaccount);
        deleteacc = view.findViewById(R.id.deleteaccount);
        bchangeemail = view.findViewById(R.id.changeemail);
        bchangepassword = view.findViewById(R.id.changepassword);
        bresetpassword = view.findViewById(R.id.resetpassword);
        logoutacc = view.findViewById(R.id.logoutaccount);

        firebaseAuth = FirebaseAuth.getInstance();

        accountemail.setText(firebaseAuth.getCurrentUser().getEmail());

        bchangeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
                mbuilder.setTitle(getString(R.string.changeemaildialog));
                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                mbuilder.setView(input);

                // Set up the buttons
                mbuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.getCurrentUser().updateEmail(input.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("accountinfo", "Email change.");
                                            Toast.makeText(getContext(), getString(R.string.emailchangeconfirm), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), getString(R.string.emailchangeerror), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        // Dismess on button click
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismess on button click
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });
                // Create and show the AlertDialog
                AlertDialog alertDialog = mbuilder.create();
                alertDialog.show();
            }});

        bchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
                mbuilder.setTitle(getString(R.string.changepassword));

                // Set View to hold strings
                // Get layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.accountpassword, null);

                final EditText oldPassword = dialogView.findViewById(R.id.editText);
                final EditText newPassword = dialogView.findViewById(R.id.editText2);
                final EditText confirmPassword = dialogView.findViewById(R.id.editText3);

                mbuilder.setView(dialogView);

                mbuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Authenicate user using oldPassword
                        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), oldPassword.getText().toString());
                        firebaseAuth.getCurrentUser().reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // check if new and old password match
                                            if (newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                                                // change password
                                                firebaseAuth.getCurrentUser().updatePassword(newPassword.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getContext(), getString(R.string.passwordchanged), Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(getContext(), getString(R.string.passwordnotupdated), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.passwordnomatch), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.d("accountinfo", "Error password not update");
                                            Toast.makeText(getContext(), getString(R.string.passwordnotupdated), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismess on button click
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = mbuilder.create();
                alertDialog.show();
            }
        });

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.confirmaccountinformation));

                // Set View to hold strings
                // Get layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.accountverification, null);

                builder.setView(dialogView);

                final EditText email = dialogView.findViewById(R.id.editText);
                final EditText password = dialogView.findViewById(R.id.editText2);

                builder.setPositiveButton(R.string.deleteaccount, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email.getText().toString(), password.getText().toString());

                        // Prompt the user to re-provide their sign-in credentials
                        firebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseAuth.getCurrentUser().delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Log.d("accountinfo", "User account deleted.");
                                                            startActivity(new Intent(getContext(), loginActivity.class));
                                                            getActivity().finish();
                                                        } else {
                                                            Toast.makeText(getContext(), getString(R.string.incorrectcredentials), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                        });
                        // Determine if correct
                        // Dismess on button click
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }});

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

        });


        // Reset password button
        bresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.requestemail));

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton(getString(R.string.sendemail), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        firebaseAuth.getInstance().sendPasswordResetEmail(m_Text)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("loginActivity", "Email sent.");
                                        }
                                    }
                                });
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        logoutacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.logoutconfirmation));
                builder.setMessage(R.string.confirmlogout);

                // Set up the buttons
                builder.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        // Logout
                        Intent intent = new Intent(getActivity(), loginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
}
