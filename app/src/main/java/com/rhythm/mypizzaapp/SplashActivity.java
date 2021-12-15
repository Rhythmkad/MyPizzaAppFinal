package com.rhythm.mypizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/** This activity is the launcher activity
 * A Splash Screen is a screen that appears when you open an app on your mobile device.
 * Sometimes it’s referred to as a launch screen or startup screen and shows up
 * when your app is loading after you’ve just opened it.
 * When the loading is finished, you’ll be taken to a more functional screen where you can complete actions.
 *  */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize();
    }

    private void initialize() {

        // here we are implementing splash screen through thread timer for shows up for 2-3 seconds then
        // go to AuthenticationActivity..
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }, 3000);
    }
}