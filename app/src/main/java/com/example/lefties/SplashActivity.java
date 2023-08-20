package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Raiyan-Keep user logged in with shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Toast.makeText(getApplicationContext(), sharedPreferences.getString("acctName",""), Toast.LENGTH_LONG).show();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                //check if data present is sharedPreferences
                if (sharedPreferences.contains("isLoggedIn")) {
                    String acnType = sharedPreferences.getString("accountType", "");
                    if(acnType.equals("Customer")){
                        goToCustomerHome();
                    } else {
                        goToRestoHome();
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        };

        Timer opening = new Timer();
        opening.schedule(task,5000);
    }

    public void goToRestoHome() {
        //Raiyan-Keep user logged in with shared preferences
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent i = new Intent(getApplicationContext(),RestaurantHomeActivity.class);
        i.putExtra("acctId", sharedPref.getLong("acctID",0));
        i.putExtra("accName", sharedPref.getString("accName",""));
        startActivity(i);
    }


    public void goToCustomerHome() {
        //Raiyan-Keep user logged in with shared preferences
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent i = new Intent(getApplicationContext(), CustomerHomeActivity.class);
        i.putExtra("acctId", sharedPref.getLong("acctID",0));
        i.putExtra("accName", sharedPref.getString("acctName",""));
        startActivity(i);
    }
}