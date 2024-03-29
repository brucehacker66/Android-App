package com.example.emptyviewstemplate;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private HashMap<String, String> timeZoneMap = new HashMap<>();
    private String cur_zone, home_zone;
    Calendar ori_time, con_time;
    ImageView time_warning;

    private void conversion() {
        if(home_zone.equals(cur_zone)) {
            //error handling
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.toast_format,
                    (ViewGroup) findViewById(R.id.toast_layout));
            Toast toast = new Toast(getApplicationContext());
            toast.setView(view);
            toast.show();
            time_warning.setVisibility(View.GONE); //remove the previous alert
        } else {
            con_time.set(Calendar.HOUR_OF_DAY, ori_time.get(Calendar.HOUR_OF_DAY) + (timezone_map.get(home_zone) - timezone_map.get(cur_zone)));
            con_time.set(Calendar.MINUTE, ori_time.get(Calendar.MINUTE));
            updateImageViewVisibility(); //check rest hour warning
            TextView con_time_text = findViewById(R.id.converted_time);
            String con_time_format = format_time(con_time);
            con_time_text.setText(con_time_format);
        }
    }

    private String format_time(Calendar calendar){
        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12; // Change 0 to 12 if hour is "00"
        }
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            return String.format(Locale.getDefault(), "%02d:%02d AM", hour, calendar.get(Calendar.MINUTE));
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d PM", hour, calendar.get(Calendar.MINUTE));
        }
    }

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

    private void updateImageViewVisibility() {
        int time = con_time.get(Calendar.HOUR_OF_DAY);
        if (time >= 23 || time < 7) {
            time_warning.setVisibility(View.VISIBLE);
        } else {
            time_warning.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the shared current timezone and home time zone info in app
        Context context = getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        cur_zone = "America/New_York";
        home_zone = mPrefs.getString("homeTimeZone", "America/Los_Angeles");

        setTimezone();
        setTimeZoneMap();

        ori_time = Calendar.getInstance(TimeZone.getTimeZone(cur_zone));  //set to current time of the chosen current time zone
        con_time = Calendar.getInstance(TimeZone.getTimeZone(home_zone)); //set up the Calendar object for the home zone

        //set home timezones
        TextView home_zone_label = findViewById(R.id.home_zone_text);
        home_zone_label.setText(home_zone);

        //set default GMT location time diff
        TextView home_time_gmt = findViewById(R.id.home_time);
        home_time_gmt.setText(timeZoneMap.get(home_zone));
        TextView current_time_gmt = findViewById(R.id.cur_timezone_num);
        current_time_gmt.setText(timeZoneMap.get(cur_zone));

        //set up the warning for if time between 7am-11pm
        time_warning = findViewById(R.id.warning);
        //set converted time to the current time
        timeButton = findViewById(R.id.original_time_display);
        timeButton.setText(format_time(ori_time));
        conversion(); //perform initial conversion with current time and default timezone
        //set up the spinner for selecting timezone locations
        Spinner spinner = findViewById(R.id.timezone_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int position = adapter.getPosition(cur_zone);
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur_zone = parent.getItemAtPosition(position).toString();
                int tzPosition = adapter.getPosition(cur_zone);
                spinner.setSelection(tzPosition);
                ((TextView) parent.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
                ((TextView) parent.getSelectedView()).setTextSize(30);
                current_time_gmt.setText(timeZoneMap.get(cur_zone));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }

        });

        ImageButton convert = findViewById(R.id.convert_button);
        convert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                conversion();
            }
        });
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
        home_zone = mPrefs.getString("homeTimeZone", "America/Los_Angeles");
        //set home timezones
        TextView home_zone_label = findViewById(R.id.home_zone_text);
        home_zone_label.setText(home_zone);
        //set default GMT location time diff
        TextView home_time_gmt = findViewById(R.id.home_time);
        home_time_gmt.setText(timeZoneMap.get(home_zone));
        conversion();
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
                ori_time.set(Calendar.HOUR_OF_DAY, selectedHour);
                ori_time.set(Calendar.MINUTE, selectedMinute);
                //set up the original time button
                timeButton = findViewById(R.id.original_time_display);
                timeButton.setText(format_time(ori_time));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, ori_time.get(Calendar.HOUR_OF_DAY), ori_time.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}