package com.example.emptyviewstemplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private String homeTown;
    private String homeTimeZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button save_button = (Button) findViewById(R.id.save_button);

        // get the shared timezone in app
        Context context = getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        homeTown = mPrefs.getString("homeTown", "Baltimore");
        homeTimeZone = mPrefs.getString("homeTimezone", "America/New_York");

        // initialize the EditText widget for the home town input
        EditText homeTownInput = findViewById(R.id.home_town_input);
        homeTownInput.setText(homeTown);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // do stuff here
        super.onDestroy();
    }

    /** Called when the user clicks the save button */
    public void setting(View view) {
        // Do something in response to button
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
