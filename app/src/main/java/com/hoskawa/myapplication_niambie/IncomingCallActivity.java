package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// begin activity IncomingCallActivity
public class IncomingCallActivity extends Activity implements CountdownTimerReachedZeroListener {

    /** VARIABLES */

    /** Buttons */

    private Button acceptButton, // button to accept the call
                   rejectButton; // button to reject the call

    /** Countdown Timer Threads */

    private CountdownTimerThread countdownTimerThread; // the counter down for the timer he he

    /** Object I/O Streams */

    private ObjectInputStream localObjectInputStream; // local stream to read objects sent to the server

    private ObjectOutputStream localObjectOutputStream; // local stream to send objects from the server

    /** Sockets */

    private Socket localSocket; // the local socket for this activity

    /** Strings */

    private String callerName; // the name of the caller

    /** Text Views */

    private TextView callerNameTextView; // text view for the caller's name
//                     callStatusTextView; // text view for the call status

    /** Vibrators */

    private Vibrator vibrator; // a vibrator

    /** METHODS */

    /** Getters And Setters */

    // begin getter for the caller's name
    private String getCallerName() { return callerName; }

    // begin setter for the caller's name
    private void setCallerName( String callerName ) { this.callerName = callerName; }

    /** Overrides */

    @Override
    // begin onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_incoming_call );

        // 0. get bundle details - the name of the caller
        // 1. initialize things
        // 1a. initialize UI
        // 1b. initialize local socket
        // 1c. get relevant streams
        // 2. process requests from the client while we wait
        // 2a. when CALLING_YOU is received
        // 2a1. show the user that they are being called
        // 2a2. (vibrate the phone)
        // 3. start countdown to 30 seconds - after which the activity terminates
        // 3a. accept button clicked
        // 3a1. send PICKED_UP
        // 3a2. pass necessary bundle things - caller name, parent activity
        // 3a3. start the Call In Session Activity
        // 3a4. destroy this activity
        // 3b. reject button clicked
        // 3b1. send REJECTED
        // 3b2. destroy this activity
        // 4. after 30 seconds
        // 4a. send TIMED_OUT
        // 4b. destroy this activity

        // 0. get bundle details - the name of the caller

        getIntent().getExtras().getString(HomeActivity.CALLER_NAME);

        // 1. initialize things

        // 1a. initialize UI

        initializeUI();

        // 1b. initialize local socket

        setUpConnection();

        // 1c. get relevant streams

        // begin new Thread
//        new Thread(

            // begin new Runnable
//            new Runnable() {
//
//                @Override
//                // begin run
//                public void run() {

                    getStreams();

//                } // end run

//            } // end new Runnable

//        ).start(); // end new Thread

        // 2. show the user that they are being called
        // 2a. (vibrate the phone)

        // 3. start countdown to 30 seconds - after which the activity terminates

        // wait for the streams to be operational

        while ( localObjectInputStream == null ) {
            HomeActivity.logError( IncomingCallActivity.class, "waiting for the streams to be operational" ); }

//        // begin countDownTimer
//        CountDownTimer countDownTimer = new CountDownTimer( HomeActivity.CALL_TIMEOUT, 1000 ) {
//            @Override
//            // begin onTick
//            public void onTick( long millisUntilFinished ) {
//
//                // if any thing is in the pipeline during this time, cancel the timer
//
//                // begin try to try see if there is anything in the pipeline
//                try {
//
//                    int availableBytes = localObjectInputStream.available();
//
//                    // since we can expect something in the pipeline, then the bytes in the pipeline should be greater than zero
//                    // once we prove that the bytes in the pipeline are more than zero, we stop counting down and continue doing stuff
//                    if( availableBytes > 0 ) { cancel(); continueAfterAvailabilityConfirmation();  }
//
//                } // end try to try see if there is anything in the pipeline
//
//                // catch I/O issues
//                catch ( IOException ioException ) { HomeActivity.logError( IncomingCallActivity.class.getClass(), ioException.getMessage() ); }
//
//            } // end onTick
//
//            @Override
//            // method onFinish
//            // at this time nothing has been found in the pipeline so finish this activity
//            public void onFinish() { finish(); }
//
//        }.start(); // end countDownTimer


        // 3a and 3b are implemented in the click listeners of each respective button

        // 3c. process requests from the client while we wait -> done in continueAfterAvailabilityConfirmation

//        processRequestsFromClient();

        // 4. is done in the onCountdownTimerReachedZero method

    } // end onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_incoming_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // begin method onDestroy
    protected void onDestroy() {

        super.onDestroy();

        // 1. close connections
        // 2. stop the vibrations!
        // 3. destroy the countdown timer

        // 1. close connections

//        closeConnection();

        // 2. stop the vibrations!

//        vibrator.cancel();

        // 3. destroy the countdown timer

//        if( countdownTimerThread != null ) { countdownTimerThread = null; }

    } // end method onDestroy

    /** Other Methods */

    // begin method initializeUI
    private void initializeUI() {

        callerNameTextView = ( TextView ) findViewById( R.id.ic_tv_caller_name );

        callerNameTextView.setText( getCallerName() );

//        callStatusTextView = ( TextView ) findViewById( R.id.ic_tv_call_status );

        acceptButton = ( Button ) findViewById( R.id.ic_b_accept );

        // begin method acceptButton.setOnClickListener
        acceptButton.setOnClickListener(

            // begin View.OnClickListener
            new View.OnClickListener() {

                @Override
                // begin method onClick
                public void onClick( View v ) {

                    // 3a. accept button clicked
                    // 3a1. send PICKED_UP
                    // 3a2. pass necessary bundle things - caller name, parent activity
                    // 3a3. start the Call In Session Activity
                    // 3a4. destroy this activity

                    // 3a. accept button clicked

                    // 3a1. send PICKED_UP

                    sendDataToClient( HomeActivity.PICKED_UP );

                    // 3a2. pass necessary bundle things - caller name

                    Intent i = new Intent( IncomingCallActivity.this, CallInSessionActivity.class );
                    i.putExtra( HomeActivity.CALLER_NAME, getCallerName() );
                    i.putExtra( HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY, HomeActivity.INCOMING_CALL_ACTIVITY );

                    // 3a3. start the Call In Session Activity

                    startActivity( i );

                    // 3a4. destroy this activity

                    finish();

                } // end method onClick

            } // end View.OnClickListener

        ); // end method acceptButton.setOnClickListener

        rejectButton = ( Button ) findViewById( R.id.ic_b_reject );

        // begin method rejectButton.setOnClickListener
        rejectButton.setOnClickListener(

            // begin View.OnClickListener
            new View.OnClickListener() {

                @Override
                // begin method onClick
                public void onClick( View v ) {

                    // 3b. reject button clicked
                    // 3b1. send REJECTED
                    // 3b2. destroy this activity

                    // 3b. reject button clicked

                    // 3b1. send REJECTED

                    sendDataToClient( HomeActivity.REJECTED );

                    // 3b2. destroy this activity

                    finish();

                } // end method onClick

            } // end View.OnClickListener

        ); // end method rejectButton.setOnClickListener

    } // end method initializeUI

    // begin method setUpConnection
    public boolean setUpConnection() {

        // 1. get the socket from the receiving call activity

        // 1. get the socket from the receiving call activity
        HomeActivity.logError( this.getClass(), ReceiveCallActivity.globalServerSideSocket.toString() );
        localSocket = ReceiveCallActivity.globalServerSideSocket;

        return true;

    } // end method setUpConnection

    // begin method getStreams
    public boolean getStreams() {

        // 1. null initialize the object streams
        // 2. try to set up the streams
        // 3. catch any I/O issues

        // 1. null initialize the object streams

        localObjectInputStream = null;
        localObjectOutputStream = null;

        // 2. try to set up the streams

        // begin try to try set up the streams
//        try {

            localObjectOutputStream = ReceiveCallActivity.globalServerSideObjectOutputStream;

            localObjectInputStream = ReceiveCallActivity.globalServerSideObjectInputStream;

            return true;

//        } // end try to try set up the streams

        // 3. catch any I/O issues

//        catch ( IOException e ) { HomeActivity.logError( IncomingCallActivity.class, e.getMessage() ); return false; }

    } // end method getStreams

    // begin method closeConnection
    public boolean closeConnection() {

        // 1. close the output stream
        // 2. close the input stream
        // 3. close the local socket

        // begin try to try close the connection
        try {

            // 1. close the output stream

            if( localObjectOutputStream != null ) { localObjectOutputStream.close(); }

            // 2. close the input stream

            if( localObjectInputStream != null ) { localObjectInputStream.close(); }

            // 3. close the local socket

            if( localSocket != null ) { localSocket.close(); }

            return true;

        } // end try to try close the connection

        // catch I/O issues
        catch ( IOException e ) { HomeActivity.logError( IncomingCallActivity.class, e.getMessage() ); return false; }

    } // end method closeConnection

    // begin method sendDataToClient
    private void sendDataToClient( Object data ) {

        // 1. try to send data to the client(the caller)
        // 2. catch any I/O issues

        // 1. try to send data to the client(the caller)

        // begin try to try send data to the client
        try {

            localObjectOutputStream.writeObject(data);
            localObjectOutputStream.flush();
            HomeActivity.logError( IncomingCallActivity.class, "just did sendDataToClient( " + data + " )" );

        } // end try to try send data to the client

        // 2. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( IncomingCallActivity.class, e.getMessage() ); }

    } // end method sendDataToClient

    // begin method processRequestsFromClient
    private void processRequestsFromClient() {

        String inputRequest = null;

        // begin try to try get the input request from the client and work on it
        try {

            // 2. process requests from the client while we wait
            // 2a. when CALLING_YOU is received
            // 2a1. show the user that they are being called
            // 2a2. (vibrate the phone)

                inputRequest = ( String ) localObjectInputStream.readObject();

                // begin switch to determine how to respond
                switch ( inputRequest ) {

                    // 2. process requests from the client while we wait

                    // 2a. when CALLING_YOU is received

                    // case a CALLING_YOU
                    case HomeActivity.CALLING_YOU:

                        // 2a1. show the user that they are being called

                        // 2a2. (vibrate the phone)

                        vibrator = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );

                        long[] vibrationTimes = { HomeActivity.VIBRATE_OFF_TIME, HomeActivity.VIBRATE_ON_TIME };

                        // vaibret based on the vibration times, and start repeating vibration at the time at index 1 of the vibration times array
                        vibrator.vibrate( vibrationTimes, 1 );

                        break;

                } // end switch to determine how to respond

            } // end try to try get the input request from the client and work on it

            // catch I/O issues
            catch ( ClassNotFoundException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }
            catch ( IOException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }

    } // end method processRequestsFromClient

    @Override
    // begin method onCountdownTimerReachedZero
    public void onCountdownTimerReachedZero() {

        // 4. after 30 seconds
        // 4a. send TIMED_OUT
        // 4b. destroy this activity

        // 4. after 30 seconds

        // 4a. send TIMED_OUT

        sendDataToClient( HomeActivity.TIMED_OUT );

        // 4b. destroy this activity

        finish();

    } // end method onCountdownTimerReachedZero

    // begin method continueAfterAvailabilityConfirmation
    private void continueAfterAvailabilityConfirmation() {

        processRequestsFromClient();

    } // end method continueAfterAvailabilityConfirmation

} // end activity IncomingCallActivity
