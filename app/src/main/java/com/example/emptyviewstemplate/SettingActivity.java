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

        // get the shared timezone and hometown info in app
        Context context = getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        homeTown = mPrefs.getString("homeTown", "Baltimore");

        // initialize the EditText widget for the home town input
        EditText homeTownInput = findViewById(R.id.home_town_input);
        homeTownInput.setText(homeTown);

        // set the spinner
        Spinner spinner = findViewById(R.id.timezone_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homeTimeZone = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing0
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

            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
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

    /** Called when the user clicks the save button */
    public void setting(View view) {
        // Do something in response to button
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
