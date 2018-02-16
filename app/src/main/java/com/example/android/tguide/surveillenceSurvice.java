package com.example.android.tguide;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by zacha_000 on 2018-02-14.
 * Service to change the text colour of surveillanceTimes after input amount of time
 * CURRENTLY UNUSED
 */

public class surveillenceSurvice extends IntentService{
    // Variables to store time and view
    int timeLength, viewCode;

    // Intent to send data back
    Intent intent;
    public static final String BROADCAST_ACTION = "com.example.android.tguide.changeText";
    private final Handler handler = new Handler();

    private Looper mServiceLooper;

    public surveillenceSurvice() {
        super("surveillenceSurvice");
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Change value after certain amount of time
        try {
            Thread.sleep(1000);
            DisplayLoggingInfo();
        } catch (InterruptedException e) {
            // Restore interrpt status
            Thread.currentThread().interrupt();
        }
    }

    /*
    @Override
    public void onCreate() {
        super.onCreate();
        // THREAD?????
        HandlerThread thread = new HandlerThread("ServiceSurveillance");
        thread.start();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // send update after time length MAY WANT TO CHANGE
    }


    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000); // 5 seconds
        }
    };
    */
    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");
        sendBroadcast(intent);
    }
}
