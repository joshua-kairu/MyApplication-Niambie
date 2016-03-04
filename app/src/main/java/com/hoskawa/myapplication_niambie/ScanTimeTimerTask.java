package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.widget.Button;

import java.util.TimerTask;

/**
 * Created by user on 29-12-2015.
 */

// begin class ScanTimeTimerTask
// determines when to execute the scan
public class ScanTimeTimerTask extends TimerTask {

    /** CONSTANTS */

    /** VARIABLES */

    /** Activities */

    private Activity activity; // the activity that hold the scan button

    /** Buttons */

    private Button scanButton; // the scan button

    /** Primitives */

    private int countdownTime; // the count down

    /** CONSTRUCTORS */

    // begin constructor
    // params
    // activity - the holding activity that holds the scan button
    // theScanButton - the scan button
    public ScanTimeTimerTask( Activity activity, Button theScanButton ) {

        this.activity = activity;
        scanButton = theScanButton;

        // 1. disable the scan button
        // 2. set the scanning to start at 10

        // 1. disable the scan button

        activity.runOnUiThread( new Runnable() { public void run() { scanButton.setEnabled( false ); } } );

        // 2. set the scanning to start at 10

        setCountdownTime( 10 );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the countdown time
    public int getCountdownTime() { return countdownTime; }

    // setter for the countdown time
    public void setCountdownTime( int countdownTime ) { this.countdownTime = countdownTime; }

    /** Overrides */

    @Override
    // begin method run
    public void run() {

        // 1. show the current time on the scan button
        // 2. if the current time is zero
        // 2a. enable the scan button
        // 2b. show the appropriate message on the scan button
        // 2c. cancel the timer
        // 3. decrement the countdown

        // 1. show the current time on the scan button

        final String string = String.format( "%s %d %s", "Scan again in", getCountdownTime(), ( getCountdownTime() != 1 ) ? "seconds. . ." : "second. . ." );

        activity.runOnUiThread( new Runnable() { public void run() { scanButton.setText( string ); } } );

        // 2. if the current time is zero

        // begin if for if the current time is zero
        if( getCountdownTime() == 0 ) {

            // begin method activity.runOnUiThread
            activity.runOnUiThread( new Runnable() { public void run() {

                    // 2a. enable the scan button

                    scanButton.setEnabled( true );

                    // 2b. show the appropriate message on the scan button

                    scanButton.setText( R.string.mcwc_scan);

                } } ); // end method activity.runOnUiThread

            // 2c. cancel the timer

            cancel();

        } // end if for if the current time is zero

        // 3. decrement the countdown

        else { setCountdownTime( getCountdownTime() - 1 ); }

    } // end method run

    /** Other Methods */

} // end class ScanTimeTimerTask
