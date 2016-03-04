package com.hoskawa.myapplication_niambie;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by user on 24-09-2015.
 */

// TODO-me start implementing the recording every second
// TODO-me      recording should start as soon as the connection is set up
// TODO-me      and end as soon as the call ends

// begin class SendRecordedSoundTimerTask
// sends recorded sound and
// nullifies the data in the file
// every second
public class SendRecordedSoundTimerTask extends TimerTask {

    /** CONSTANTS */

    /** Integers */

    public static final String PLEASE_RECORD = "PLEASE_RECORD", // flag for when it is time to start recording
                               STOP_RECORDING_AND_SEND_RECORD = "STOP_RECORDING_AND_SEND_RECORD"; // flag for when it is time to stop recording and start sending the recorded file

    /** VARIABLES */

    /** Call In Session Server Activities */

    private CallInSessionActivity currentActivity; // the current call in session server activity

    /** Files */

    private File myInternalSendingRecordFile; // the internal file to which sound will be recorded

    /** Strings */

    private String flag; // flag to tell us what we are supposed to do at this time

    /** CONSTRUCTORS */

    // begin constructor
    public SendRecordedSoundTimerTask( CallInSessionActivity currentActivity, File myInternalSendingRecordFile ) {

        this.currentActivity = currentActivity;
        this.myInternalSendingRecordFile = myInternalSendingRecordFile;

        // start with the flag set to recording

        setFlag( PLEASE_RECORD );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the flag
    public String getFlag() { return flag; }

    // setter for the flag
    public void setFlag( String flag ) { this.flag = flag; }

    /** Overrides */

    @Override
    // begin method run
    public void run() {

        HomeActivity.logError( SendRecordedSoundTimerTask.class, "begin method run: flag = " + getFlag()  );

        // begin switch to determine what to do
        switch ( getFlag() ) {

            // case for when we are to start recording
            case PLEASE_RECORD:

                doStartRecordingThings();

                HomeActivity.logError(SendRecordedSoundTimerTask.class, "finished doStartRecordingThings() : flag = " + getFlag());

                setFlag( STOP_RECORDING_AND_SEND_RECORD );

                break;

            // case for when we are to stop recording and think about sending the recorded file
            case STOP_RECORDING_AND_SEND_RECORD:

                doStopRecordingAndSendRecord();

                HomeActivity.logError(SendRecordedSoundTimerTask.class, "finished doStopRecordingAndSendRecord() : flag = " + getFlag());

                setFlag( PLEASE_RECORD );
                break;

        } // end switch to determine what to do

    } // end method run

    /** Other Methods */

    // begin method doStartRecordingThings
    // will start the recording
    private void doStartRecordingThings() {

//            File file = new File(myInternalSendingRecordFile);

        // initialize recording file
//        String fileName = currentActivity.getFilesDir().getAbsolutePath();
//        fileName += File.separator + "record.aac";
//        myInternalSendingRecordFile = new File( fileName );

        myInternalSendingRecordFile = new File( currentActivity.getApplicationContext().getDir( "RecordedSoundFiles", Context.MODE_PRIVATE ), "recordToSend.3gp" );

        // start recording sound
        currentActivity.startRecordingSound();

    } // end method doStartRecordingThings

    // begin method doStopRecordingAndSendRecord
    // will stop recording and send the recorded file
    public void doStopRecordingAndSendRecord() {

        // stop recording sound
        currentActivity.stopRecordingSound();

        HomeActivity.logError( SendRecordedSoundTimerTask.class, "String.valueOf(myInternalSendingRecordFile.length() ) " + String.valueOf(myInternalSendingRecordFile.length() ) );
        List recordedBytesAsList  = null;

        // try to convert the file to a byte array
        try { recordedBytesAsList = Arrays.asList( HomeActivity.getByteArrayFromFile( myInternalSendingRecordFile ) ); }

        // catch I/O issues
        catch ( IOException e ) { e.printStackTrace(); HomeActivity.logError( SendRecordedSoundTimerTask.class, e.getMessage() ); }

        // send sound to client
        currentActivity.sendDataToClient( HomeActivity.SOUND_BYTE );
        currentActivity.sendDataToClient( recordedBytesAsList );

//        // stop recording sound
//        currentActivity.stopRecordingSound();

        // nullify the file
        myInternalSendingRecordFile = null;

    } // end method doStopRecordingAndSendRecord

} // end class SendRecordedSoundTimerTask
