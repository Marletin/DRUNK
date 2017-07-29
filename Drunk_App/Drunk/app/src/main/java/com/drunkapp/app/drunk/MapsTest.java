package com.drunkapp.app.drunk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;


public class MapsTest extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private short backPressed = 0;
    private TextureView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_maps_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
      mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        view = (TextureView) findViewById(R.id.textureView);
        //view.setAlpha((float) 0.5);
        openWindowOnButtonClick(view);
    }

    /**
     * Function which is used to avoid that the app is going in background if you tap back one time.
     * Now you have to tap two times.
     */
    @Override
    public void onBackPressed() {
        backPressed++;
        if(backPressed >= 2)
        {
            moveTaskToBack(true);
            backPressed = 0;
        }
        return;
    }

    /**
     * Get the permission to use the location by the user
     * only called if permission is not given yet
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap = googleMap;

            // only called when permission is given.
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            //Call a function to pop a dialog window and get permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            mMap.setMyLocationEnabled(true);
        }
    }

    /*
     * Function to open a new window which lays over the map and shows the chat.
     * The button moves in the right corner when tapped.
     * you can write your message in a text field.
     * Synchronisation with Database which holds the chat is called.
     * onBackClick the window closes and the button goes back to its old position.
     */
    public void openWindowOnButtonClick(final View view)
    {
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        final InputMethodManager keyboard = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
        fb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // check if the Overlay should be visible. If this value is false, it is not shown -> show it.
                if(view.getVisibility() == View.INVISIBLE)
                {
                    view.setVisibility(View.VISIBLE);
                    //view.bringToFront();
                    keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    Log.d("Overlay", "Klick");
                }
                else
                {
                    view.setVisibility(View.INVISIBLE);
                    keyboard.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

            }
        });
    }
    /**
     * TODO: Button muss verschiebbar sein.                                                                                                                         (-)
     * TODO: Bei Tippen auf Button soll Fenster aufgehen mit Chatbox. Tastatur soll aufgehen und Eingabefeld soll erscheinen.                                       (+)
        * TODO: Fenster soll sich bei Tippen auf zurück oder Tippen in anderes Fenster (MepsTest) schließen => OnTouchListener implementieren in MapsTest              (+)
        * TODO: Außerdem OnTouchListener im Overlay, um im Chat scrollen zu können                                                                                     (-)
        * TODO: Eingabe geht an Datenbank, Synchronisation testen.                                                                                                     (-)
        * TODO: Alpha des StackViews setzen  (ist jetzt TextureView)                                                                                                   (+)
        * TODO: Alpha wird erst nach 3. Tippen auf Knopf gemacht?                                                                                                      (-)
        * TODO: Beim Verstecken der Tastatur Overlay unsichtbar machen                                                                                                 (-)
        */
}
