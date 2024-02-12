package com.example.emptyviewstemplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Integer numHits;
    private SharedPreferences myPrefs;
    SharedPreferences.Editor peditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* SharedPreferences at activity only level, not shared by others
        myPrefs = this.getPreferences(Activity.MODE_PRIVATE);
        */

        Context context = getApplicationContext(); // app level storage
        myPrefs = context.getSharedPreferences(String.valueOf(R.string.context_prefs), Context.MODE_PRIVATE);
        peditor = myPrefs.edit();
        peditor.putInt("hitsValue", -1);
        peditor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        numHits = myPrefs.getInt("hitsValue", 0);
        TextView hits = (TextView) findViewById(R.id.hits_value);
        hits.setText(numHits.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        peditor.putInt("hitsValue", numHits);
        peditor.apply();

        super.onPause();
    }

    @Override
    protected void onStop() {

        peditor.putInt("hitsValue", 10);
        peditor.apply();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // do stuff here
        super.onDestroy();
    }

    /** Called when the user clicks the HITME button */
    public void hitme(View view) {
        // Do something in response to button

        Intent intent = new Intent(MainActivity.this, HitsActivity.class);
        startActivity(intent);
    }

}