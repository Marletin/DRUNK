package com.drunkapp.app.drunk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    //static long appStarted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void startAct(View v)
    {
        Intent intent = new Intent(this, GMapsActivity.class);
        startActivity(intent);
        finish();
    }
}
