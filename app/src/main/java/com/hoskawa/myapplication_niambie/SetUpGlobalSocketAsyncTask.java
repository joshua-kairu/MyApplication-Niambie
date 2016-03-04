package com.hoskawa.myapplication_niambie;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by joshua on 1/28/16.
 */

// begin class SetUpGlobalSocketAsyncTask
public class SetUpGlobalSocketAsyncTask extends AsyncTask< String, Void, Boolean > {

    // 1. set up a socket
    // 2. initialize the Making Call Activity socket with the made socket

    /** CONSTANTS */

    /** VARIABLES */

    /** Primitives */

    private boolean done; // boolean to tell if execution is done

    /** Server Sockets */

    private ServerSocket aServerSocket; // the server socket that is needed for receiving a call

    /** Sockets */

    private Socket aSocket; // a socket we will set up

    /** Strings */

    private String activityIdentifier; // string that identifies which activity called this task, and thus which code to execute

    /** CONSTRUCTORS */

    // begin constructor
    public SetUpGlobalSocketAsyncTask( ServerSocket aServerSocket, String activityIdentifier ) {

        this.aServerSocket = aServerSocket;

        this.activityIdentifier = activityIdentifier;

        setDone( false );

        HomeActivity.logError(SetUpGlobalSocketAsyncTask.class, "end constructor");
    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the done state
    public boolean isDone() { return done; }

    // setter for the done state
    public void setDone( boolean done ) { this.done = done; }

    /** Overrides */

    // 1. set up a socket

    @Override
    // begin method doInBackground
    protected Boolean doInBackground( String... params ) {

        HomeActivity.logError( SetUpGlobalSocketAsyncTask.class, "protected Boolean doInBackground( String... params )" );
        // begin switch to determine which socket to set up
        switch ( activityIdentifier ) {

            // case make call activity
            // set a socket from a remote server
            case HomeActivity.MAKE_CALL_ACTIVITY:

                return setSocketFromRemoteServer( params );

            // case receive call activity
            // set a socket from the server socket accepting a socket connection
            case HomeActivity.RECEIVE_CALL_ACTIVITY:

                return setSocketFromServerSocket();

            default:

                return false;

        } // end switch to determine which socket to set up

    } // end method doInBackground

    // 2. initialize the Making Call Activity socket with the made socket

    @Override
    // begin method onPostExecute
    protected void onPostExecute( Boolean aBoolean ) {

        super.onPostExecute( aBoolean );

        if( activityIdentifier.equals( HomeActivity.MAKE_CALL_ACTIVITY ) == true ) { MakeCallActivity.globalClientSideSocket = aSocket; }

        else if( activityIdentifier.equals( HomeActivity.RECEIVE_CALL_ACTIVITY ) == true ) {

            Log.e("onPostExecute", "onPostExecute: activityIdentifier.equals(HomeActivity.RECEIVE_CALL_ACTIVITY ) == true" );
            ReceiveCallActivity.globalServerSideSocket = aSocket;

            HomeActivity.logError(SetUpGlobalSocketAsyncTask.class, "ReceiveCallActivity.globalClientSideSocket = " + ReceiveCallActivity.globalServerSideSocket.toString());
            setDone(true);

            aServerSocket.notifyAll();
        }

    } // end method onPostExecute

    /** Other Methods */

    // begin method setSocketFromRemoteServer
    private boolean setSocketFromRemoteServer( String ... params ) {

        String ipAddress = params[ 0 ];

        int portNumber = Integer.parseInt( params[ 1 ] );

        // try to set up the socket

        try { aSocket = new Socket( InetAddress.getByName( ipAddress ), portNumber ); return true; }

        // 3. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( MakeCallActivity.class, e.getMessage() ); return false; }

    } // end method setSocketFromRemoteServer

    // begin method setSocketFromServerSocket
    private boolean setSocketFromServerSocket() {

        // try to get a socket from the server socket
        try {
            HomeActivity.logError( SetUpGlobalSocketAsyncTask.class, "waiting for an accept" );
            aSocket = aServerSocket.accept();
            HomeActivity.logError( SetUpGlobalSocketAsyncTask.class, "gotten an accept" );
            return true; }

        // catch I/O issues
        catch ( IOException ioException ) { HomeActivity.logError( ReceiveCallActivity.class, ioException.getMessage() ); return false; }

    } // end method setSocketFromServerSocket

} // end class SetUpGlobalSocketAsyncTask
