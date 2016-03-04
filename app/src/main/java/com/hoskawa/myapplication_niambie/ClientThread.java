package com.hoskawa.myapplication_niambie;

/**
 * Created by user on 03-10-2015.
 */

// begin class ClientThread
public class ClientThread extends Thread  {

    /** CONSTANTS */

    /** VARIABLES */

    // activities here should be clients
    /** Call In Session Client Activities */

    private CallInSessionClientActivity currentActivity; // the current call in session client activity

    /** CONSTRUCTORS */

    // begin constructor
    public ClientThread( String threadName, CallInSessionClientActivity currentActivity ) {

        super( threadName );
        this.currentActivity = currentActivity;

    } // end constructor


    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin run
    public void run() {

        // super.run();

        // set up connection
        currentActivity.setUpConnection();

        // get the streams
        currentActivity.getStreams();

        // the sending of recorded sound should start now and
        // send sound every second(1000ms)
        // TODO-me disabled automatic every-second sending of recorded sound to test if I can do it manually first. should be enabled post test
//            callTimeTimer.schedule( sendRecordedSoundTimerTask, Calendar.getInstance().getTime(), 1000 );

        // process requests from the server
        currentActivity.processRequestsFromServer();

        // at this point no more requests are incoming therefore close connection
        currentActivity.closeConnection();



    } // end run

    /** Other Methods */

} // end class ClientThread
