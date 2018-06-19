package com.example.android.tguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class accountinfo extends Fragment {
    /**
     * Accountinfo class used to display information for user account. Will display email address
     * and buttons to allow for email addresse, and passwords to be changed. Also, offers the ability
     * to logout or delete user account.
     */

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
                input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mbuilder.setView(input);

                // Set up the buttons
                mbuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString().trim();
                        // Confirm if box is empty
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(getContext(), getString(R.string.boxempty), Toast.LENGTH_LONG).show();
                        } else {
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
                        // Confirm is empty
                        final String SoldPassword = oldPassword.getText().toString().trim();
                        final String SnewPassword = newPassword.getText().toString().trim();
                        final String SconfirmPassword = confirmPassword.getText().toString().trim();

                        if (TextUtils.isEmpty(SoldPassword) & TextUtils.isEmpty(SnewPassword) & TextUtils.isEmpty(SconfirmPassword)) {
                            Toast.makeText(getContext(), getString(R.string.boxempty), Toast.LENGTH_LONG).show();
                        } else {
                            // Authenicate user using oldPassword
                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), SoldPassword);
                            firebaseAuth.getCurrentUser().reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // check if new and old password match
                                                if (SconfirmPassword.equals(SnewPassword)) {
                                                    // change password
                                                    firebaseAuth.getCurrentUser().updatePassword(SnewPassword)
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
                builder.setMessage(R.string.deletemessage);

                // Set View to hold strings
                // Get layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.accountverification, null);

                builder.setView(dialogView);

                final EditText password = dialogView.findViewById(R.id.editText2);

                builder.setPositiveButton(R.string.deleteaccount, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String passwordString  = password.getText().toString().trim();
                        // Determine if edittext is empty or not
                        if (!TextUtils.isEmpty(passwordString)) {
                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), passwordString);

                            // Prompt the user to re-provide their sign-in credentials
                            firebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseAuth.getCurrentUser().delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("accountinfo", "User account deleted.");
                                                        // Delete table to prevent other accounts from seeing it
                                                        ReminderDBHelper database = new ReminderDBHelper(getContext());
                                                        database.deletall();

                                                        Intent intent = new Intent(getActivity(), loginActivity.class);
                                                        intent.putExtra("caller", getContext().getClass().getSimpleName());
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getContext(), getString(R.string.emailchangeerror), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), getString(R.string.passwordnotcorrect), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), getString(R.string.boxempty), Toast.LENGTH_LONG).show();
                        }

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
                builder.setTitle(getString(R.string.resetpassword));
                builder.setMessage(R.string.passwordresetConfirm);

                // Set up the buttons
                builder.setPositiveButton(getString(R.string.sendemail), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.getInstance().sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail())
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
                        // Delete table to prevent other accounts from seeing it
                        ReminderDBHelper database = new ReminderDBHelper(getContext());
                        database.deletall();

                        FirebaseAuth.getInstance().signOut();
                        // Logout
                        Intent intent = new Intent(getActivity(), loginActivity.class);
                        intent.putExtra("caller", getContext().getClass().getSimpleName());
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

        // Grab views
        final CheckBox heartBox = view.findViewById(R.id.heartCheck);
        final CheckBox hearingBox = view.findViewById(R.id.hearingCheck);

        // Set if box is checked or not
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("heartBox") || dataSnapshot.hasChild("hearingBox")) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            heartBox.setChecked(user.getheartBox());
                            hearingBox.setChecked(user.gethearingBox());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        // If checked or not then input data
        heartBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save heart abnormality state
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("heartBox").setValue(heartBox.isChecked());
            }
        });

        hearingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save hearing abnormality state
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("hearingBox").setValue(hearingBox.isChecked());
            }
        });



        return view;
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
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
