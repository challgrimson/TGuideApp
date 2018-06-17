package com.example.android.tguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroductionOne extends AppCompatActivity {
    /**
     * Class shows activity that introduces user and will go to next introduction
     * layout when button is clicked
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_one);

        Button button = findViewById(R.id.introductionOneBut);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to second introduction page when clicked
                startActivity(new Intent(getApplicationContext(), IntroductionTwo.class));
            }
        });
    }
}
