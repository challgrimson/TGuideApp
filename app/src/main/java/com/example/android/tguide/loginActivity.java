package com.example.android.tguide;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


//mirror of the register
public class loginActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * LoginActivity uses firebase to login in the user. Will then direct to mainactivity.
     * Firebase also set to persist when offline
     */

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView forgotPass;
    private String m_Text = "";
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Persist database when offline
        if (!getIntent().hasExtra("caller")) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

       if(firebaseAuth.getCurrentUser() != null){
           startActivity(new Intent(getApplicationContext(), HomeActivity.class));
           finish();

        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        buttonSignIn =  findViewById(R.id.buttonSignin);
        textViewSignup  =  findViewById(R.id.textViewSignUp);
        forgotPass =  findViewById(R.id.forgotPass);
        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    //method for user login
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,getString(R.string.requestemaillogin),Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,getString(R.string.enterpassword),Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage(getString(R.string.loggingin));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            // Load date from firebase
                            ReminderDBHelper database = new ReminderDBHelper(getApplicationContext());
                            //database.loadFromFirebase();

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(loginActivity.this).create();
                            alertDialog.setTitle(getString(R.string.error));
                            alertDialog.setMessage(getString(R.string.logingeorr));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){

            finish();
            startActivity(new Intent(this, registerAct.class));
        }

        if(view == forgotPass){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.requestemail));

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton(getString(R.string.sendemail), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    firebaseAuth.getInstance().sendPasswordResetEmail(m_Text)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
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

            builder.show();
    }
} }

