package com.turner.android.tguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntroductionTwo extends AppCompatActivity {
    /**
     * Class holds buttons that will determine if the user has a heart abnormality.
     * Will go to HomeActivity after.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_two);

        // Define Layouts
        Button button = findViewById(R.id.introductionTwoBut);
        final CheckBox heartBox = findViewById(R.id.heartBox);
        final CheckBox hearingBox = findViewById(R.id.hearingBox);

        // Set action when clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to first introduction page when clicked
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });


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
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}
