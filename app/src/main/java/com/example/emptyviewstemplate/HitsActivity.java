package com.example.emptyviewstemplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HitsActivity extends AppCompatActivity {

    private Integer hits;
    private TextView msg;
    private SharedPreferences myPrefs;


    private final View.OnClickListener bigListener = new View.OnClickListener()  {
        public void onClick(View v)  {
            // do stuff
            hits += 10;
            msg.setText(hits + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hits);

        Intent intent = getIntent();

        myPrefs = getApplicationContext().getSharedPreferences(String.valueOf(R.string.context_prefs), Context.MODE_PRIVATE);
        hits = myPrefs.getInt("hitsValue", -1);

        msg = (TextView) findViewById(R.id.msg);
        msg.setText(hits + "");

        Button little = (Button) findViewById(R.id.btn_sm);
        little.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {// Do something in response to button click 
                                          hits += 1;
                                          msg.setText(hits + "");
                                      }
                                  }
        );

        Button big = (Button) findViewById(R.id.btn_lg);
        big.setOnClickListener(bigListener);
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("hitsValue", hits);
        peditor.apply();   // TO SAVE CHANGES

        super.onPause();
    }
}
