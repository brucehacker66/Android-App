package com.example.emptyviewstemplate;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private SharedPreferences mPrefs;
    Button timeButton;
    HashMap<String, Integer> timezone_map = new HashMap<>();

    private String cur_zone, home_zone;
    int original_hr, original_min;
    int converted_hr, converted_min;
    LocalTime hometime, convertime;
    private final View.OnClickListener convertListener = new View.OnClickListener() {
        public void onClick(View v) {
            conversion();
        }
    };

    private void conversion() {
        converted_hr = original_hr + (timezone_map.get(home_zone) - timezone_map.get(cur_zone));
        if (converted_hr > 24) {
            converted_hr = converted_hr % 24;
        } else if (converted_hr < 0) {
            converted_hr = 24 + converted_hr;
        }
        converted_min = original_min;
        TextView con_time = findViewById(R.id.converted_time);
        con_time.setText(String.format(Locale.getDefault(), "%02d:%02d", converted_hr, converted_min));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get the shared current timezone and home time zone info in app
        Context context = getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        cur_zone = mPrefs.getString("currentTimeZone", "America/New_York");
        home_zone = mPrefs.getString("homeTimeZone", "America/Los_Angeles");

        setTimezone();

        //set default timezones
        TextView cur_zone_label = findViewById(R.id.cur_zone_text);
        cur_zone_label.setText(cur_zone);
        TextView home_zone_label = findViewById(R.id.home_zone_text);
        home_zone_label.setText(home_zone);

        //set to current time
        Calendar calendar = Calendar.getInstance();
        original_hr =  calendar.get(Calendar.HOUR_OF_DAY);
        original_min = calendar.get(Calendar.MINUTE);

        //set converted time to the current time
        timeButton = findViewById(R.id.original_time_display);
        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", original_hr, original_min));

        conversion(); //perform initial conversion with current time and default timezone

        //set up the spinner for selecting timezone locations
        Spinner spinner = findViewById(R.id.timezone_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur_zone = parent.getItemAtPosition(position).toString();
                TextView cur_time = findViewById(R.id.cur_zone_text);
                cur_time.setText(cur_zone);
                SharedPreferences.Editor editor = mPrefs.edit();
                //save timezone location to sharedPreferences
                editor.putString("currentTimeZone", cur_zone);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing0
            }

        });


        Button convert = findViewById(R.id.convert_button);
        convert.setOnClickListener(convertListener);
    }

    private void setTimezone() {
        timezone_map.put("America/New_York", -5);
        timezone_map.put("America/Los_Angeles", -8);
        timezone_map.put("Europe/Berlin", 1);
        timezone_map.put("Europe/Istanbul", 2);
        timezone_map.put("Asia/Singapore", 8);
        timezone_map.put("Asia/Tokyo", 9);
        timezone_map.put("Australia/Canberra", 10);
        timezone_map.put("Asia/Shanghai", 8);
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


    public void setting(View view) {
        // Do something in response to button

        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //the time picker
    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                original_hr = selectedHour;
                original_min = selectedMinute;
                //convertime = LocalTime.of(selectedHour, selectedMinute);
                //set up the original time button
                timeButton = findViewById(R.id.original_time_display);
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", original_hr, original_min));
                TextView cur_zone_label = findViewById(R.id.cur_zone_text);
                cur_zone_label.setText(cur_zone);
            }

        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, original_hr, original_min, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}