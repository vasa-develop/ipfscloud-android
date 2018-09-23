package com.tbc.vasa.ipfscloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Splash extends AppCompatActivity {

    private String TAG = "SPLASH: ";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        settings = getSharedPreferences("account", MODE_PRIVATE);

        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                String value = settings.getString("pubKey1", "");
                Log.d(TAG, value);
                if(value.isEmpty()){
                    Intent i = new Intent(Splash.this, Sync.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(Splash.this, Documents.class);
                    startActivity(i);
                }

            }
        }.start();
    }
}
