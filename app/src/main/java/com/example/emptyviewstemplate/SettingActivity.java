package com.example.emptyviewstemplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    private String homeTown;
    private String homeTimeZone;
    private HashMap<String, String> timeZoneMap = new HashMap<>();

    private void setTimeZoneMap() {
        timeZoneMap.put("America/New_York", "GMT -05:00");
        timeZoneMap.put("America/Los_Angeles", "GMT -08:00");
        timeZoneMap.put("Europe/Berlin", "GMT +01:00");
        timeZoneMap.put("Europe/Istanbul", "GMT +02:00");
        timeZoneMap.put("Asia/Singapore", "GMT +08:00");
        timeZoneMap.put("Asia/Tokyo", "GMT +09:00");
        timeZoneMap.put("Australia/Canberra", "GMT +10:00");
        timeZoneMap.put("Asia/Shanghai", "GMT +08:00");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTimeZoneMap();

        Button save_button = (Button) findViewById(R.id.save_button);

        // get the shared timezone and hometown info in app
        Context context = getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        homeTown = mPrefs.getString("homeTown", "Baltimore");
        homeTimeZone = mPrefs.getString("homeTimeZone", "America/Los_Angeles");

        //set default GMT location time diff
        TextView home_time_gmt = findViewById(R.id.cur_timezone_num);
        home_time_gmt.setText(timeZoneMap.get(homeTimeZone));

        // initialize the EditText widget for the home town input
        EditText homeTownInput = findViewById(R.id.home_town_input);
        homeTownInput.setText(homeTown);

        // set the spinner
        Spinner spinner = findViewById(R.id.timezone_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int position = adapter.getPosition(homeTimeZone);
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homeTimeZone = parent.getItemAtPosition(position).toString();
                int tzPosition = adapter.getPosition(homeTimeZone);
                spinner.setSelection(tzPosition);
                ((TextView) parent.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
                ((TextView) parent.getSelectedView()).setTextSize(34);
                home_time_gmt.setText(timeZoneMap.get(homeTimeZone));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        save_button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mPrefs.edit();
            // get the home town input from the EditText widget and push to shared preference:
            String homeTown = homeTownInput.getText().toString();
            if (homeTown.length() > 0) {
                editor.putString("homeTown", homeTown);
            }
            editor.putString("homeTimeZone", homeTimeZone);
            editor.apply();
            finish(); // finish the activity
        });
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
}
