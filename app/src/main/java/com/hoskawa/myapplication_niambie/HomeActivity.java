package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Process;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;


// begin activity HomeActivity
// the landing of the app
public class HomeActivity extends Activity {

    /** CONSTANTS */

    /** Integers */

    public static final int PORT_NUMBER = 8988, // the port number
                            CALL_TIMEOUT = 30000, // the amount of time in milliseconds before a call times out
                            ERROR_DISPLAY_TIME = 2500, // the amount of time in milliseconds in which the call error (busy or timed out) will be displayed
                            NIAMBIE_COUNTDOWN = 5000, // the amount of time in milliseconds before we can conclude that the other party in the communication does not use Niambie
                            VIBRATE_OFF_TIME = 1000, // do not vibrate for one thousand milliseconds
                            VIBRATE_ON_TIME = 5000, // and then vibrate for five thousand milliseconds
                            MINIMUM_UI_DISPLAY_TIME = 2500, // the amount of time in milliseconds in which an activity's default UI should be seen
                            RECORDING_INTERVAL = 2000; // amount of time in milliseconds between two successive records

    /** Strings */

    // bundle constants

    public static final String CALLED_NAME = "Called Name", // name of the person being called
                               CALLER_NAME = "Caller Name", // name of the person initiating the call
                               CALL_IN_SESSION_PARENT_ACTIVITY = "CALL_IN_SESSION_PARENT_ACTIVITY", // string to show which activity calls the call in session activity
                               CALLING_ACTIVITY = "CALLING_ACTIVITY", // identifier for the calling activity as the parent activity
                               INCOMING_CALL_ACTIVITY = "INCOMING_CALL_ACTIVITY", // identifier for the incoming call activity as the parent activity
                               MAKE_CALL_ACTIVITY = "MAKE_CALL_ACTIVITY", // identifier for the make call activity as the parent activity
                               RECEIVE_CALL_ACTIVITY = "RECEIVE_CALL_ACTIVITY"; // identifier for the recieve call activity as the parent activity


    // button state constants

    public static final String SPEAKER_OFF = "SPEAKER_OFF", // the speaker phone is off
                               SPEAKER_ON = "SPEAKER_ON", // the speaker phone is on
                               MUTE_OFF = "MUTE_OFF", // the phone is not muted
                               MUTE_ON = "MUTE_ON"; // the phone is on mute

    // network constants

    public static final String DISCOVER = "Discover", // discover a potential conversation partner and send them this user's contact information - done by the client mostly
                               OFFER = "Offer", // offer this contact's details in response to a DISCOVER - done mostly by the server
                               CONNECTING = "CONNECTING", // the calling activity is attempting to connect to the called
                               RINGING = "RINGING", // the calling activity is ringing the called
                               CALLING_YOU = "CALLING_YOU", // tell the conversation partner that they are being called
                               NIAMBIE = "NIAMBIE", // a tag to identify if both caller and called are using Niambie
                               PICKED_UP = "PICKED_UP", // tell the caller that the called has picked up the phone
                               REJECTED = "REJECTED", // tell the caller that the called has rejected the phone call
                               TIMED_OUT = "TIMED_OUT", // tell the caller that the called has taken too long to pick up
                               TERMINATE = "Terminate", // informs either party that connection is terminating


                               SOUND_BYTE = "Sound Byte", // byte array of sound file coming through

                               HARD_CODED_HOTSPOT_IP_ADDRESS = "192.168.43.1", // according to AOSP, the Android AP has a default IP of 192.168.43.1 so that is what we will use when we are the client

                               APP_HOTSPOT_PASSWORD = "APP_HOTSPOT_PASSWORD", // the hotspot's password as used by the application
                               USER_HOTSPOT_PASSWORD = "USER_HOTSPOT_PASSWORD"; // the hotspot's password as used by the user

    // preferences constants

    public static final String PREFERENCES_NAME = "APP_PREFERENCES"; // the preferences of the app

    /** CONSTANTS */

    /** VARIABLES */

    /** Buttons */

    private Button makeCallButton, // button to choose if to make a call
                   receiveCallButton; // button to choose if to receive a call

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin method onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // 1. initialize UI

        // 1. initialize UI

        makeCallButton = ( Button ) findViewById( R.id.h_b_make_call );

        // begin method makeCallButton.setOnClickListener
        makeCallButton.setOnClickListener(

                // begin View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick( View v ) {

                        // 1. start the make call activity
                        // 2. stop this activity

                        // 1. start the make call activity

                        Intent i = new Intent( HomeActivity.this, MakeCallActivity.class );
                        startActivity( i );

                        // 2. stop this activity

                        finish();

                    } // end method onClick

                } // end View.OnClickListener

        ); // end method makeCallButton.setOnClickListener

        receiveCallButton = ( Button )findViewById( R.id.h_b_receive_call );

        // begin method receiveCallButton.setOnClickListener
        receiveCallButton.setOnClickListener(

                // begin View.OnClickListener
                new View.OnClickListener() {

                    @Override
                    // begin method onClick
                    public void onClick(View v) {

                        // 1. start the receive call activity
                        // 2. stop this activity

                        // 1. start the receive call activity

                        Intent i = new Intent(HomeActivity.this, ReceiveCallActivity.class);
                        startActivity(i);

                        // 2. stop this activity

                        finish();

                    } // end method onClick

                } // end View.OnClickListener

        ); // end method receiveCallButton.setOnClickListener

    } // end method onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    /** Other Methods */

    // begin method getByteArrayFromFile
    // reads contents of a file into a byte array and returns the byte array
    public static byte[] getByteArrayFromFile( File inputFile ) throws IOException {

        // 1. input stream to read from the file
        // 2. get the size of the file
        // 3. create a byte array based on the file's length
        // 4. create the byte array that will hold the file's data
        // 5. read in the bytes to the array
        // 6. ensure all the bytes have been read in
        // 7. close the stream and return the bytes

        // 1. input stream to read from the file
        InputStream inputStream = new FileInputStream( inputFile );

        // 2. get the size of the file
        long fileLength = inputFile.length();

        // 3. create a byte array based on the file's length

        // arrays cannot be created using long
        // so the long will need to be cast into int
        // but before that confirm that the file is not longer than the maximum integer value

        if( fileLength > Integer.MAX_VALUE )
        { throw new IOException( "Could not completely read the file " + inputFile.getName() + " because it is too long.\n" ); }

        // 4. create the byte array that will hold the file's data
        byte[] byteArray = new byte[ ( int ) fileLength ];

        // 5. read in the bytes to the array

        int byteArrayOffset = 0;

        int numberOfBytesRead = inputStream.read( byteArray, byteArrayOffset, byteArray.length - byteArrayOffset );

        // keep reading to the byte array while
        // the byte array offset is within the byte array and
        // the number of bytes read is greater than -1 (-1 would indicate end of file)

        // begin while to read bytes to the array
        while ( byteArrayOffset < byteArray.length && numberOfBytesRead >= 0 ) {

            byteArrayOffset += numberOfBytesRead;
            numberOfBytesRead = inputStream.read( byteArray, byteArrayOffset, byteArray.length - byteArrayOffset );

        } // end while to read bytes to the array

        // 6. ensure all the bytes have been read in
        if( byteArrayOffset < byteArray.length )
        { throw new IOException( "Could not completely read the file " + inputFile.getName() ); }

        // 7. close the stream and return the bytes
        inputStream.close();
        return byteArray;

    } // end method getByteArrayFromFile

    // begin method writeByteArrayToFile
    // writes the passed byte array to the path specified by the passed file
    public static void writeByteArrayToFile( File outputFile, byte[] byteArray ) {

        // 1. create a buffered output stream so that we can write all the bytes once as opposed to byte by byte
        // 2. get a file output stream from the output file
        // 3. wrap the buffer output stream around the file output stream
        // 4. use the buffered output stream to write the contents of the byte array to the file
        // 5. flush and close the buffered output stream

        // 1. create a buffered output stream so that we can write all the bytes once as opposed to byte by byte

        BufferedOutputStream bufferedOutputStream = null;

        // 2. get a file output stream from the output file

        // begin try to try get a file output stream and use it
        try {

            FileOutputStream fileOutputStream = new FileOutputStream( outputFile );

            // removed buffered output stream and just worked with file output stream

            bufferedOutputStream = new BufferedOutputStream( fileOutputStream );

            // 3. use the file output stream to write the contents of the byte array to the file

            bufferedOutputStream.write( byteArray );

        } // end try to try get a file output stream and use it

        // catch IO exceptions
        catch ( IOException e ) { e.printStackTrace(); }

        finally {
            if( bufferedOutputStream != null ) {

                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    } // end method writeByteArrayToFile

    // begin method connectToRemoteHotspot
    // connects to a remote hotspot using the context and SSID passed to it
    public static boolean connectToRemoteHotspot( final Context context, final String remoteSSID ) {

        // 1. initialize a wifi configuration using the remote SSID
        // 1a. instantiate a wifi configuration
        // 1b. set the configuration's SSID, ensuring it has double double quotes ""remoteSSID""
        // 1c. ensure the wifi configuration uses plaintext or static WEP authentication only
        // 2. add the configured network to the networks known by the device
        // 3. connect to the newly configured network

        final boolean connectStatus[] = new boolean[ 1 ];

        // begin class runnable
        // where things will go down
        Runnable runnable = new Runnable() {

            // begin method run
            @Override
            public void run() {

                Process.setThreadPriority( Process.THREAD_PRIORITY_BACKGROUND );

                // 1. initialize a wifi configuration using the remote SSID

                // 1a. instantiate a wifi configuration

                WifiConfiguration wifiConfiguration = new WifiConfiguration();

                // 1b. set the configuration's SSID, ensuring it has double double quotes ""remoteSSID""

                String newSSID = remoteSSID;
                wifiConfiguration.SSID = newSSID;

                // 1c. ensure the wifi configuration uses plaintext or static WEP authentication only

                wifiConfiguration.allowedKeyManagement.set( WifiConfiguration.KeyMgmt.NONE );

                // 2. add the configured network to the networks known by the device

                WifiManager wifiManager = ( WifiManager ) context.getSystemService( Context.WIFI_SERVICE );
                wifiManager.addNetwork( wifiConfiguration );

                // 3. connect to the newly configured network

                // get configured networks

                List< WifiConfiguration > configurations = wifiManager.getConfiguredNetworks();

                // begin for to go through the configured networks
                // when we get to the network we just created, we connect to it
                for( WifiConfiguration currentWifiConfiguration : configurations ){

                    // begin if to look for our configured network
                    // the current configuration is our network if it is not null and
                    // its SSID is the same as that of our configured network
                    if( currentWifiConfiguration.SSID != null &&
                        currentWifiConfiguration.SSID.equals( newSSID ) ) {

                        // 1. disconnect from any connected Wifi
                        // 2. enable the configured network
                        // 3. reconnect

                        // 1. disconnect from any connected Wifi

                        wifiManager.disconnect();

                        // 2. enable the configured network while disabling all other networks
                        wifiManager.enableNetwork( currentWifiConfiguration.networkId, true );

                        // 3. reconnect
                        wifiManager.reconnect();

                        logError( HomeActivity.class, "wifiManager.getConnectionInfo(): " + wifiManager.getConnectionInfo() );
                        connectStatus[ 0 ] = true;

                    } // end if to look for our configured network

                } // end for to go through the configured networks

            } // end method run

        }; // end class runnable

        Thread thread = new Thread( runnable );
        thread.start();

        return connectStatus[ 0 ];

    } // end method connectToRemoteHotspot

    // begin method getHotspotIPAddress
    // gets the IP address of the hotspot
    public static String getHotspotIPAddress() {

        // 1. get network interfaces
        // 2. get IP addresses associated with an interface
        // 3. identify the IP address associated with the hotspot
        //    it should have a "wlan" or a "ap" attached to it

        // begin try to try go through the network interfaces
        try {

            // 1. get network interfaces

            // begin for to go through the network interfaces
            for ( Enumeration< NetworkInterface > enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements(); ) {

                NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();

                // begin if for if the interface contains "wlan" or "ap"

                if( networkInterface.getName().contains( "wlan" ) || networkInterface.getName().contains( "ap" ) ) {

                    // 2. get IP addresses associated with an interface

                    // begin for to go through the IP addresses in an interface
                    for( Enumeration< InetAddress > enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements(); ) {

                        InetAddress inetAddress = enumerationInetAddress.nextElement();

                        // 3. identify the IP address associated with the hotspot

                        // begin if for if the IP address is valid
                        // an IP address is valid if it is not a loopback and
                        // the IP address has four octets
                        if( inetAddress.isLoopbackAddress() == false &&
                            inetAddress.getAddress().length == 4 ) {

                            Log.e( "HomeActivity", inetAddress.getHostAddress());

                            return inetAddress.getHostAddress();

                        } // end if for if the IP address is valid

                    } // end for to go through the IP addresses in an interface

                } // end if for if the interface contains "wlan" or "ap"

            } // end for to go through the network interfaces

        } // end try to try go through the network interfaces

        // catch socket exceptions
        catch ( SocketException socketException ) { logError( HomeActivity.class, socketException.getMessage() ); }

        return null;

    } // end method getHotspotIPAddress

    // begin method toastInformation
    // toasts the user with needed info
    public static void toastInformation( final Activity activity, final String info ) {

        // begin method runOnUiThread
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, info, Toast.LENGTH_SHORT).show();
            }
        }); // end method runOnUiThread

    } // end method toastInformation

    // method logError
    // logs an error into the Log
    public static void logError( Class aClass, String errorMessage ) { Log.e(aClass.getSimpleName(), errorMessage); }

    // begin method displayErrorsDuringCalls
    // displays any errors happening on specific call activities (Calling and Incoming Call) by changing the UI of these activities and finally terminating them
    public static void displayErrorsDuringCalls
    ( final Activity theCallActivity, final ImageView pictureImageView,
      final Drawable replacementDrawable, final RelativeLayout textRelativeLayout,
      final String statusString, final TextView statusTextView, final Button[] buttons ) {

        // 7a. show the unfortunate status
        // 7a1. replace the picture image view with the appropriate image
        // 7a2. set the color of the top text relative layout background to red
        // 7a3. set the calling status to the unfortunate status
        // 7a4. disable all buttons
        // 7a5. show for error time display seconds
        // 8. end call
        // 8a. destroy the activity

        // begin method theCallActivity.runOnUiThread
        theCallActivity.runOnUiThread(

                // begin Runnable
                new Runnable() {

                    @Override
                    // begin method run
                    public void run() {

                        // 7a. show the unfortunate status

                        statusTextView.setText(statusString);

                        // 7a1. replace the picture image view with the appropriate image

                        pictureImageView.setImageDrawable(replacementDrawable);

                        // 7a2. set the color of the top text relative layout background to red

                        textRelativeLayout.setBackgroundColor(theCallActivity.getResources().getColor(R.color.red));

                        // 7a3. set the calling status to the unfortunate status

                        statusTextView.setText(statusString);

                        // 7a4. disable all buttons

                        for (Button button : buttons) { button.setEnabled( false ); }

                        // 7a5. show for error time display seconds

                        // begin new CountDownTimer
                        CountDownTimer countDownTimer = new CountDownTimer( HomeActivity.ERROR_DISPLAY_TIME, 1000 ) {

                            @Override
                            public void onTick( long millisUntilFinished ) {}

                            // 8. end call

                            // 8a. destroy the activity

                            @Override
                            // method onFinish
                            public void onFinish() { theCallActivity.finish(); }

                        };// end new CountDownTimer

                        countDownTimer.start();

//                        theCallActivity.finish();

                    } // end method run

                } // end Runnable

        ); // end method theCallActivity.runOnUiThread

    } // end method displayErrorsDuringCalls

    // begin method changeSpeakerPhoneState
    // turns the speaker on or off
    public static void changeSpeakerPhoneState( Activity activity, String stateToTurnSpeakerTo ) {

        // 1. get the audio manager
        // 2. determine what to do - either switch the speaker on or off
        // 2a. if we are to switch the speaker off then do so
        // 2b. if we are to switch the speaker on then do so

        // 1. get the audio manager

        AudioManager audioManager = ( AudioManager ) activity.getSystemService( Context.AUDIO_SERVICE );

        // 2. determine what to do - either switch the speaker on or off

        // begin switch to determine what to do
        switch ( stateToTurnSpeakerTo ) {

            // 2a. if we are to switch the speaker off then do so

            // case the speaker is to be turned off
            case HomeActivity.SPEAKER_OFF:

                // turn the speaker off
                if( audioManager.isSpeakerphoneOn() != false ) { audioManager.setSpeakerphoneOn( false ); }

                break;

            // 2b. if we are to switch the speaker on then do so

            // case the speaker is to be turned on
            case HomeActivity.SPEAKER_ON:

                if( audioManager.isSpeakerphoneOn() != true ) { audioManager.setSpeakerphoneOn( true ); }

                break;

        } // end switch to determine what to do

    } // end method changeSpeakerPhoneState

    // begin method changeMuteState
    // turns the mute on or off
    public static void changeMuteState( Activity activity, String stateToTurnMuteTo ) {

        // 1. get the audio manager
        // 2. determine what to do - either switch the mute on or off
        // 2a. if we are to switch the mute off then do so
        // 2b. if we are to switch the mute on then do so

        // 1. get the audio manager

        AudioManager audioManager = ( AudioManager ) activity.getSystemService( Context.AUDIO_SERVICE );

        // 2. determine what to do - either switch the mute on or off

        // begin switch to determine what to do
        switch ( stateToTurnMuteTo ) {

            // 2a. if we are to switch the mute off then do so

            // case the mute is to be turned off
            case HomeActivity.MUTE_OFF:

                // turn the mute off
                if( audioManager.isMicrophoneMute() != false ) { audioManager.setMicrophoneMute( false ); }

                break;

            // 2b. if we are to switch the mute on then do so

            // case the mute is to be turned on
            case HomeActivity.MUTE_ON:

                if( audioManager.isMicrophoneMute() != true ) { audioManager.setMicrophoneMute( true ); }

                break;

        } // end switch to determine what to do

    } // end method changeMuteState

} // end activity HomeActivity
