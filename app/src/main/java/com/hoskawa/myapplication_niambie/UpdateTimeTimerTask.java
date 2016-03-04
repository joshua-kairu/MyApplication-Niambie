package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.widget.TextView;

import com.hoskawa.time.TimeTaken;

import java.util.TimerTask;

/**
 * Created by user on 24-09-2015.
 */

// begin class UpdateTimeTimerTask
// updates the call time taken and
// displays it on the appropriate text view
public class UpdateTimeTimerTask extends TimerTask {

    /** CONSTANTS */

    /** VARIABLES */

    /** Activities */

    private Activity currentActivity; // the current activity

    /** Text Views */

    private TextView currentCallTimeTakenTextView; // the current call time taken text view

    /** Time Takens */

    private TimeTaken currentCallTimeTaken; // the current call time taken

    /** CONSTRUCTORS */

    // begin constructor
    public UpdateTimeTimerTask( Activity currentActivity, TextView currentCallTimeTakenTextView, TimeTaken currentCallTimeTaken) {

        this.currentActivity = currentActivity;
        this.currentCallTimeTakenTextView = currentCallTimeTakenTextView;
        this.currentCallTimeTaken = currentCallTimeTaken;

    } // end constructor


    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin run
    public void run() {

        // increase the current call time by a second
        currentCallTimeTaken.tickBy( 1000 );

        // get the universal time after the second tick
        final String currentTime = currentCallTimeTaken.toUniversalString();

        // begin method runOnUiThread
        currentActivity.runOnUiThread(

                // begin anonymous inner class Runnable
                new Runnable() {

                    @Override
                    // begin run
                    public void run() {

                        currentCallTimeTakenTextView.setText(currentTime);

                    } // end run

                } // end anonymous inner class Runnable

        ); // end method runOnUiThread

    } // end run

    /** Other Methods */

} // end class UpdateTimeTimerTask
