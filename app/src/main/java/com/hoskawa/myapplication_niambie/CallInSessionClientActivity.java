package com.hoskawa.myapplication_niambie;

import android.app.ListActivity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoskawa.networking.Client;
import com.hoskawa.time.TimeTaken;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

// begin activity CallInSessionClientActivity
public class CallInSessionClientActivity extends ListActivity implements Client {

    // for prototyping purposes
    // 1. show a list of available hotspots
    //    user will select the correct hotspot
    // 2. we connect to that hotspot
    // 3. we start call functions
    // 3a. by displaying the call in session UI

    /** VARIABLES */

    /** For Prototyping */

    private ArrayList< Contact > contacts; // the contacts visible

    private Button scanButton; // button to scan for available users(hotspots)

    /** Audio Managers */

    private AudioManager audioForSpeakerManager; // for managing the speaker

    /** Buttons */

    private Button speakerButton, // the speakerphone button
                   muteButton, // the mute button
                   endCallButton; // the end call button

    /** Call Infos */

    private CallInfo callInfo; // call information

    /** Contacts */

    private Contact clientContact, // this contact - in the client the contact will be initialized before runtime
                    serverContact; // the server contact

    /** Files */

    private File myInternalSendingRecordFile;


    /** Image Views */

    private ImageView callerPictureImageView; // image view to picture of called/r


    /** Media Players */

    private MediaPlayer mediaPlayer; // player of the sound

    /** Media Recorders */

    private MediaRecorder mediaRecorder; // recorder of the sound

    /** Object I/O Streams */

    private ObjectInputStream objectInputStream; // stream to read objects sent to the server

    private ObjectOutputStream objectOutputStream; // stream to send objects from the server

    /** Primitives */

    private int randomNameDisplayTimeInMilliseconds; // time in milliseconds to display the random name

    private boolean muteStatus, // status of mute
                    speakerStatus, // status of speakerphone
                    terminatingSelf = false; // status of connection self termination - start with it being false

    /** Server Sockets */

    private ServerSocket serverSocket; // server socket to handle server requests

    /** Sockets */

    private Socket connection; // socket connection between caller and called

    /** Strings */

    private String callerName, // name of called/r
            callerPicPath; // path of profile picture of called/r

    /** Text Views */

    private TextView callerNameTextView, // text view to show caller's name
            currentCallTimeTakenTextView; // text view to show the current time the call has taken

    /** Time Takens */

    private TimeTaken currentCallTimeTaken; // current time the call has taken

    /** Timers */

    private Timer callTimeTimer; // timer to schedule call time tasks

    /** Timer Tasks */

    private UpdateTimeTimerTask updateTimeTimerTask;// timer task to update the call time
    private SendRecordedSoundTimerTask sendRecordedSoundTimerTask; // timer task to send recorded sound

    /** METHODS */

    /** Overrides */

    @Override
    // begin onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

//        // initialize recording file
//        myInternalSendingRecordFile = new File( new ContextWrapper( getApplicationContext() ).getDir("RecordedSoundStorage", Context.MODE_PRIVATE ), "record.3gp" );
//        Log.e( "my internal sending record file", myInternalSendingRecordFile.getPath() );
//
//        /** Initialize UI */
//
//        callerNameTextView = ( TextView ) findViewById( R.id.cisc_tv_caller_name );
//        callerNameTextView.setText( callerName );
//
//        currentCallTimeTakenTextView = ( TextView ) findViewById( R.id.cisc_tv_call_time );
//        currentCallTimeTaken = new TimeTaken();
//
//        callerPictureImageView = ( ImageView ) findViewById( R.id.cisc_iv_caller_picture );
//
//        speakerButton = ( Button ) findViewById( R.id.cisc_b_speaker );
//
//        // TODO-me speaker button - start recording
//        // TODO-me mute button - stop recording
//        // TODO-me end button - play and send recording
//        // begin method speakerButton.setOnClickListener
//        speakerButton.setOnClickListener(
//
//                // begin anonymous inner class View.OnClickListener
//                new View.OnClickListener() {
//
//                    @Override
//                    // begin method onClick
//                    public void onClick( View v ) {
//
//                        startRecordingSound();
//                        toastInformation( "recording started" );
//
//                    } // end method onClick
//
//                } // end anonymous inner class View.OnClickListener
//
//        ); // end method speakerButton.setOnClickListener
//
//        muteButton = ( Button ) findViewById( R.id.cisc_b_mute );
//
//        // begin method muteButton.setOnClickListener
//        muteButton.setOnClickListener(
//
//                // begin anonymous inner class View.OnClickListener
//                new View.OnClickListener() {
//
//                    @Override
//                    // begin method onClick
//                    public void onClick( View v ) {
//
//                        stopRecordingSound();
//                        toastInformation( "recording stopped" );
//
//                    } // end method onClick
//
//                } // end anonymous inner class View.OnClickListener
//
//        ); // end method muteButton.setOnClickListener
//
//        endCallButton = ( Button ) findViewById( R.id.cisc_b_end_call );
//
//        // begin method endCallButton.setOnClickListener
//        endCallButton.setOnClickListener(
//
//                // terminate the connection here
//
//                // begin inner class View.OnClickListener
//                new View.OnClickListener() {
//
//                    @Override
//                    // begin method onClick
//                    public void onClick( View v ) {
//
//
//                        startPlayingSound(myInternalSendingRecordFile.getPath());
//                        toastInformation( "started playing sound" );
//
//                        // convert the recorded file to a byte array then to a List then send it to the other side
//
//                        // begin try to try send the recorded file
//                        try {
//
//                            toastInformation( "try sending sound" );
//
//                            // convert the recorded file to a byte array then to a List then send it to the other side
//
//                            sendDataToServer( MakeCallActivity.SOUND_BYTE );
//                            sendDataToServer( Arrays.asList( MakeCallActivity.getByteArrayFromFile( myInternalSendingRecordFile ) ) );
//
//                        } // end try to try send the recorded file
//
//                        // IO Exceptions from the get bytes method just show problems with the recorded file
//                        // print them out as necessary
//                        catch ( IOException e ) { toastInformation( e.getMessage() ); }
//
////                        sendDataToServer( MakeCallActivity.TERMINATE );
////                        closeConnection();
//
//                    } // end method onClick
//
//                } // end inner class View.OnClickListener
//
//        ); // end method endCallButton.setOnClickListener
//
//        /** Initialize connection */
//
//        // start network thread
//        Thread clientThread = new Thread( new ClientThread( "Client Thread", this ) );
//        clientThread.start();
//
//        /** Initialize timer tasks */
//
//        callTimeTimer = new Timer();
//        updateTimeTimerTask = new UpdateTimeTimerTask( this, currentCallTimeTakenTextView, currentCallTimeTaken );
//
//        // the call time timer should start now and
//        // update the time every second(1000 ms)
//        callTimeTimer.schedule( updateTimeTimerTask, Calendar.getInstance().getTime(), 1000 );
//
//        /** Initialize contact details */
//
//        callerName = "Client";
//
//        clientContact = new Contact( "Joshua - Client", Contact.NOT_APPLICABLE, null, connection.getInetAddress().toString(), null, false, null );
//
//        /** Start relevant processes */
//
//        // set up the connection to the server
//        setUpServerConnection();

    } // end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        // 1. turn off wifi if the wifi is on

        // 1. turn off wifi if the wifi is on

        WifiManager wifiManager = ( WifiManager ) getSystemService( Context.WIFI_SERVICE );

        if( wifiManager.isWifiEnabled() == true ) { wifiManager.setWifiEnabled( false ); }

    }  // end method onDestroy

    // begin method setUpConnection
    // sets up a connection to the client
    public boolean setUpConnection() {

        connection = null;

        try { connection = new Socket( HomeActivity.HARD_CODED_HOTSPOT_IP_ADDRESS, HomeActivity.PORT_NUMBER ); }
        catch ( IOException e ) { e.printStackTrace(); return false; }

        HomeActivity.toastInformation(CallInSessionClientActivity.this, "Client: Connection set up.");

        return true;

    } // end method setUpConnection

    // begin method getStreams
    // sets up I/O streams
    public boolean getStreams() {

        objectInputStream = null;
        objectOutputStream = null;

        // begin try to try set up the streams
        try {

            objectOutputStream = new ObjectOutputStream( connection.getOutputStream() );

            // flush the output stream to send the header information
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream( connection.getInputStream() );

        } // end try to try set up the streams

        catch ( IOException e ) { e.printStackTrace(); return false; }

        HomeActivity.toastInformation(CallInSessionClientActivity.this, "Client: Streams gotten.");

        return true;

    } // end method getStreams

    // begin method closeConnection
    // terminates the socket connection
    public boolean closeConnection() {

        // close streams and the connection

        try {

            if( objectInputStream != null ) { objectInputStream.close(); }
            if( objectOutputStream != null ) { objectOutputStream.close(); }
            if( connection != null ) { connection.close(); }

        }

        catch ( IOException e ) { e.printStackTrace(); return false; }
        HomeActivity.toastInformation(CallInSessionClientActivity.this, ("Client: Connection closed."));
        return true;

    } // end method closeConnection

    @Override
    public boolean isTerminatingItself() { return terminatingSelf; }

    @Override
    public void setTerminatingItself( boolean terminatingItself ) { terminatingSelf = terminatingItself; }

    @Override
    // begin method sendDataToServer
    public boolean sendDataToServer( Object data ) {

        // try to send data to the server
        try {

            objectOutputStream.writeObject( data );
            objectOutputStream.flush();
        }

        catch ( IOException e ) { e.printStackTrace(); return false; }


        return true;

    } // end method sendDataToServer

    /** Other Methods */

    // begin method processRequestsFromServer
    // processes requests from the server
    public boolean processRequestsFromServer() {

        String inputRequest = null;

        // begin do while to loop through requests as long as connection is not terminated
        do {

            // begin try to get the request from the server
            try {

                inputRequest = ( String ) objectInputStream.readObject();

                // begin switch to handle the input request
                switch ( inputRequest ) {

                    // case offer
                    // 1. get the server contact
                    // 2. update the UI as needed
                    case HomeActivity.OFFER:
                        break;

                    // case recorded file
                    case HomeActivity.SOUND_BYTE:

                        // play the received file

                        File receivedFile = ( File ) objectInputStream.readObject();
                        // TODO-me play sound should be changed to match the planned use of byte array
//                        startPlayingSound( receivedFile );

                        break;


                } // end switch to handle the input request

            } // end try to get the request from the client

            catch ( ClassNotFoundException | IOException e ) { e.printStackTrace(); }

        } while ( isTerminatingItself() == false || inputRequest.equals( HomeActivity.TERMINATE ) == false ); // begin do while to loop through requests as long as connection is not terminated

        return true;

    } // end method processRequestsFromServer

    // begin method startRecordingSound
    // does the recording of sound and saves the recorded sound in the file
    private void startRecordingSound() {

        // begin if to check if the media recorder is null, that is, not initialized
        if( mediaRecorder == null ) {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC ); // use sound from the mic
            mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP ); // use 3GPP sound format
            mediaRecorder.setOutputFile( myInternalSendingRecordFile.getPath() );
            mediaRecorder.setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB ); // use AMR NB encoding

        } // end if to check if the media recorder is null, that is, not initialized

        // try preparing the recorder
        try { mediaRecorder.prepare(); }
        catch ( IOException e ) { HomeActivity.toastInformation(CallInSessionClientActivity.this, (e.getMessage())); }

        mediaRecorder.start();

    } // end method startRecordingSound

    // begin method stopRecordingSound
    // stops the recording of sound
    private void stopRecordingSound() {

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    } // end method stopRecordingSound

    // begin method startPlayingSound
    // plays sound from the file passed to it
    private void startPlayingSound( String file ) {

        // try use a file descriptor to play the file
//        FileInputStream fileInputStream = null;
//        try { fileInputStream = new FileInputStream( file ); } catch ( FileNotFoundException e ) { MakeCallActivity.toastInformation( CallInSessionClientActivity.this,( e.getMessage() ); return false; }

        mediaPlayer = new MediaPlayer();

        // try set up and prepare the media player
        try {


            mediaPlayer.setDataSource( file );
            mediaPlayer.prepare();
            mediaPlayer.start();
            HomeActivity.toastInformation(CallInSessionClientActivity.this, ("media player started playing file"));

        }

        catch ( IOException e ) { HomeActivity.toastInformation(CallInSessionClientActivity.this, (e.getMessage())); }



    } // end method startPlayingSound

    // begin method stopPlayingSound
    private void stopPlayingSound() {

        mediaPlayer.release();
        mediaPlayer = null;
        HomeActivity.toastInformation(CallInSessionClientActivity.this, ("media player stopped playing file"));

    } // end method stopPlayingSound

    // begin method setUpServerConnection
    private void setUpServerConnection() {

        // try to send discover to the server
        sendDataToServer( HomeActivity.DISCOVER );

        // send the client contact
        sendDataToServer( clientContact );


    } // end method setUpServerConnection

    // begin method updateServerContactInfoOnUI
    // uses the passed server information to update the UI
    private void updateServerContactInfoOnUI( final Contact server ) {

        // begin method runOnUiThread
        runOnUiThread(

                // begin method Runnable
                new Runnable() {

                    // begin method run
                    @Override
                    public void run() {

                        callerName = server.getName();
                        callerNameTextView.setText( callerName );

                    } // end method run

                } // end method Runnable

        ); // end method runOnUiThread

    } // end method updateServerContactInfoOnUI

} // end activity CallInSessionClientActivity
