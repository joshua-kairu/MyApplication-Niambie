package com.hoskawa.myapplication_niambie;

/**
 * Created by joshua on 1/26/16.
 */

// begin class CountdownTimerThread
public class CountdownTimerThread extends Thread {

    /** CONSTANTS */

    /** VARIABLES */

    /** Countdown Timer Reached Zero Listeners */

    private CountdownTimerReachedZeroListener countdownListener; // the listener in which the countdown will be done

    /** Primitives */

    private int countdownTimeTaken; // the time the countdown should take

    /** CONSTRUCTORS */
    // begin constructor
    public CountdownTimerThread( CountdownTimerReachedZeroListener countdownListener, int countdownTimeTaken ) {

        setCountdownTimeTaken( countdownTimeTaken );

        this.countdownListener = countdownListener;

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the countdown time taken
    private int getCountdownTimeTaken() { return countdownTimeTaken; }

    // setter for the countdown time taken
    private void setCountdownTimeTaken( int countdownTimeTaken ) { this.countdownTimeTaken = countdownTimeTaken; }

    /** Overrides */

    @Override
    // begin method run
    public void run() {

        super.run();

        // 1. start counting down
        // 2. when the time is up, call the activity's on countdown timer reached zero

        // 1. start counting down

        long startTime = System.nanoTime(),
             currentTime = System.nanoTime(),
             difference = currentTime - startTime;

        // begin while to count the time
        while( difference < getCountdownTimeTaken() ) {

            currentTime = System.nanoTime();
            difference = currentTime - startTime;

        } // end while to count the time

        // 2. when the time is up, call the activity's on countdown timer reached zero

        countdownListener.onCountdownTimerReachedZero();

    } // end method run

    /** Other Methods */

} // end class CountdownTimerThread
