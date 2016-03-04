package com.hoskawa.myapplication_niambie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * Created by joshua on 1/29/16.
 */

// begin class WiFiStateChangeReceiver
public class WiFiStateChangeReceiver extends BroadcastReceiver {

    /** CONSTANTS */

    /** VARIABLES */

    /** Search For Contacts Threads */

    private SearchForContactsThread searchForContactsThread; // thread to search for contacts

    /** CONSTRUCTORS */

    // constructor
    public WiFiStateChangeReceiver( SearchForContactsThread searchForContactsThread ) { this.searchForContactsThread = searchForContactsThread; }

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin method onReceive
    public void onReceive( Context context, Intent intent ) {

        // 1. get the state of the Wifi

        int extraWifiState = intent.getIntExtra( WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN );

        // 2. if the Wifi is enabled, then search for contacts

        // begin switch to determine what to do
        switch ( extraWifiState ) {

            // case for when the Wifi is enabled
            case WifiManager.WIFI_STATE_ENABLED:

                searchForContactsThread.start();

                break;

        } // end switch to determine what to do

    } // end method onReceive

    /** Other Methods */

} // end class WiFiStateChangeReceiver
