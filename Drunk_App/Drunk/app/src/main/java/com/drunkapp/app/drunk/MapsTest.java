package com.drunkapp.app.drunk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.KeyEventCompat;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.Map;


public class MapsTest extends FragmentActivity implements OnMapReadyCallback {

    /////////////////////////////////

    private GoogleMap mMap;
    private short backPressed = 0;
    private int keyboardHeight;
    private LinearLayout view;
    private InputMethodManager keyboard;
    private FloatingActionButton fb;
    private Button send;
    private TextView  ctv;
    private EditText cet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        init();
        openWindowOnButtonClick();
    }

    private void init()
    {
        keyboard = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
        view = (LinearLayout) findViewById(R.id.view);
        fb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        send = (Button) findViewById(R.id.send);
        ctv = (TextView) findViewById(R.id.ChatText);
        cet = (EditText) findViewById(R.id.ChatText);
    }

    /**
     * Function which is used to avoid that the app is going in background if you tap back one time.
     * Now you have to tap two times.
     */
    @Override
    public void onBackPressed() {
        //counter to determine when the app should be set in background.

        /**
         * if the overlay is VISIBLE set it invisible
         */
        if(view.getVisibility() == View.VISIBLE)
        {
            view.setVisibility(View.INVISIBLE);
            ctv.setVisibility(View.INVISIBLE);
            send.setVisibility(View.INVISIBLE);
            return;
        }

        backPressed++;
        if(backPressed >= 2)
        {
            moveTaskToBack(true);
            backPressed = 0;
        }

        return;
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ke)
    {
        //Check wether the "keyboard-hide"-key is pressed. WRONG KEY!!!!!!!!!!
        if(keyCode == ke.KEYCODE_ASSIST)
        {
            view.setVisibility(View.INVISIBLE);
        }
        return true;
    } */

    /**
     * Get the permission to use the location by the user
     * only called if permission is not given yet
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // set the map in the fragment if the permission is given, otherwise ask for permission.
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
            //mMap.setMyLocationEnabled(true);
        }

    }

    /*
     * Function to open a new window which lays over the map and shows the chat.
     * The button moves in the right corner when tapped.
     * you can write your message in a text field.
     * Synchronisation with Database which holds the chat is called.
     * onBackClick the window closes and the button goes back to its old position.
     */
    public void openWindowOnButtonClick()
    {
        view.setAlpha((float)0.5);
        fb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // check if the Overlay should be visible. If this value is false, it is not shown -> show it.
                if(view.getVisibility() == View.INVISIBLE)
                {
                    view.setVisibility(View.VISIBLE);
                    showKeyboardAndReadInput(keyboard, send);
                    Log.d("Overlay", "Klick");
                }
                else if(view.getVisibility() == View.VISIBLE)
                {
                    view.setVisibility(View.INVISIBLE);
                    hideKeyboardAndStoreInput(keyboard, send);
                }

                /*if(keyboard.isActive() == false)
                {
                    view.setVisibility(View.VISIBLE);
                    keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }
                else if (keyboard.isActive() == true)
                {
                    view.setVisibility(View.INVISIBLE);
                    keyboard.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }*/
            }
        });

    }

    /**
     * function to show keyboard and store the input in a variable. Show the text in a TextView
     * Calls a send()-method if the send-button is pressed.
     * via send() the input is stored in the database on the server
     */
    private int showKeyboardAndReadInput(InputMethodManager kb, Button send)
    {
        kb.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        ctv.setVisibility(View.VISIBLE);
        send.setVisibility(View.VISIBLE);

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("Send", cet.getText().toString());
                cet.setText("");
                /**
                 * send the message to the db here
                 * show own message in the chatbox. Chatbox will be a spererated class
                 */
            }
        });
        checkKeyboardIsShown();
        return ReturnCodes.OK;
    }


    /**
     * function to hide keyboard and TextView
     * Stores the input in a variable -> is not deleted if the keyboard is hidden
     */
    private int hideKeyboardAndStoreInput(InputMethodManager kb, final Button send)
    {
        Log.d("Close Keyboard", "Keyboard cloesd");
        final View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                final Rect r = new Rect();

                rootView.getWindowVisibleDisplayFrame(r);
                getHeight(r.top - rootView.getRootView().getHeight());
            }
        });

        if(keyboardHeight < -1854)
        {
           /* Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(5000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ctv.setVisibility(View.INVISIBLE);
                                    send.setVisibility(View.INVISIBLE);
                                    keyboard.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent mainActivity = new Intent(getApplicationContext(),MapsTest.class);
                    startActivity(mainActivity);
                };
            };
            thread.start();*/
            ctv.setY((float) 10);
            send.setY((float) 10);
            return ReturnCodes.OK;
        }
        else if (keyboardHeight > 100)
        {
            return ReturnCodes.OK;
        }

        return ReturnCodes.UNKNOWN_ERR;
    }

    /**
     * needed by hideKeyboardAndStoreInput
     * returns keyboard height to detect if its shown.
     *
     *
     */
    private int getHeight(int height)
    {
        keyboardHeight = height;
        String s = Integer.toString(height);
        Log.d("Größe", s);
        return ReturnCodes.OK;
    }

    /**
     * funtion that is called every xy milliseconds.
     * check if keyboard is shown.
     */
    private int checkKeyboardIsShown()
    {
        Tick tick = new Tick(500);
        tick.setTick(new onTick() {
            @Override
            public void execute() {

                hideKeyboardAndStoreInput(keyboard, send);
            }
        });

        return ReturnCodes.OK;
    }

    /**
     * TODO: Button muss verschiebbar sein.                                                                                                                         (-)
     * TODO: Bei Tippen auf Button soll Fenster aufgehen mit Chatbox. Tastatur soll aufgehen und Eingabefeld soll erscheinen.                                       (+)
     * TODO: Fenster soll sich bei Tippen auf zurück oder Tippen in anderes Fenster (MepsTest) schließen => OnTouchListener implementieren in MapsTest              (+)
     * TODO: Außerdem OnTouchListener im Overlay, um im Chat scrollen zu können                                                                                     (-)
     * TODO: Eingabe geht an Datenbank, Synchronisation testen.                                                                                                     (-)
     * TODO: Alpha des StackViews setzen  (ist jetzt LinearLayout)                                                                                                  (+)
     * TODO: Alpha wird erst nach 3. Tippen auf Knopf gemacht?                                                                                                      (+)  BEHOBEN
     * TODO: Thread-Problem beim Einfahrend er Tastatur beheben, Erkennung beim Einfahren vorhanden                                                                 (-)
     * TODO: Aus den letzten beiden Funktionen eine InputHandler-Klasse machen                                                                                      (-)
     * TODO: Größe der Tastatur herausfinden und die Symbole anpassen                                                                                               (-)
     */
}
