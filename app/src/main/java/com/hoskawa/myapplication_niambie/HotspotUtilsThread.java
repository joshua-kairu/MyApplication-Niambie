package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.*;
import android.os.Process;
import android.util.Log;

/**
 * Created by user on 20-10-2015.
 */

// begin class HotspotUtilsThread
public class HotspotUtilsThread extends Thread {

    /** CONSTANTS */

    /** Strings */

    // constants to govern what to do with the hotspot
    public static final String GET_PASSWORD = "GET_PASSWORD",
                               SET_PASSWORD = "SET_PASSWORD",
                               START_HOTSPOT = "START_HOTSPOT",
                               STOP_HOTSPOT = "STOP_HOTSPOT",
                               GET_SSID = "GET_SSID";

    /** VARIABLES */

    /** Arrays */

    private Object[] parameters; // the parameters to be used in the various functions

    /** Activities */

    private Activity activity; // the activity within which this AsyncTask will work

    /** Primitives */

    private static boolean finished = false; // boolean to tell if the current thread has finished working

    /** Progress Dialogs */

    private ProgressDialog progressDialog; // progress dialog to show user what is happening

    /** Strings */

    private String hotspotState; // String to hold the state of the hotspot

    /** Wifi Hotspot Managers */

    protected WiFiHotspotManager wiFiHotspotManager; // Wifi hotspot manager

    /** CONSTRUCTORS */

    // begin constructor
    public HotspotUtilsThread( Activity activity ) {

        this.activity = activity;

        progressDialog = new ProgressDialog( this.activity );

        wiFiHotspotManager = new WiFiHotspotManager( this.activity );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the hotspot state
    public String getHotspotState() { return hotspotState; }

    // setter for the hotspot state
    public void setHotspotState( String hotspotState ) { this.hotspotState = hotspotState; }

    // getter for if the thread has completed
    public static boolean isFinished() { return finished; }

    // setter for if the thread has completed
    public static void setFinished( boolean finished ) { HotspotUtilsThread.finished = finished; }

    // getter for the parameters
    private Object[] getParameters() { return parameters; }

    // setter for the parameters
    public void setParameters( Object[] parameters ) { this.parameters = parameters; }


    // begin method itsOKToStartHotspot
    // tells if it is okay to start the hotspot
    // its okay to start the hotspot only when the hotspot is not enabling and is not enabled
    // begin method itsOKToStartHotspot
    public boolean itsOKToStartHotspot() {

        Log.e( getClass().getSimpleName() + " itsOKToStartHotspot", wiFiHotspotManager.getWifiHotspotState().toString() );
        return ( wiFiHotspotManager.getWifiHotspotState() != WIFI_HOTSPOT_STATE.WIFI_AP_STATE_ENABLED )
               &&
               ( wiFiHotspotManager.getWifiHotspotState() != WIFI_HOTSPOT_STATE.WIFI_AP_STATE_ENABLING );

    } // end method itsOKToStartHotspot

    public WIFI_HOTSPOT_STATE getWifiHotspotState() { return wiFiHotspotManager.getWifiHotspotState(); }

    /** Overrides */

    // begin method run
    @Override
    public void run() {

        super.run();

        // 0. set process to background priority
        // 1. set up and show the progress dialog
        // 2. determine what to do to the hotspot
        // 3. tell the user the final state of the hotspot

        // 0. set process to background priority

        Process.setThreadPriority( Process.THREAD_PRIORITY_BACKGROUND );

        // 1. set up and show the progress dialog

        String message = getHotspotState();
//        progressDialog.setMessage( getHotspotState() );
//        progressDialog.show();

        // 2. determine what to do to the hotspot

        // begin switch to determine what to do to the hotspot
        switch ( getHotspotState() ) {

            // case get password
            case GET_PASSWORD:

                // 1. get the current hotspot password
                // 2. if the activity is the home activity, call its onGetPasswordListener so as to give the activity the password

                // 1. get the current hotspot password

//                String currentPassword = wiFiHotspotManager.getHotspotPassword();

                // 2. if the activity is the home activity, call its onGetPasswordListener so as to give the activity the password

//                if( activity instanceof MakeCallActivity) { ( (MakeCallActivity) activity ).onGetUserPassword( currentPassword ); message = "Hotspot password gotten. . ."; }

//                else { message = "Hotspot NOT password gotten. . ."; }

                break;

            // case set password
            case SET_PASSWORD:

                // 1. get the new password that is to be set
                // 2. call the wifi hotspot manager to change the password

                // 1. get the new password that is to be set

                // the new password will be element zero of the parameters

                String newPassword = ( String ) getParameters()[ 0 ];

                // 2. call the wifi hotspot manager to change the password

                wiFiHotspotManager.setHotspotPassword( activity, newPassword );

                message = "Hotspot password set and changed. . .";

                break;

            // case start hotspot
            case START_HOTSPOT:
HomeActivity.toastInformation(activity, "starting to set up hotspot");
                // 1. start the hotspot

                // 1. start the hotspot

                wiFiHotspotManager.enableWifiHotspot( null, ( ( WifiManager ) activity.getSystemService( Context.WIFI_SERVICE ) ).isWifiEnabled() );
                HomeActivity.toastInformation(activity, "ending to set up hotspot");

                break;

            // case stop hotspot
            case STOP_HOTSPOT:

                // 1. enable station mode so as to disable hotspot mode
                // 2. disable station mode

                WifiManager wifiManager = ( WifiManager ) activity.getSystemService( Context.WIFI_SERVICE );

                // 1. enable station mode so as to disable hotspot mode

                wifiManager.setWifiEnabled( true );

                // 2. disable station mode

                wifiManager.setWifiEnabled( false );

                break;

            // case get SSID
            case GET_SSID:

                ( ( MakeCallActivity )activity ).setCallerName( getHotspotSSID() );

                break;

        } // end switch to determine what to do to the hotspot

        // 3. tell the user the final state of the hotspot

        // 3a. dismiss the progress dialog
        // 3b. tell the user the hotspot is set up

        // 3a. dismiss the progress dialog

//        if( progressDialog.isShowing() == true ) { progressDialog.dismiss(); }

        // 3b. tell the user the hotspot is set up

        Log.i( getClass().toString(), message );

    } // end method run

    /** Other Methods */

    // begin method getHotspotSSID
    private String getHotspotSSID() { return wiFiHotspotManager.getWifiHotspotConfiguration().SSID; } // end method getHotspotSSID

} // end class HotspotUtilsThread
