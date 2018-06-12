package com.example.android.tguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerAct extends AppCompatActivity implements View.OnClickListener{

    //largely inspired from https://www.simplifiedcoding.net/android-firebase-tutorial-1/

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth; //firebase object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance(); //intialize firebase object

       if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        //initializing view
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignin = findViewById(R.id.textViewSignin);
        buttonRegister= findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,getString(R.string.requestemaillogin),Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,getString(R.string.requestpassword),Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("users").child(EncodeString(user.getEmail())).child(user.getUid()).child("baseInfo").child("firstTime").setValue(true);
                            Toast.makeText(registerAct.this,getString(R.string.registersucess),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(registerAct.this,getString(R.string.registerrorr),Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister){
            registerUser();
        }

        if(view == textViewSignin){
            finish();
            startActivity(new Intent(registerAct.this, loginActivity.class));
        }

    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}
