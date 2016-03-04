package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoskawa.networking.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// begin activity CallingActivity
// implements what happens when a user starts a call
public class CallingActivity extends Activity implements Client, CountdownTimerReachedZeroListener {

    /** VARIABLES */

    /** Bitmap Drawables */

    private BitmapDrawable calledPictureBitmap; // bitmap of called

    /** Buttons */

    private Button speakerButton, // button to toggle the speaker phone
                   muteButton, // button to toggle the mute status
                   endCallButton; // button to end the call

    /** Countdown Timer Threads */

    private CountdownTimerThread countdownTimerThread; // // the counter down for the timer he he

    //    /** Image Views */
//
    private ImageView calledPictureBitmapImageView; // image view for the person being called

    /** Media Players */

    private MediaPlayer mediaPlayer; // player for the audio

    /** Object I/O Streams */

    private ObjectInputStream localObjectInputStream; // local stream to read objects sent to the server

    private ObjectOutputStream localObjectOutputStream; // local stream to send objects from the server

    /** Primitives */

    private int callStatusStringID; // variable to hold the R.ID of calling status string

    /** Relative Layouts */

    private RelativeLayout callerNameAndStatusRelativeLayout; // relative layout for the caller's name and status

    /** Sockets */

    private Socket localSocket; // localSocket to connect to the called's server localSocket

    /** Strings */

    private String calledName, // name of person being called
                   calledPicPath, // path of the called person's picture
                   muteStatus, // status for mute
                   speakerStatus, // status for speaker
                   callerName; // name of the caller

    /** Text Views */

    private TextView calledNameTextView, // text view to display the name of the called person
                     callingStatusTextView; // text view to display the status of the call

    /** METHODS */

    /** Getters and Setters */

    // getter for the speaker phone status
    public String getSpeakerStatus() { return speakerStatus; }

    // setter for the speaker phone status
    public void setSpeakerStatus( String speakerStatus ) { this.speakerStatus = speakerStatus; }

    // getter for the mute status
    public String getMuteStatus() { return muteStatus; }

    // setter for the mute status
    public void setMuteStatus( String muteStatus ) { this.muteStatus = muteStatus; }

    // getter for the call status string ID
    private int getCallStatusStringID() { return callStatusStringID; }

    // begin setter for the call status string ID
    private void setCallStatusStringID( String callStatus ) {

        // if we are connecting, then the call status ID should point to the "connecting" string
        // else we are ringing so the call status ID should point to the "ringing" string

        if( callStatus.equals(HomeActivity.CONNECTING ) == true ) { callStatusStringID = R.string.c_connecting; }

        else { callStatusStringID = R.string.c_ringing; }

    } // end setter for the call status string ID

    // getter for the caller's name
    private String getCallerName() { return callerName; }

    // setter for the caller's name
    private void setCallerName( String callerName ) { this.callerName = callerName; }

    /** Overrides */

    @Override
    // begin method onCreate
    // Bundle - name of person being called
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_calling );

        // start off with the speaker off and the mute off
        setSpeakerStatus( HomeActivity.SPEAKER_OFF );
        setMuteStatus( HomeActivity.MUTE_OFF );

        // 1. get things from the bundle - the name of the one being called
        // 2. initialize the UI
        // 2a. show the UI for at least some time
        // 3. attempt to connect to the hotspot using the hardcoded IP address
        // 3a. initialize the client localSocket
        // 3a1. if the server does not have the hard coded IP address
        // 3a1a. tell the user the called is not a phone
        // 3a1b. destroy this activity
        // 3b. get the relevant streams
        // 3c. check if the hotspot operates this app
        // 3c1. send the hotspot a NIAMBIE tag
        // 3c2. start a countdown timer starting NIAMBIE_COUNTDOWN
        // 3c3. if the hotspot returns a NIAMBIE tag before the countdown ends, continue
        // 3c4. when the countdown ends
        // 3c4a. tell the user the called does not have Niambie
        // 3c4b. destroy this activity
        // 4. discover the hotspot
        // 4a. send DISCOVER to the hotspot
        // 4b. send the caller's name
        // 4c. set the status TextView to show that we are connecting
        // 4d. stop counting down
        // 5. ring up the called
        // 5a. wait for an OFFER from the hotspot
        // 5b. respond with a CALLING_YOU
        // 5c. set the status TextView to show that the called's phone is ringing
        // 6. wait for a pick up
        // 6a. wait for the called to PICKED_UP
        // 6b. pass the necessary bundle things - name of called, parent activity
        // 6c. start the Call In Session activity
        // 6d. destroy this activity
        // 7. when REJECTED
        // 7a. show the caller that the called is busy
        // 7a1. get the width and height of the profile image view
        // 7a2. replace the profile image view with the busy image
        // 7a3. set the color of the top text relative layout background to red
        // 7a4. set the calling status to Busy
        // 7a5. disable all buttons
        // 7a6. show for five seconds
        // 7b. end call
        // 7b1. destroy this activity
        // 8. when TIMED_OUT
        // 8a1. get the width and height of the profile image view
        // 8a2. replace the profile image view with the timed out image
        // 8a3. set the color of the top text relative layout background to red
        // 8a4. set the calling status to Timed Out
        // 8a5. disable all buttons
        // 8a6. show for five seconds
        // 8b. end call
        // 8b1. destroy this activity

        // 1. get things from the bundle - the name of the one being called

        calledName = getIntent().getExtras().getString( HomeActivity.CALLED_NAME );
        setCallerName(getIntent().getExtras().getString(HomeActivity.CALLER_NAME));

        // 2. initialize the UI

        initializeUI();

        // 2a. show the UI for at least some time

        // 3 - 8 are done in continueAfterCountdown

        // begin new CountDownTimer
        new CountDownTimer( HomeActivity.MINIMUM_UI_DISPLAY_TIME, 1000 ) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("afbebtrbs", "onTick: " + millisUntilFinished ); }

            @Override
            // method onFinish
            // handles what happens after the countdown is done
            public void onFinish() {
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {

                                continueAfterCountdown();

                            }

                       }
                );
            }

        }.start(); // end new CountDownTimer

    } // end method onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calling, menu);
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

        // 1. close the socket connection
        // 2. stop counting down

        // 1. close the socket connection

//        closeConnection();

        // 2. stop counting down

        if( countdownTimerThread != null ) { countdownTimerThread = null; }

    } // end method onDestroy

    @Override
    // begin method setUpConnection
    public boolean setUpConnection() {

        // 1. get the socket from the making call activity

        // 1. get the socket from the making call activity

        localSocket = MakeCallActivity.globalClientSideSocket;

        // 3a1. if the server does not have the hard coded IP address

        // if the server does not have the hard coded IP address then
        // the making call activity will have a null global socket and so
        // the local socket here will also be null

        // begin if for if the local socket is null
        if ( localSocket == null ) {

            Button[] buttons = { speakerButton, muteButton, endCallButton };

            // 3a1a. tell the user the called is not a phone
            // 3a1b. destroy this activity

            HomeActivity.displayErrorsDuringCalls( this, calledPictureBitmapImageView, getResources().getDrawable( R.drawable.not_a_phone ),
                                                   callerNameAndStatusRelativeLayout, String.valueOf( getText( R.string.not_a_phone ) ), callingStatusTextView, buttons );

//            finish();

            return false;

        } // end if for if the local socket is null

        return true;

    } // end method setUpConnection

    @Override
    // begin method getStreams
    public boolean getStreams() {

        // 1. null initialize the object streams
        // 2. try to set up the streams
        // 3. catch any I/O issues

        // 1. null initialize the object streams
        HomeActivity.logError( CallingActivity.class, "before null initialize" );
        MakeCallActivity.globalClientSideObjectInputStream = null;
        MakeCallActivity.globalClientSideObjectOutputStream = null;
        HomeActivity.logError( CallingActivity.class, "after null initialize" );
        // 2. try to set up the streams

        // begin try to try set up the streams
        try {
            HomeActivity.logError( CallingActivity.class, "before MakeCallActivity.globalClientSideObjectOutputStream = new ObjectOutputStream( MakeCallActivity.globalClientSideSocket.getOutputStream() )" );
            MakeCallActivity.globalClientSideObjectOutputStream = new ObjectOutputStream( MakeCallActivity.globalClientSideSocket.getOutputStream() );
            HomeActivity.logError( CallingActivity.class, "after MakeCallActivity.globalClientSideObjectOutputStream = new ObjectOutputStream( MakeCallActivity.globalClientSideSocket.getOutputStream() )" );

            HomeActivity.logError( CallingActivity.class, "before MakeCallActivity.globalClientSideObjectOutputStream.flush()" );
            // flush the output stream to send the header information
            MakeCallActivity.globalClientSideObjectOutputStream.flush();
            HomeActivity.logError(CallingActivity.class, "after MakeCallActivity.globalClientSideObjectOutputStream.flush()");

            HomeActivity.logError( CallingActivity.class, "before MakeCallActivity.globalClientSideObjectInputStream = new ObjectInputStream(MakeCallActivity.globalClientSideSocket.getInputStream() )" );
            MakeCallActivity.globalClientSideObjectInputStream = new ObjectInputStream(MakeCallActivity.globalClientSideSocket.getInputStream() );
            HomeActivity.logError( CallingActivity.class, "after MakeCallActivity.globalClientSideObjectInputStream = new ObjectInputStream(MakeCallActivity.globalClientSideSocket.getInputStream() )" );

            localObjectOutputStream = MakeCallActivity.globalClientSideObjectOutputStream;
            localObjectInputStream = MakeCallActivity.globalClientSideObjectInputStream;
            return true;

        } // end try to try set up the streams

        // 3. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( CallingActivity.class, e.getMessage() ); return false; }

    } // end method getStreams

    @Override
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
        catch ( IOException e ) { HomeActivity.logError( CallingActivity.class, e.getMessage() ); return false; }

    } // end method closeConnection

    @Override
    public boolean isTerminatingItself() { return false; }

    @Override
    public void setTerminatingItself( boolean b ) {}

    @Override
    // begin method sendDataToServer
    public boolean sendDataToServer( Object data ) {

        // 1. try to send data to the server(the called)
        // 2. catch any I/O issues

        // 1. try to send data to the server(the called)

        // begin try to try send data to the server
        try {

            localObjectOutputStream.writeObject(data);
            localObjectOutputStream.flush();
            HomeActivity.logError(CallingActivity.class, "just did sendDataToServer( " + data.toString() + " )");
            return true;

        } // end try to try send data to the server

        // 2. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( CallingActivity.class, e.getMessage() ); return false; }

    } // end method sendDataToServer

    @Override
    // begin method processRequestsFromServer
    public boolean processRequestsFromServer() {

        String inputRequest = null;

        Button buttons[] = { speakerButton, muteButton, endCallButton };

        // begin do while to process requests from the server
        do {

            // begin try to try get the input request from the server and work on it
            try {

                // 5. ring up the called
                // 5a. wait for an OFFER from the hotspot
                // 5b. respond with a CALLING_YOU
                // 5c. set the status TextView to show that the called's phone is ringing
                // 6. wait for a pick up
                // 6a. wait for the called to PICK_UP
                // 6b. pass the necessary bundle things - name of called
                // 6c. start the Call In Session activity
                // 6d. destroy this activity

                inputRequest = ( String ) localObjectInputStream.readObject();

                HomeActivity.logError( this.getClass(), "inputRequest = " + inputRequest );

                // begin switch to determine how to respond
                switch ( inputRequest ) {

                    // 5. ring up the called

                    // 5a. wait for an OFFER from the hotspot

                    // case an offer is received
                    case HomeActivity.OFFER:

                        // 5b. respond with a CALLING_YOU

                        // tell the called that they are being called

                        sendDataToServer( HomeActivity.CALLING_YOU );

                        // 5c. set the status TextView to show that the called's phone is ringing

                        setCallStatusStringID( HomeActivity.RINGING );
                        callingStatusTextView.setText( getCallStatusStringID() );

                        break;

                    // 6. wait for a pick up

                    // 6a. wait for the called to PICKED_UP

                    // case the user picks up
                    case HomeActivity.PICKED_UP:

                        // 6b. pass the necessary bundle things - name of called, parent activity

                        HomeActivity.logError( CallingActivity.class, "before Intent i = new Intent( this, CallInSessionActivity.class );" );
                        Intent i = new Intent( this, CallInSessionActivity.class );
                        HomeActivity.logError( CallingActivity.class, "after Intent i = new Intent( this, CallInSessionActivity.class );" );

                        HomeActivity.logError(CallingActivity.class, "before i.putExtra( HomeActivity.CALLED_NAME, calledName );");
                        i.putExtra(HomeActivity.CALLED_NAME, calledName);
                        HomeActivity.logError(CallingActivity.class, "after i.putExtra( HomeActivity.CALLED_NAME, calledName );");

                        HomeActivity.logError(CallingActivity.class, "before i.putExtra( HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY, HomeActivity.CALLING_ACTIVITY );");
                        i.putExtra(HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY, HomeActivity.CALLING_ACTIVITY);
                        HomeActivity.logError(CallingActivity.class, "after i.putExtra( HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY, HomeActivity.CALLING_ACTIVITY );");

                        // 6c. start the Call In Session activity

                        HomeActivity.logError(CallingActivity.class, "before startActivity(i);");
//                        i.setFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
                        startActivity(i);
                        HomeActivity.logError(CallingActivity.class, "after startActivity(i);");

                        // 6d. destroy this activity

                        HomeActivity.logError( CallingActivity.class, "before finish();" );
                        finish();
                        HomeActivity.logError(CallingActivity.class, "after finish();");

                        inputRequest = HomeActivity.TERMINATE;

                        break;

                    // 7. when REJECTED

                    // case when the user rejects the call
                    case HomeActivity.REJECTED:

                        // 7a and 7b are done in the display errors method

                        HomeActivity.displayErrorsDuringCalls( this, calledPictureBitmapImageView, getResources().getDrawable( R.drawable.busy ),
                                                               callerNameAndStatusRelativeLayout, String.valueOf( getText( R.string.busy ) ),
                                                               callingStatusTextView, buttons );

                        break;

                    // 8. when TIMED_OUT

                    // case when the call is timed out
                    case HomeActivity.TIMED_OUT:

                        // 8a and 8b are done in the display errors method

                        HomeActivity.displayErrorsDuringCalls( this, calledPictureBitmapImageView, getResources().getDrawable( R.drawable.timed_out ),
                                                               callerNameAndStatusRelativeLayout, String.valueOf( getText( R.string.timed_out ) ),
                                                               callingStatusTextView, buttons );

                        break;

                } // end switch to determine how to respond

            } // end try to try get the input request from the server and work on it

            // catch I/O issues
            catch ( ClassNotFoundException e ) { HomeActivity.logError( CallingActivity.class, e.getMessage() ); }
            catch ( IOException e ) {
//                HomeActivity.logError( CallingActivity.class, e.getMessage() );
                e.printStackTrace();
            }

        } while( inputRequest.equals( HomeActivity.TERMINATE ) == false ); // end do while to process requests from the server

        return true;

    } // end method processRequestsFromServer

    /** Other Methods */

    // begin method initializeUI
    private void initializeUI() {

        calledNameTextView = ( TextView ) findViewById( R.id.c_tv_name_of_called );
        calledNameTextView.setText( calledName );

        callerNameAndStatusRelativeLayout = ( RelativeLayout ) findViewById( R.id.c_rl_textviews );

        calledPictureBitmapImageView = ( ImageView ) findViewById( R.id.c_iv_caller_picture );

        callingStatusTextView = ( TextView ) findViewById( R.id.c_tv_status );

        speakerButton = ( Button ) findViewById(  R.id.c_b_speaker );

        // begin method speakerButton.setOnClickListener
        speakerButton.setOnClickListener(

                // begin View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {

                        // begin switch to determine what to do
                        switch ( getSpeakerStatus() ) {

                            // case for when the speaker phone status is off
                            case HomeActivity.SPEAKER_OFF:

                                // 1. switch on the speaker
                                // 2. change the speaker's state to on
                                // 3. change the text on the speaker button to tell the user that the speaker is on

                                // 1. switch on the speaker

                                HomeActivity.changeSpeakerPhoneState( CallingActivity.this, HomeActivity.SPEAKER_ON );

                                // 2. change the speaker's state to on

                                setSpeakerStatus( HomeActivity.SPEAKER_ON );

                                // 3. change the text on the speaker button to tell the user that the speaker is on

                                speakerButton.setText( R.string.speaker_on );

                                break;

                            // case for when the speaker phone status is on
                            case HomeActivity.SPEAKER_ON:

                                // 1. switch off the speaker
                                // 2. change the speaker's state to off
                                // 3. change the text on the speaker button to tell the user that the speaker is off

                                // 1. switch off the speaker

                                HomeActivity.changeSpeakerPhoneState( CallingActivity.this, HomeActivity.SPEAKER_OFF );

                                // 2. change the speaker's state to off

                                setSpeakerStatus( HomeActivity.SPEAKER_OFF );

                                // 3. change the text on the speaker button to tell the user that the speaker is off

                                speakerButton.setText( R.string.speaker_off );

                                break;

                        } // end switch to determine what to do

                    } // end method onClick

                } // end View.OnClickListener

        ); // end method speakerButton.setOnClickListener

        muteButton = ( Button ) findViewById( R.id.c_b_mute );

        // begin method muteButton.setOnClickListener
        muteButton.setOnClickListener(

                // begin View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {

                        // begin switch to determine what to do
                        switch( getMuteStatus() ) {

                            // case for when the mute status is off
                            case HomeActivity.MUTE_OFF:

                                // 1. switch on the mute
                                // 2. change the mute's state to on
                                // 3. change the text on the mute button to tell the user that the mute is on

                                // 1. switch on the mute

                                HomeActivity.changeMuteState( CallingActivity.this, HomeActivity.MUTE_ON );

                                // 2. change the mute's state to on

                                setMuteStatus(HomeActivity.MUTE_ON);

                                // 3. change the text on the mute button to tell the user that the mute is on

                                muteButton.setText( R.string.mute_on );

                                break;

                            // case for when the mute status is on
                            case HomeActivity.MUTE_ON:

                                // 1. switch off the mute
                                // 2. change the mute's state to off
                                // 3. change the text on the mute button to tell the user that the mute is off

                                // 1. switch off the mute

                                HomeActivity.changeMuteState( CallingActivity.this, HomeActivity.MUTE_OFF );

                                // 2. change the mute's state to off

                                setMuteStatus( HomeActivity.MUTE_OFF );

                                // 3. change the text on the mute button to tell the user that the mute is off

                                muteButton.setText( R.string.mute_off );

                                break;

                        } // end switch to determine what to do

                    } // end method onClick

                } // end View.OnClickListener

        ); // end method muteButton.setOnClickListener

        endCallButton = ( Button ) findViewById( R.id.c_b_end_call );

        // begin method endCallButton.setOnClickListener
        endCallButton.setOnClickListener(

                // begin View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {

                        // 1. finish this call by finishing this activity

                        // 1. finish this call by finishing this activity

                        finish();

                    } // end method onClick

                } // end View.OnClickListener

        ); // end method endCallButton.setOnClickListener

    } // end method initializeUI

    @Override
    // begin method onCountdownTimerReachedZero
    public void onCountdownTimerReachedZero() {

        // 3c4. when the countdown ends
        // 3c4a. tell the user the called does not have Niambie
        // 3c4b. destroy this activity

        // 3c4. when the countdown ends

        // 3c4a. tell the user the called does not have Niambie
        // 3c4b. destroy this activity

        Button buttons[] = { speakerButton, muteButton,endCallButton };

        HomeActivity.displayErrorsDuringCalls( this, calledPictureBitmapImageView, getResources().getDrawable( R.drawable.not_niambie ),
                                               callerNameAndStatusRelativeLayout, String.valueOf( getText( R.string.not_niambie ) ), callingStatusTextView, buttons );

    } // end method onCountdownTimerReachedZero

    // begin method continueAfterCountdown
    // code to continue processing after countdown
    private void continueAfterCountdown() {

        // 3. attempt to connect to the hotspot using the hardcoded IP address

        // 3a. initialize the client localSocket

        // 3a1a and 3a1b are also done in the following method

        boolean connectionSetUp = setUpConnection();

        HomeActivity.logError( CallingActivity.class, "connectionSetUp " + connectionSetUp );

        // begin if for if the connection is set up
        if ( connectionSetUp == true ) {

            // 3b. get the relevant streams

            getStreams();

            HomeActivity.logError(CallingActivity.class, "after getStreams");
            // 3c. check if the hotspot operates this app

            // 3c1. send the hotspot a NIAMBIE tag

//            sendDataToServer(HomeActivity.NIAMBIE);

            HomeActivity.logError(CallingActivity.class, "after sendDataToServer(HomeActivity.NIAMBIE)");

            // 3c2. start a countdown timer starting NIAMBIE_COUNTDOWN

//            countdownTimerThread = new CountdownTimerThread(this, HomeActivity.NIAMBIE_COUNTDOWN);
//            countdownTimerThread.run();

            // 3c3. if the hotspot returns a NIAMBIE tag before the countdown ends, continue

//            String inputRequest = "";

            // begin try to try reading input from the other side
//            try {
//
//                inputRequest = (String) localObjectInputStream.readObject();
//
//                // begin if for if the input received is not NIAMBIE
//                if (inputRequest.equals(HomeActivity.NIAMBIE) == false) {
//
//                    // 1. tell the user that the application is not Niambie
//                    // 2. end this activity
//
//                    // 1. tell the user that the application is not Niambie
//                    // 2. end this activity
//
//                    Button buttons[] = {speakerButton, muteButton, endCallButton};
//
//                    HomeActivity.displayErrorsDuringCalls(this, calledPictureBitmapImageView, getResources().getDrawable(R.drawable.not_niambie),
//                            callerNameAndStatusRelativeLayout, String.valueOf(getText(R.string.not_niambie)),
//                            callingStatusTextView, buttons);
//
//                } // end if for if the input received is not NIAMBIE
//
//            } // end try to try reading input from the other side
//
//            // catch class and I/O issues
//            catch (ClassNotFoundException classNotFoundException) {
//                HomeActivity.logError(CallingActivity.class, classNotFoundException.getMessage());
//            } catch (IOException ioException) {
//                HomeActivity.logError(CallingActivity.class, ioException.getMessage());
//            }

            // 3c4. happens in the onCountdownTimerReachedZero method

            // 4. discover the hotspot

            // 4a. send DISCOVER to the hotspot

            HomeActivity.logError( CallingActivity.class, "before sendDataToServer(HomeActivity.DISCOVER)" );
            sendDataToServer(HomeActivity.DISCOVER);

            // 4b. send the caller's name

            sendDataToServer(getCallerName());

            // 4c. set the status TextView to show that we are connecting

            setCallStatusStringID(HomeActivity.CONNECTING);
            callingStatusTextView.setText(getCallStatusStringID());

            // 4d. stop counting down

//            countdownTimerThread = null;

            // 5 - 8 are done in the process requests method(s)

            processRequestsFromServer();

        } // end if for if the connection is set up

    } // end method continueAfterCountdown

} // end activity CallingActivity
