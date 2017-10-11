package com.drunkapp.app.drunk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Timer;
import java.util.TimerTask;

public class GMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static short backCt;
    private View floatingActionButton;
    private View send;
    private InputToggleManager itm;
    private InputMethodManager imm;
    private TabHost tabHost;
    private TabHost.TabSpec spec;
    private Timer backTimer;
    private EditText inputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(this);

        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // set the map in the fragment if the permission is given, otherwise ask for permission.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            // only called when permission is given.
            googleMap.setMyLocationEnabled(true);
        }
        else
        {
            //Call a function to pop a dialog window and get permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onBackPressed()
    {
        tabHost.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().findFragmentById(R.id.map_frag).getView().setAlpha((float)1);
        send.setVisibility(View.INVISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
        inputMessage.setVisibility(View.INVISIBLE);

        // count up Counter to check if the app should be set in background
        ++backCt;
        if(backCt >= 2)
        {
            moveTaskToBack(true);
            backCt = 0;
        }

        //set timer to detect if back is pressed in a short interval. If not, set counter back to 0.
        backTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                backCt = 0;
            }
        }, 500);
    }

    private void init()
    {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        send = findViewById(R.id.send);
        itm = new InputToggleManager();
        imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
        backTimer = new Timer();
        inputMessage = (EditText) findViewById(R.id.inputMessage);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        // Init Tab one
        spec = tabHost.newTabSpec("public_chat").setContent(R.id.public_chat).setIndicator("Public Chat");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("private_Chat").setContent(R.id.tab2).setIndicator("Private Chat");
        tabHost.addTab(spec);



        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        // Listener for the floatingActionButton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itm.toggleInputShown(imm);
                tabHost.setVisibility(View.VISIBLE);
                getSupportFragmentManager().findFragmentById(R.id.map_frag).getView().setAlpha((float)0.5);
                inputMessage.setVisibility(View.VISIBLE);
                inputMessage.bringToFront();
                floatingActionButton.setVisibility(View.INVISIBLE);
                send.setVisibility(View.VISIBLE);
                send.setY(inputMessage.getY());
            }
        });

        // Listener for tab changing
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            switch(tabId)
            {
                case "public_chat": Log.d("Chat", "chosen");

                // call funciton which synchronize the public/private database with the phone
            }
            }
        });

        //Listener for send-button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call funtion which sends the string via internet into the database
            }
        });

    }

}

/**
 * TODO: Funktionen, die mit DB synchen schreiben                                           -> externe Klasse                           (-)
 * TODO: Funktion zum senden in richtige DB schreiben (oder auch zwei Funktionnen?)         -> externe Klasse                           (-)
 * TODO: Aufberietung der Daten in einem Chat                                               -> (größten)teils externe Klasse            (-)
 * TODO: LinearLayouts in TabHost durch ScrollLayouts ersetzen                              -> scrollen durch Verlauf                   (-)
 */
