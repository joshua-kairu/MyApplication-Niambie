package com.hoskawa.myapplication_niambie;


/**
 * Created by user on 24-09-2015.
 */

// begin class SocketServerThread
// does the server side work in a different thread
public class SocketServerThread extends Thread {

    /** CONSTANTS */

    /** VARIABLES */

    // activities here should be servers
    /** Receive Call Activities */

    private ReceiveCallActivity currentActivity; // the current receive call activity

    /** CONSTRUCTORS */

    // begin constructor
    public SocketServerThread( String threadName, ReceiveCallActivity currentActivity ) {

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

        boolean setUpConnection = currentActivity.setUpConnection();

        HomeActivity.logError( this.getClass(), "started to set up connection" );
        while ( setUpConnection == false ) { HomeActivity.logError( this.getClass(), "waiting for set up connection" ); setUpConnection = currentActivity.setUpConnection(); }
        HomeActivity.logError( this.getClass(), "successfully finished to set up connection" );
        // get the streams
        currentActivity.getStreams();

//            // the sending of recorded sound should start now and
//            // send sound every second(1000ms)
//            callTimeTimer.schedule( sendRecordedSoundTimerTask, Calendar.getInstance().getTime(), 1000 );

        // process requests from the server
        currentActivity.processRequestsFromClient();

        // at this point no more requests are incoming therefore close connection
//        currentActivity.closeConnection();

    } // end run


    /** Other Methods */

} // end class SocketServerThread
