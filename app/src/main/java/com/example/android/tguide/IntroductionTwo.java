package com.example.android.tguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

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

        // TODO: Delete these four lines after put into firebase
        // Delete after firebase
        // Set shared preference values
        final SharedPreferences sharedPref = this.getSharedPreferences("TGUIDE", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        heartBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Put this into Firebase
                editor.putBoolean("heart", heartBox.isChecked());
                editor.apply();
            }
        });

        hearingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Put this into Firebase
                editor.putBoolean("hearing", hearingBox.isChecked());
                editor.apply();
            }
        });
    }
}
