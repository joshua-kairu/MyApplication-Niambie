package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoskawa.time.TimeTaken;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

// begin class CallInSessionActivity
// handles the call being in session
public class CallInSessionActivity extends Activity {

    /** VARIABLES */

    /** Audio Managers */

    private AudioManager audioForSpeakerManager; // for managing the speaker

    /** Buttons */

    private Button speakerButton, // the speakerphone button
                   muteButton, // the mute button
                   endCallButton; // the end call button

    /** Call Infos */

    private CallInfo callInfo; // call information

    /** Contacts */

    private Contact serverContact, // this contact - in the server the contact will be initialized before runtime
                    clientContact; // the client contact

    /** Files */

    private File myInternalSendingRecordFile, // file to store the recorded sound to be sent
                 myInternalReceivingRecordFile; // file to store the recorded sound to be received

    /** Hotspot Util AsyncTasks */

    private HotspotUtilsThread hotspotUtilsThread; // AsyncTask to handle the hotspot


    /** Image Views */

    private ImageView callerPictureImageView; // image view to picture of called/r

//    /** Inet Addresses */
//
//    public static InetAddress serverIPAddress = InetAddress.getByName( "10.1.1.1" ); // the activity's IP

    /** Media Players */

    private MediaPlayer mediaPlayer; // player of the sound

    /** Media Recorders */

    private MediaRecorder mediaRecorder; // recorder of the sound

    /** Object I/O Streams */

    private ObjectInputStream localObjectInputStream; // local stream to read objects sent to the server

    private ObjectOutputStream localObjectOutputStream; // local stream to send objects from the server

    /** Primitives */

    private boolean muteStatus, // status of mute
                    speakerStatus, // status of speakerphone
                    terminatingSelf = false; // status of connection self termination - start with it being false

    /** Server Sockets */

    private ServerSocket serverSocket; // server socket to handle server requests

    /** Sockets */

    private Socket localSocket; // the local socket connection between caller and called

    /** Strings */

    private String callexName, // name of called/r
                   callerPicPath, // path of profile picture of called/r
                   randomCallerName, // a random name for the called/r
                   serverIPAddress, // hard code the IP address as .43.1 since Android hotspots usually have this IP
                   parentActivityString; // string to identify the parent activity

    // not hardcoding now

    /** Text Views */

    private TextView callerNameTextView, // text view to show caller's name
                     currentCallTimeTakenTextView; // text view to show the current time the call has taken

    /** Threads */

    private Thread socketServerThread; // server thread

    /** Time Takens */

    private TimeTaken currentCallTimeTaken; // current time the call has taken

    /** Timers */

    private Timer callTimeTimer; // timer to schedule call time tasks

    /** Timer Tasks */

    private SendRecordedSoundTimerTask sendRecordedSoundTimerTask; // timer task to send recorded data
    private UpdateTimeTimerTask updateTimeTimerTask;// timer task to update the call time

    /** Wifi Managers */

    private WifiManager wifiManager; // manager for the wifi in station mode

    /** METHODS */

    /** Getters and Setters */

    // begin getter for the caller or the called
    private String getCallexName() { return callexName; }

    // setter for the caller or the called
    private void setCallexName( String callexName ) { this.callexName = callexName; }

    // getter for the parent activity string
    private String getParentActivityString() { return parentActivityString; }

    // setter for the parent activity string
    private void setParentActivityString( String parentActivityString ) { this.parentActivityString = parentActivityString; }

    /** Overrides */

    @Override
    // begin onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        // 0. pre-initialization
        // 0a. get bundle things - name of caller/called, parent activity
        // 0b. start with the speaker phone off
        // 0c. start with the mute off
        // 0d. start with the current time taken as zero
        // 1 initialize things
        // 1a. initialize the UI
        // 1b. initialize the recorder
        // 1b1. initialize the recording files
        // 1b2. initialize the audio recorder
        // 1c. initialize the media player
        // 1d. initialize the local socket
        // 1e. initialize and start timers
        // 1e1. initialize and start timer for call time taken
        // 1e2. initialize and start timer for sending records
        // 1e3. start the timer
        // 2. get relevant streams
        // 3. process relevant requests
        // 3a. when a SOUND_BYTE is received
        // 3b. read the file
        // 3c. play the audio
        // 4. speaker phone clicked
        // 4a. change speaker state based on what was previous
        // 5. mute button clicked
        // 5a. change mute state based on what was previous
        // 6. end call button clicked
        // 6a. send TERMINATE
        // 6b. end this activity

        // 0. pre-initialization

        HomeActivity.logError( CallInSessionActivity.class, "just started CallInSessionActivity" );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_call_in_session );

        // 0a. get bundle things - name of caller/called, parent activity

        // parent activity bundle things

        setParentActivityString(getIntent().getExtras().getString(HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY));

        HomeActivity.logError(CallInSessionActivity.class, "setParentActivityString( " + getIntent().getExtras().getString(HomeActivity.CALL_IN_SESSION_PARENT_ACTIVITY) + " )");

        // name of caller/called bundle things

        // if the parent was a calling activity, then the callex passed was the name of the individual called, else
        // the incoming call activity was the parent activity so callex passed is the name of the caller
        if( getParentActivityString().equals( HomeActivity.CALLING_ACTIVITY ) ) { setCallexName(getIntent().getExtras().getString(HomeActivity.CALLED_NAME)); }

        else if ( getParentActivityString().equals( HomeActivity.INCOMING_CALL_ACTIVITY ) ) { setCallexName( getIntent().getExtras().getString(HomeActivity.CALLER_NAME) ); }

        // 0b. start with the speaker phone off

        HomeActivity.changeSpeakerPhoneState( this, HomeActivity.SPEAKER_OFF );

        // 0c. start with the mute off

        HomeActivity.changeMuteState(this, HomeActivity.MUTE_OFF);

        // 0d. start with the current time taken as zero

        currentCallTimeTaken = new TimeTaken();

        // 1 initialize things

        // 1a. initialize the UI

        initializeUI();

        // 1b. initialize the recorder <- done in the initializeRecorder method

//        initializeRecorder();

        // 1c. initialize the media player

//        mediaPlayer = new MediaPlayer();

        // 1d. initialize the local socket

        setUpConnection();

        // 1e. initialize and start timers

        callTimeTimer = new Timer();

        // 1e1. initialize and start timer for call time taken

        updateTimeTimerTask = new UpdateTimeTimerTask( this, currentCallTimeTakenTextView, currentCallTimeTaken );

        // 1e2. initialize and start timer for sending records
        initializeRecorder();
        sendRecordedSoundTimerTask = new SendRecordedSoundTimerTask( this, myInternalSendingRecordFile );

        // 1e3. start the timer

        mediaPlayer = null;
        // the call time timer should start right now at this instance and
        // update the time every second(1000 ms)
        callTimeTimer.schedule( updateTimeTimerTask, Calendar.getInstance().getTime(), 1000 );

        // the sending of recorded sound should start right now at this instance and
        // send sound every second(1000ms)
        long delay = 0;
        callTimeTimer.schedule( sendRecordedSoundTimerTask, delay, HomeActivity.RECORDING_INTERVAL );

        // 2. get relevant streams

        // 3. process relevant requests <- done in the processRequestsFromClient method

        // begin new Thread
        new Thread(

                // begin new Runnable
                new Runnable() {

                    @Override
                    // begin run
                    public void run() {

                        getStreams();

                        processRequestsFromClient();

                    } // end run

                } // end new Runnable

        ).start(); // end new Thread

        // 4. speaker phone clicked
        // 4a. change speaker state based on what was previous
        // 5. mute button clicked
        // 5a. change mute state based on what was previous
        // 6. end call button clicked
        // 6a. send TERMINATE
        // 6b. end this activity

        // 2. start the server

//        serverCode();

    } // end onCreate

    // begin method onPause
    @Override
    protected void onPause() {

        super.onPause();

        // 1. pause the network
        // 1a. stop the hotspot
        // 1b. destroy the wifi manager
        // 2. commit changes to database

        // 1. pause the network

        // 1a. stop the hotspot

        if( hotspotUtilsThread != null ) {

//            hotspotUtilsThread.setHotspotState( HotspotUtilsThread.STOP_HOTSPOT );
            hotspotUtilsThread.run();

        }

        // 1b. destroy the wifi manager

        wifiManager = null;

    } // end method onPause

    // begin method onResume
    @Override
    protected void onResume() {

        super.onResume();

        // 1. resume the network
        // 1a. get the wifi manager
        // 1b. restart the wifi hotspot

        // 1. resume the network

        // 1a. get the wifi manager

        wifiManager = ( WifiManager ) getSystemService( Context.WIFI_SERVICE );

        // 1b. restart the wifi hotspot

        if( hotspotUtilsThread != null ) {

//            hotspotUtilsThread.setHotspotState( HotspotUtilsThread.START_HOTSPOT );
            hotspotUtilsThread.run();

        }

    } // end method onResume

    // begin method onDestroy
    @Override
    protected void onDestroy() {

        super.onDestroy();

        // 1. completely stop the network
        // 1a. stop the hotspot
        // 1b. destroy the wifi hotspot manager
        // 1c. destroy the wifi manager
        // 1d. destroy the connection
        // 1e. destroy the server socket

        // 1. completely stop the network

        // 1a. stop the hotspot

        if( hotspotUtilsThread != null ) {

//            hotspotUtilsThread.setHotspotState( HotspotUtilsThread.STOP_HOTSPOT );
//            hotspotUtilsThread.run();

        }

        sendDataToClient( HomeActivity.TERMINATE );

        // 1b. destroy the wifi hotspot manager

//        if( hotspotUtilsThread!= null ) { hotspotUtilsThread.wiFiHotspotManager = null; hotspotUtilsThread = null; }

        // 1c. destroy the wifi manager

        wifiManager = null;

        // 1d. destroy the connection

//        localSocket = null;

        // 1e. destroy the server socket

//        serverSocket = null;

        if( mediaRecorder != null ) { mediaRecorder.release(); mediaRecorder = null; }

    } // end method onDestroy

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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // begin method setUpConnection
    // sets up a connection to the client
    public void setUpConnection() {

        // 1. initialize the local socket based on the identity of the parent activity
        // 1a. if the parent activity is the incoming call activity then initialize the local socket with the receiving call activity socket
        // 1b. if the parent activity is the calling activity then initialize the local socket with the make call activity socket

        // 1. initialize the local socket based on the identity of the parent activity

        // 1a. if the parent activity is the incoming call activity then initialize the local socket with the receiving call activity socket

        if( getParentActivityString().equals( HomeActivity.INCOMING_CALL_ACTIVITY ) == true ) { localSocket = ReceiveCallActivity.globalServerSideSocket; }

        // 1b. if the parent activity is the calling activity then initialize the local socket with the make call activity socket

        else if ( getParentActivityString().equals( HomeActivity.CALLING_ACTIVITY ) == true ) { localSocket = MakeCallActivity.globalClientSideSocket; }

        toastInformation( "Server: Connection set up" );

    } // end method setUpConnection

    // begin method getStreams
    // sets up I/O streams
    public void getStreams() {

        // 1. initialize the local object I/O streams based on the identity of the parent activity
        // 1a. if the parent activity is the incoming call activity then initialize the local object I/O streams with the receive call activity object I/O streams
        // 1b. if the parent activity is the calling activity then initialize the local object I/O streams with the make call activity object I/O streams

        // 1. initialize the local object I/O streams based on the identity of the parent activity

        // 1a. if the parent activity is the incoming call activity then initialize the local object I/O streams with the receive call activity object I/O streams

        // begin if for if the parent activity is the incoming call activity
        if( getParentActivityString().equals( HomeActivity.INCOMING_CALL_ACTIVITY ) == true ) {

            localObjectOutputStream = ReceiveCallActivity.globalServerSideObjectOutputStream;

            HomeActivity.logError( this.getClass(), "ReceiveCallActivity.globalServerSideObjectOutputStream = " + ReceiveCallActivity.globalServerSideObjectOutputStream.toString() );
            HomeActivity.logError( this.getClass(), "localObjectOutputStream = " + localObjectOutputStream.toString() );

            localObjectInputStream = ReceiveCallActivity.globalServerSideObjectInputStream;

            HomeActivity.logError( this.getClass(), "ReceiveCallActivity.globalServerSideObjectInputStream = " + ReceiveCallActivity.globalServerSideObjectInputStream.toString() );
            HomeActivity.logError( this.getClass(), "localObjectInputStream = " + localObjectInputStream.toString() );


        } // end if for if the parent activity is the incoming call activity

        // 1b. if the parent activity is the calling activity then initialize the local object I/O streams with the make call activity object I/O streams

        // begin if for if the parent activity is the calling activity
        if( getParentActivityString().equals( HomeActivity.CALLING_ACTIVITY ) == true ) {

            localObjectOutputStream = MakeCallActivity.globalClientSideObjectOutputStream;

            localObjectInputStream = MakeCallActivity.globalClientSideObjectInputStream;

        } // end if for if the parent activity is the calling activity


        // begin try to try set up the streams
//        try {
//
//            localObjectOutputStream = new ObjectOutputStream( localSocket.getOutputStream() );
//
//            // flush the output stream to send the header information
//            localObjectOutputStream.flush();
//
//            localObjectInputStream = new ObjectInputStream( localSocket.getInputStream() );
//
//        } // end try to try set up the streams
//
//        catch ( IOException e ) { HomeActivity.logError( CallInSessionActivity.class, e.getMessage() ); }

        HomeActivity.logError(CallInSessionActivity.class, "Streams gotten.");

    } // end method getStreams

    // begin method closeConnection
    // terminates the socket connection
    public boolean closeConnection() {

        // close streams and the connection

//        try {

//            if( localObjectInputStream != null ) { localObjectInputStream.close(); }
//            if( localObjectOutputStream != null ) { localObjectOutputStream.close(); }
//            if( localSocket != null ) { localSocket.close(); }

//        }

//        catch ( IOException e ) { e.printStackTrace(); return false; }

        toastInformation( "Server: Connection closed" );

        return true;

    } // end method closeConnection

    public boolean isTerminatingItself() { return terminatingSelf; }

    public void setTerminatingItself( boolean terminatingItself ) { terminatingSelf = terminatingItself; }


    // begin method processRequestsFromClient
    // processes requests coming from the client
    public boolean processRequestsFromClient() {

        // 3a. when a SOUND_BYTE is received
        // 3b. read the file
        // 3c. play the audio

        String inputRequest = null;

        // begin do while to loop through requests as long as connection is not terminated either by server or client
        do {

            // begin try to get the request from the client
            try {

                // 3a. when a SOUND_BYTE is received
                // 3b. read the file
                // 3c. play the audio

                inputRequest = ( String ) localObjectInputStream.readObject();

                // begin switch to handle the input request
                switch ( inputRequest ) {

                    // 3a. when a SOUND_BYTE is received

                    // case sound byte
                    // TODO-me To implement sound_byte I might have to convert the byte array to a list and back to an array for now, I hope.
                    case HomeActivity.SOUND_BYTE:

                        // 3b. read the file
                        // 3c. play the audio

                        //          ||
                        //          ||
                        //         \  /
                        //          \/

                        // 1. get the list
                        // 2. convert it to a byte array to the received file's path
                        // 3. play the received file
                        // 4. delete the received file

                        // 1. get the list

                        // maybe the assignment here puts the array from the client in the first slot of the byte array list
                        List< Object > byteArrayAsList = ( List< Object > ) localObjectInputStream.readObject();

                        // 2. convert it to a byte array to the received file's path

                        byte[] byteArray = ( byte[] )byteArrayAsList.get( 0 );

                        HomeActivity.logError( CallInSessionActivity.class, "byteArray: " + Arrays.toString( byteArray ) );

//                        myInternalReceivingRecordFile = new File( getApplicationContext().getDir( "RecordedSoundFiles", Context.MODE_PRIVATE ), "receivedRecord.3gp" );

                        if( myInternalReceivingRecordFile == null ) { myInternalReceivingRecordFile = new File( getApplicationContext().getDir( "RecordedSoundFiles", Context.MODE_PRIVATE ), "receivedRecord.3gp" ); }

                        HomeActivity.writeByteArrayToFile(myInternalReceivingRecordFile, byteArray);

                        // 3. play the received file

                        FileInputStream fileInputStream = new FileInputStream( myInternalReceivingRecordFile );

                        HomeActivity.logError( CallInSessionActivity.class, "myInternalReceivingRecordFile.length(): " + myInternalReceivingRecordFile.length() );

                        startPlayingSound(fileInputStream.getFD());

                        // 4. delete the received file

                        myInternalReceivingRecordFile.delete();
                        myInternalReceivingRecordFile = null;

                        break;

                } // end switch to handle the input request

            } // end try to get the request from the client

            catch ( ClassNotFoundException | IOException e ) { e.printStackTrace(); }

            catch( IllegalArgumentException e ) { toastInformation( "Illegal Argument Exception: " + e.getMessage() ); }

        } while ( isTerminatingItself() == false || inputRequest.equals( HomeActivity.TERMINATE ) == false ); // begin do while to loop through requests as long as connection is not terminated either by server or client

        return true;

    } // end method processRequestsFromClient

    // begin method sendDataToClient
    // sends the passed data to the client
    public boolean sendDataToClient( Object data ) {

        // try to send the passed data to the client
        try { localObjectOutputStream.writeObject(data); }

        catch ( IOException e ) { e.printStackTrace(); return false; }

        return true;

    } // end method sendDataToClient

    /** Other Methods */

    // begin method toastInformation
    // toasts the user with needed info
    protected void toastInformation( String info ) {

        final String infoString = info;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CallInSessionActivity.this, infoString, Toast.LENGTH_LONG).show();
            }
        });

    } // end method toastInformation

    // begin method startRecordingSound
    // does the recording of sound and saves the recorded sound in the file
    protected void startRecordingSound() {

        int maxRecordTime = 1000;

        // begin if for if the media recorder is null
        if( mediaRecorder == null ) {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // use sound from the mic
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // use 3GPP sound format
            mediaRecorder.setMaxDuration(maxRecordTime); // record for at most a second
            mediaRecorder.setOutputFile(myInternalSendingRecordFile.getPath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // use AMR NB encoding


            // begin method mediaRecorder.setOnInfoListener
//            mediaRecorder.setOnInfoListener(
//
//                    new MediaRecorder.OnInfoListener() {
//
//                        @Override
//                        public void onInfo(MediaRecorder mr, int what, int extra) {
//
//                            if ( what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED ) {
//
//                                HomeActivity.logError(this.getClass(), "startRecordingSound( ) MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
//
//                                sendRecordedSoundTimerTask.doStopRecordingAndSendRecord();
//
//                                HomeActivity.logError(this.getClass(), "startRecordingSound() after sendRecordedSoundTimerTask.doStopRecordingAndSendRecord()");
//
//                                mediaRecorder.reset();
//
//                                HomeActivity.logError(this.getClass(), "startRecordingSound() after mediaRecorder.reset()");
//
//                                mediaRecorder.release();
//
//                                HomeActivity.logError(this.getClass(), "startRecordingSound() after mediaRecorder.release()");
//
//                                mediaRecorder = null;
//
//                                HomeActivity.logError(this.getClass(), "startRecordingSound() after mediaRecorder = null");
//
//                            }
//
//                        }
//
//            }); // end method mediaRecorder.setOnInfoListener

            HomeActivity.logError(this.getClass(), "just at startRecordingSound()");

            // try preparing the recorder
            try {

                mediaRecorder.prepare();

            } catch (IOException e) {

                e.printStackTrace();

                HomeActivity.logError(this.getClass(), "at startRecordingSound() - prepare problem");

                toastInformation(e.getMessage());

            }

            HomeActivity.logError(this.getClass(), "at startRecordingSound() - after successful prepare");

            try { mediaRecorder.start(); }

            catch ( IllegalStateException e ) { HomeActivity.logError(this.getClass(), "at startRecordingSound() - start problem"); }

            HomeActivity.logError(this.getClass(), "at startRecordingSound() - after successful start");

        } // end if for if the media recorder is null

    } // end method startRecordingSound

    // begin method stopRecordingSound
    // stops the recording of sound
    protected void stopRecordingSound() {

        HomeActivity.logError(this.getClass(), "at stopRecordingSound() - just in");

        try {

            mediaRecorder.stop();

            HomeActivity.logError(this.getClass(), "at stopRecordingSound() - after mediaRecorder.stop()");
        }

        catch ( RuntimeException e ) {

            HomeActivity.logError(this.getClass(), "at stopRecordingSound() - stop problem");

            e.printStackTrace();

        }

        finally {

            HomeActivity.logError(this.getClass(), "at stopRecordingSound() - finally");

            mediaRecorder.release();

            HomeActivity.logError(this.getClass(), "at stopRecordingSound() - after mediaRecorder.release()");

            mediaRecorder = null;

            HomeActivity.logError(this.getClass(), "at stopRecordingSound() - after mediaRecorder = null");

        }

    } // end method stopRecordingSound

    // begin method startPlayingSound
    // plays sound from the file passed to it
    private void startPlayingSound( FileDescriptor fileDescriptor )
//    private void startPlayingSound( String path )
    {
        // try use a file descriptor to play the file
//        FileInputStream fileInputStream = null;
//        try { fileInputStream = new FileInputStream( file ); } catch ( FileNotFoundException e ) { toastInformation( e.getMessage() ); return false; }

        HomeActivity.logError( CallInSessionActivity.class, "startPlayingSound - just in" );

        if( mediaPlayer == null ) {

            try{

                mediaPlayer = new MediaPlayer();

                HomeActivity.logError( CallInSessionActivity.class, "startPlayingSound - mediaPlayer = " + mediaPlayer.toString() );

                HomeActivity.logError( CallInSessionActivity.class, "startPlayingSound - after initializing new MediaPlayer" );

                mediaPlayer.setDataSource( fileDescriptor );

                HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - after successfully setting the media player data source");

            // begin method mediaPlayer.setOnCompletionListener
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    mp.release();

                    HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - after releasing the media player");

                    mediaPlayer = null;

                    HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - after nullifying the media player");

                }

            }); // end method mediaPlayer.setOnCompletionListener

//            mediaPlayer.reset();
//            mediaPlayer.setDataSource( path );
//            mediaPlayer.setDataSource( fileDescriptor );
                mediaPlayer.prepare();
                HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - after preparing the media player");
                mediaPlayer.start();
                HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - after starting the media player");
                HomeActivity.logError(CallInSessionActivity.class, "================Media Player Success===========");
                HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - media player plays the file");
                HomeActivity.logError(CallInSessionActivity.class, "================Media Player Success===========");

            }

            catch (IOException e) {
                HomeActivity.logError(CallInSessionActivity.class, "startPlayingSound - mediaPlayer.setDataSource( fileDescriptor )IOException e");
                e.printStackTrace();
            }
        }



    } // end method startPlayingSound

    // begin method stopPlayingSound
    private void stopPlayingSound() {

        mediaPlayer.release();
        mediaPlayer = null;
        toastInformation( "media player stopped playing file" );

    } // end method stopPlayingSound

    // begin method initializeUI
    private void initializeUI() {

        callerNameTextView = ( TextView ) findViewById( R.id.cis_tv_caller_name );
        callerNameTextView.setText(callexName);

        currentCallTimeTakenTextView = ( TextView ) findViewById( R.id.cis_tv_call_time );


        callerPictureImageView = ( ImageView ) findViewById( R.id.cis_iv_caller_picture );
        speakerButton = ( Button ) findViewById( R.id.cis_b_speaker );

        // just for testing
        // use the speaker button to start the connection
        // begin method speakerButton.setOnClickListener
        speakerButton.setOnClickListener(

                // begin anonymous inner class View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {



                    } // end method onClick

                } // end anonymous inner class View.OnClickListener

        ); // end method speakerButton.setOnClickListener


        muteButton = ( Button ) findViewById( R.id.cis_b_mute );

        // begin method muteButton.setOnClickListener
        muteButton.setOnClickListener(



                // begin anonymous inner class View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {



                    } // end method onClick

                } // end anonymous inner class View.OnClickListener

        ); // end method muteButton.setOnClickListener

        endCallButton = ( Button ) findViewById( R.id.cis_b_end_call );

        // begin method endCallButton.setOnClickListener
        endCallButton.setOnClickListener(

                // terminate the connection here

                // begin inner class View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {

                        sendDataToClient(HomeActivity.TERMINATE);
//                        setTerminatingItself( true );
//                        closeConnection();

                        finish();

                    } // end method onClick

                } // end inner class View.OnClickListener

        ); // end method endCallButton.setOnClickListener

    } // end method initializeUI

    // begin method initializeRecorder
    private void initializeRecorder() {

        // 1b1. initialize the recording files

        myInternalSendingRecordFile = new File( getApplicationContext().getDir( "RecordedSoundFiles", Context.MODE_PRIVATE ), "recordToSend.3gp" );

        myInternalReceivingRecordFile = new File( getApplicationContext().getDir( "RecordedSoundFiles", Context.MODE_PRIVATE ), "receivedRecord.3gp" );

        // 1b2. initialize the audio recorder

        mediaRecorder = null;
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC ); // use sound from the mic
//        mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP ); // use 3GPP sound format
//        mediaRecorder.setOutputFile( myInternalSendingRecordFile.getPath() );
//        mediaRecorder.setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB ); // use AMR NB encoding

    } // end method initializeRecorder

} // end class CallInSessionActivity
