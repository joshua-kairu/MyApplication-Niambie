package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// begin activity ReceiveCallActivity
// is where the user will wait until a call is received
public class ReceiveCallActivity extends Activity implements GetUserPasswordListener {

    /** CONSTANTS */

    /** VARIABLES */

    /** Object I/O Streams */

    public static ObjectInputStream globalServerSideObjectInputStream; // global server side stream to read objects sent to the server

    private ObjectOutputStream localObjectOutputStream; // local stream to read objects sent to the server

    public static ObjectOutputStream globalServerSideObjectOutputStream; // global server side stream to send objects from the server

    private ObjectInputStream localObjectInputStream; // local stream to send objects from the server

    /** Server Sockets */

    private ServerSocket serverSocket; // the server socket

    /** Socket Server Threads */

    private SocketServerThread socketServerThread; // the server socket management thread

    /** Sockets */

    public static Socket globalServerSideSocket; // the public socket

    private Socket localSocket; // the local socket we will use throughout this activity

    /** Strings */

    private String userPasswordString, // the user's password
                   callerName; // the caller's name

    /** Text Views */

    private TextView waitingForCallTextView; // text view for showing we are waiting for a call

    /** METHODS */

    /** Getters and Setters */

    // getter for the user password
    public String getUserPasswordString() { return userPasswordString; }

    // setter for the user password
    public void setUserPasswordString( String userPasswordString ) { this.userPasswordString = userPasswordString; }

    // getter for the caller's name
    private String getCallerName() { return callerName; }

    // setter for the caller's name
    private void setCallerName( String callerName ) { this.callerName = callerName; }

    /** Overrides */

    @Override
    // begin method onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_receive_call );

        // 0. set things straight
        // 0a. store the user's password
        // 0b. set the hotspot password to be the app password
        // 1. initialize UI
        // 2. set up the hotspot
        // 3. wait for a call
        // 3a. initialize the server socket
        // 3b. initialize the socket to connect server and client - the socket will be public and static
        // 3c. get relevant streams
        // 3d. when a DISCOVER is received, get the caller's name immediately after the DISCOVER
        // 4. switch to incoming call
        // 4a. send an OFFER
        // 4b. create the Incoming Call Activity
        // 4c. pass the caller's name to the activity
        // 4d. start the Incoming Call Activity
        // 4e. reset the waiting for call text
        // 5. when a NIAMBIE tag arrives
        // 5a. immediately send a similar tag back out

        // 0. set things straight

        // 0a. store the user's password

//        storeUserPassword();

        // 0b. set the hotspot password to be the app password

//        changeHotspotPassword(HomeActivity.APP_HOTSPOT_PASSWORD);

        // 1. initialize UI

        initializeUI();

        // 2. set up the hotspot

//        setUpHotspot();

        // 3. wait for a call

        // 3a. initialize the server socket

        try { serverSocket = new ServerSocket( HomeActivity.PORT_NUMBER, 1, InetAddress.getByName( HomeActivity.HARD_CODED_HOTSPOT_IP_ADDRESS ) ); }

        catch ( IOException ioException ) { HomeActivity.logError( ReceiveCallActivity.class, ioException.getMessage() ); }

//        SetUpGlobalSocketAsyncTask setUpGlobalSocketAsyncTask = new SetUpGlobalSocketAsyncTask( serverSocket, HomeActivity.RECEIVE_CALL_ACTIVITY );
//        setUpGlobalSocketAsyncTask.execute();

        socketServerThread = new SocketServerThread( "Socket Server Thread", this );
        socketServerThread.start();

        // 3c. get relevant streams

//        getStreams();

        // 3d - 4d are done in the process requests method

//        processRequestsFromClient();

    } // end method onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive_call, menu);
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

    //    @Override
//    // begin method onResume
//    protected void onResume() {
//
//        super.onResume();
//
//        // 1. restart the hotspot if it is not already up
//
//        // 1. restart the hotspot if it is OK to (re)start it
//
//        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
//
//        if( hotspotUtilsThread.itsOKToStartHotspot() == true ) {
//
//            Log.e( "MakeCallActivity-onResume-itsOKToStartHotspot", "hotspotUtilsThread.itsOKToStartHotspot() == true" );
//            hotspotUtilsThread.setHotspotState( HotspotUtilsThread.START_HOTSPOT );
//            hotspotUtilsThread.run();
//
//        }
//
//    } // end method onResume

//    @Override
//    // begin method onPause
//    protected void onPause() {
//
//        super.onPause();
//
//        // 1. return the hotspot's password to its original password
//        // 2. turn the wifi off
//        // 3. turn the hotspot off
//
//        // 1. return the hotspot's password to its original password
//
////        if( WiFiHotspotManager.getHotspotPassword( this ).equals( getUserPassword() ) == false ) { WiFiHotspotManager.setHotspotPassword( this, getUserPassword() ); }
//
//        // 2. turn the wifi off
//
//        WifiManager wifiManager = ( WifiManager )getSystemService( Context.WIFI_SERVICE );
//        if( wifiManager.isWifiEnabled() == true ) { wifiManager.setWifiEnabled( false ); }
//
//        // 3. turn the hotspot off
//        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
//        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.STOP_HOTSPOT );
//        hotspotUtilsThread.run();
//
//    } // end method onPause

//    @Override
//    // begin method onStop
//    protected void onStop() {
//
//        super.onStop();
//
//        // 1. return the hotspot's password to its original password
//        // 2. turn the wifi off
//        // 3. turn the hotspot off
//
//        // 1. return the hotspot's password to the app password
//
////        WiFiHotspotManager.setHotspotPassword( this, APP_HOTSPOT_PASSWORD );
//
//        // 2. turn the wifi off
//
//        WifiManager wifiManager = ( WifiManager )getSystemService( Context.WIFI_SERVICE );
//        if( wifiManager.isWifiEnabled() == true ) { wifiManager.setWifiEnabled( false ); }
//
//        // 3. turn the hotspot off
//        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
//        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.STOP_HOTSPOT );
//        hotspotUtilsThread.run();
//
//    } // end method onStop

    @Override
    // begin method onDestroy
    protected void onDestroy() {

        super.onDestroy();

        // 1. return the hotspot's password to its original password
        // 2. close the sockets
        // 3. turn the hotspot off

        // 1. return the hotspot's password to its original password

//        if( WiFiHotspotManager.getHotspotPassword( this ).equals( getUserPassword() ) == false ) { WiFiHotspotManager.setHotspotPassword( this, getUserPassword() ); }

        // 2. close the sockets

//        closeConnection();
//        tearDownSocket();

        // 3. turn the hotspot off

//        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
//        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.STOP_HOTSPOT );
//        hotspotUtilsThread.run();

    } // end method onDestroy

    @Override
    // begin method onGetUserPassword
    public void onGetUserPassword( String userPassword ) {

        // 1. set the user's password

        // 1. set the user's password

        setUserPasswordString(userPassword);

    } // end method onGetUserPassword

    /** Other Methods */

    // begin method storeUserPassword
    private void storeUserPassword() {

        // 1. get the user's password initialized using the hotspot utility
        // 2. create a thread to store the user's password to the shared preferences

        // 1. get the user's password initialized using the hotspot utility

        // we wont use a thread here because the utility creates a new thread by itself

        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.GET_PASSWORD );
        hotspotUtilsThread.start();

        // 2. create a thread to store the user's password to the shared preferences

        SharedPreferences settings = getSharedPreferences( HomeActivity.PREFERENCES_NAME, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(HomeActivity.USER_HOTSPOT_PASSWORD, getUserPasswordString());

        editor.commit();

    } // end method storeUserPassword

    // begin method changeHotspotPassword
    private void changeHotspotPassword( String newPassword ) {

        String[] parameters = { newPassword };

        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
        hotspotUtilsThread.setHotspotState(HotspotUtilsThread.SET_PASSWORD);
        hotspotUtilsThread.setParameters(parameters);
        hotspotUtilsThread.run();

    } // end method changeHotspotPassword

    // begin method initializeUI
    private void initializeUI() {

        waitingForCallTextView = ( TextView ) findViewById( R.id.rc_tv_waiting_for_call );

    } // end method initializeUI

    // begin method setUpHotspot
    private void setUpHotspot() {

        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.START_HOTSPOT );
        hotspotUtilsThread.run();

    } // end method setUpHotspot

    // begin method setUpConnection
    public boolean setUpConnection() {

        // try to get a socket from the server socket
        try {
            HomeActivity.logError( SetUpGlobalSocketAsyncTask.class, "waiting for an accept" );
            ReceiveCallActivity.globalServerSideSocket = serverSocket.accept();
            HomeActivity.logError( SetUpGlobalSocketAsyncTask.class, "gotten an accept" );
            localSocket = globalServerSideSocket;
            return true; }

        // catch I/O issues
        catch ( IOException ioException ) { HomeActivity.logError( ReceiveCallActivity.class, ioException.getMessage() ); return false; }
    } // end method setUpConnection

    // begin method getStreams
    public void getStreams() {

        // 1. null initialize the object streams
        // 2. try to set up the streams
        // 3. catch any I/O issues

        // 1. null initialize the object streams

        HomeActivity.logError( ReceiveCallActivity.class, "before null initialize the object streams" );
        globalServerSideObjectInputStream = null;localObjectInputStream = null;
        globalServerSideObjectOutputStream = null;localObjectOutputStream = null;
        HomeActivity.logError( ReceiveCallActivity.class, "after null initialize the object streams" );
        // 2. try to set up the streams

        // begin try to try set up the streams
        try {
            HomeActivity.logError( ReceiveCallActivity.class, "before globalServerSideObjectOutputStream = new ObjectOutputStream( localSocket.getOutputStream() )" );
            globalServerSideObjectOutputStream = new ObjectOutputStream( localSocket.getOutputStream() );
            HomeActivity.logError( ReceiveCallActivity.class, "after globalServerSideObjectOutputStream = new ObjectOutputStream( localSocket.getOutputStream() ); " +
                                                              "globalServerSideObjectOutputStream: " + globalServerSideObjectOutputStream.toString() );

            HomeActivity.logError( ReceiveCallActivity.class, "before globalServerSideObjectOutputStream.flush()" );
            // flush the output stream to send the header information
            globalServerSideObjectOutputStream.flush();
            HomeActivity.logError(ReceiveCallActivity.class, "after globalServerSideObjectOutputStream.flush()");

            HomeActivity.logError(ReceiveCallActivity.class, "before globalServerSideObjectInputStream = new ObjectInputStream( localSocket.getInputStream() )");
            globalServerSideObjectInputStream = new ObjectInputStream( localSocket.getInputStream() );
            HomeActivity.logError( ReceiveCallActivity.class, "after globalServerSideObjectInputStream = new ObjectInputStream( localSocket.getInputStream() );  " +
                                                              "globalServerSideObjectInputStream: " + globalServerSideObjectInputStream.toString() );

            localObjectOutputStream = globalServerSideObjectOutputStream;
            localObjectInputStream = globalServerSideObjectInputStream;

        } // end try to try set up the streams

        // 3. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }

    } // end method getStreams

    // begin method processRequestsFromClient
    public void processRequestsFromClient() {
HomeActivity.logError( ReceiveCallActivity.class, "just started processRequestsFromClient()" );
        String inputRequest = null;

        // begin do while to process requests from the client
        do {

            // begin try to try get the input request from the server and work on it
            try {

                // 3d. when a DISCOVER is received, get the caller's name immediately after the DISCOVER
                // 4. switch to incoming call
                // 4a. send an OFFER
                // 4b. create the Incoming Call Activity
                // 4c. pass the caller's name to the activity
                // 4d. start the Incoming Call Activity
                // 4e. reset the waiting for call text
                // 5. when a NIAMBIE tag arrives
                // 5a. immediately send a similar tag back out

                inputRequest = ( String ) localObjectInputStream.readObject();
                HomeActivity.logError( ReceiveCallActivity.class, "input request = " + inputRequest );
                // begin switch to determine how to respond
                switch ( inputRequest ) {

                    // 3d. when a DISCOVER is received, get the caller's name immediately after the DISCOVER

                    // case a DISCOVER is received
                    case HomeActivity.DISCOVER:

                        String incomingCallerName = ( String ) localObjectInputStream.readObject();

                        HomeActivity.logError(ReceiveCallActivity.class, "incoming caller name = " + incomingCallerName);

                        setCallerName(incomingCallerName);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waitingForCallTextView.append("\n" + getCallerName() + " is trying to call you. . . :-)");
                            }
                        });


                        // 4. switch to incoming call

                        // 4a. send an OFFER

                        sendDataToClient( HomeActivity.OFFER );

                        // 4b. create the Incoming Call Activity

                        Intent i = new Intent( this, IncomingCallActivity.class );

                        // 4c. pass the caller's name to the activity

                        i.putExtra(HomeActivity.CALLER_NAME, getCallerName());

                        // 4d. start the Incoming Call Activity

                        startActivity(i);

                        // 4e. reset the waiting for call text

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waitingForCallTextView.setText( R.string.rc_waiting_for_call );
                            }
                        });

                        // 4f. stop the server socket thread

                        inputRequest = HomeActivity.TERMINATE;

                        break;

                    // 5. when a NIAMBIE tag arrives

                    // case a NIAMBIE tag is received
                    case HomeActivity.NIAMBIE:

                        // 5a. immediately send a similar tag back out

                        sendDataToClient( HomeActivity.NIAMBIE );

                        break;

                } // end switch to determine how to respond

            } // end try to try get the input request from the server and work on it

            // catch I/O issues
            catch ( ClassNotFoundException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }
            catch ( IOException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }

        } while( inputRequest.equals( HomeActivity.TERMINATE ) == false ); // end do while to process requests from the client

    } // end method processRequestsFromClient

    // begin method sendDataToClient
    private void sendDataToClient( Object data ) {

        // 1. try to send data to the client(the caller)
        // 2. catch any I/O issues

        // 1. try to send data to the client(the caller)

        // begin try to try send data to the client
        try {

            localObjectOutputStream.writeObject( data );
            localObjectOutputStream.flush();

        } // end try to try send data to the client

        // 2. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }

    } // end method sendDataToClient

    // begin method closeConnection
    public void closeConnection() {

        // 1. close the output stream
        // 2. close the input stream
        // 3. close the local socket

        // begin try to try close the connection
        try {

            // 1. close the output stream

            if( localObjectOutputStream != null ) { localObjectOutputStream.close(); }

            // 2. close the input stream

            if( localObjectOutputStream != null ) { localObjectOutputStream.close(); }

            // 3. close the local socket

            if( localSocket != null ) { localSocket.close(); }

        } // end try to try close the connection

        // catch I/O issues
        catch ( IOException e ) { HomeActivity.logError( ReceiveCallActivity.class, e.getMessage() ); }

    } // end method closeConnection

    // begin method setUpSocket
    private void setUpSocket() throws IOException {

        // 1. initialize the global socket using the server socket parameter

        // 1. initialize the global socket using the server socket parameter

        globalServerSideSocket = serverSocket.accept();

    } // end method setUpSocket

    // begin method tearDownSocket
    private void tearDownSocket() {

        // 1. close the global socket

        // 1. close the global socket

        // try to close the global socket
        try { if( globalServerSideSocket != null ) { globalServerSideSocket.close(); } }

        // catch I/O issues
        catch ( IOException ioException ) { HomeActivity.logError( ReceiveCallActivity.class, ioException.getMessage() ); }

    } // end method tearDownSocket

    // begin method tearDownGlobalConnection
    // tears down the system wide connection objects
    private boolean tearDownGlobalConnection() {

        // 1. try to tear down the objects
        // 1a. tear down the I/O streams
        // 1b. tear down the socket
        // 2. catch any I/O issues

        // 1. try to tear down the objects

        // begin try to try tearing down
        try {

            // 1a. tear down the I/O streams

            if ( globalServerSideObjectOutputStream != null ) { globalServerSideObjectOutputStream.close(); }

            if( globalServerSideObjectInputStream != null ) { globalServerSideObjectInputStream.close(); }

            // 1b. tear down the socket

            if ( globalServerSideSocket != null ) { globalServerSideSocket.close(); }

            return true;

        } // end try to try tearing down

        // 2. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( MakeCallActivity.class, e.getMessage() ); return false; }

    } // end method tearDownGlobalConnection


} // end activity ReceiveCallActivity
