/**
 *  Original idea by Whitebyte(Nick Russler, Ahmet Yueksektepe).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * */

package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by user on 18-10-2015.
 */

// begin class WiFiHotspotManager
public class WiFiHotspotManager {

    /** CONSTANTS */

    /** VARIABLES */

    /** Contexts */

    private Context context; // the app's context

    /** Wifi Managers */

    private final WifiManager wifiManager; // the context's Wifi Manager

    /** CONSTRUCTORS */

    // begin constructor
    public WiFiHotspotManager( Context context ) {

        // get the app's context
        this.context = context;

        // get the app's WiFi manager
        wifiManager = ( WifiManager ) this.context.getSystemService( Context.WIFI_SERVICE );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    /** Other Methods */

    // begin method enableWifiHotspot
    // enables the Wifi hotspot using the specified Wifi configurations
    public boolean enableWifiHotspot( WifiConfiguration wifiConfiguration, boolean wifiStationModeEnabled ) {

        // 1. disable the Wifi station mode (station mode is the mode used when a Wifi device wants to connect to a hotspot)
        // 2. enable the Wifi hotspot or access point mode (hotspot or access point mode is the mode used when a Wifi device wants to be a hotspot)

        // 1. disable the Wifi station mode

        // check if Wifi station mode is on and if so disable it
        if( wifiStationModeEnabled == true ) { wifiManager.setWifiEnabled( false ); }

        // 2. enable the Wifi hotspot or access point mode

        // begin try to try set up the hotspot
        try {

            // 2a. get from the Wifi manager via reflection the method to set up the hotspot
            // 2b. invoke that method using the Wifi configuration, and the station mode status as parameters

            // 2a. get via reflection the method to set up the hotspot

            Method method = wifiManager.getClass().getMethod( "setWifiApEnabled", WifiConfiguration.class, boolean.class );

            // 2b. invoke that method using the Wifi manager, Wifi configuration, and the station mode status as parameters

            return ( Boolean ) method.invoke( wifiManager, wifiConfiguration, wifiStationModeEnabled );

        } // end try to try set up the hotspot

        // catch exceptions and return false
        catch ( Exception exception ) { Log.e( this.getClass().toString(), "", exception ); return false; }

    } // end method enableWifiHotspot

    // begin method getWifiHotspotState
    // gets the enabled state of the hotspot
    public WIFI_HOTSPOT_STATE getWifiHotspotState() {

        HotspotUtilsThread.setFinished( false );

        // 1. get Wifi hotspot state from the Wifi manager via reflection
        // 2. account for Android 4.x's Wifi state numbers (of course this is very device specific and should be made more abstract)
        // 3. return the hotspot state

        final WIFI_HOTSPOT_STATE[] hotspotState = new WIFI_HOTSPOT_STATE[ 1 ];

        // begin class runnable
        Runnable runnable = new Runnable() {

            // begin method run
            @Override
            public void run() {

                // 1. get Wifi hotspot state from the Wifi manager via reflection

                // begin try to reflect hotspot state
                try {

                    Method method = wifiManager.getClass().getMethod( "getWifiApState" );

                    // 2. account for Android 4.x's Wifi state numbers (of course this is very device specific and should be made more abstract)

                    // invoke the method to get an integer return value for the wifi hotspot state
                    int tempHotspotState = ( Integer ) method.invoke( wifiManager );

                    if( tempHotspotState >= 10 ) { tempHotspotState = tempHotspotState - 10; }

                    // 3. return the hotspot state

                    hotspotState[ 0 ] = WIFI_HOTSPOT_STATE.class.getEnumConstants()[ tempHotspotState ];

                    HotspotUtilsThread.setFinished( true );

                } // end try to reflect hotspot state

                // catch exceptions and return failed
                catch ( Exception exception ) { Log.e( this.getClass().toString(), "", exception ); hotspotState[ 0 ] = WIFI_HOTSPOT_STATE.WIFI_AP_STATE_FAILED; HotspotUtilsThread.setFinished( true ); }

            } // end method run

        }; // end class runnable

        runnable.run();

        return hotspotState[ 0 ];

    } // end method getWifiHotspotState

    // begin method itsOKToStartHotspot
    // tells if the hotspot is enabled or not
    public boolean isWifiHotspotEnabled() { return getWifiHotspotState() == WIFI_HOTSPOT_STATE.WIFI_AP_STATE_ENABLED; } // end method itsOKToStartHotspot

    // begin method getWifiHotspotConfiguration
    // gets the hotspot configuration
    public WifiConfiguration getWifiHotspotConfiguration() {

        HotspotUtilsThread.setFinished( false );

        final WifiConfiguration[] hotspotConfiguration = new WifiConfiguration[ 1 ];

        // begin class runnable
        Runnable runnable = new Runnable() {

            // begin method run
            @Override
            public void run() {

                // 1. get the hotspot configuration via reflection
                // 2. return the gotten hotspot configuration

                // 1. get the hotspot configuration via reflection

                // begin try to try reflect
                try {

                    Method method = wifiManager.getClass().getMethod( "getWifiApConfiguration" );

                    // 2. return the gotten hotspot configuration

                    hotspotConfiguration[ 0 ] = ( WifiConfiguration ) method.invoke( wifiManager );

                    HotspotUtilsThread.setFinished( true );

                } // end try to try reflect

                // catch exceptions and return null
                catch ( Exception exception ) { Log.e( this.getClass().toString(), "", exception ); hotspotConfiguration[ 0 ] = null; HotspotUtilsThread.setFinished( true ); }

            } // end method run

        }; // end class runnable

        runnable.run();

        return hotspotConfiguration[ 0 ];

    } // end method getWifiHotspotConfiguration

    // begin method setWifiHotspotConfiguration
    // sets the hotspot configuration and returns true if the process is successful
    public boolean setWifiHotspotConfiguration( final WifiConfiguration wifiConfiguration ) {

        HotspotUtilsThread.setFinished( false );

        final boolean[] hotspotState = new boolean[ 1 ];

        // begin class runnable
        Runnable runnable = new Runnable() {

            // begin method run
            @Override
            public void run() {

                // 1. set the hotspot configuration via reflection
                // 2. return the state of configuration setup success

                // 1. set the hotspot configuration via reflection

                // begin try to try reflect
                try {

                    Method method = wifiManager.getClass().getMethod( "setWifiApConfiguration", WifiConfiguration.class );

                    // 2. return the state of configuration setup success

                    hotspotState[ 0 ] = ( Boolean ) method.invoke( wifiManager, wifiConfiguration );

                    HotspotUtilsThread.setFinished( true );

                } // end try to try reflect

                // catch exceptions and return false
                catch ( Exception exception ) { Log.e( this.getClass().toString(), "", exception );  HotspotUtilsThread.setFinished( true ); }

            } // end method run

        }; // end class runnable

        runnable.run();

        return hotspotState[ 0 ];

    } // end method setWifiHotspotConfiguration

    // begin method getHotspotPassword
    // gets the password of the hotspot
    public String getHotspotPassword() {

        // 1. get the hotspot's wifi configuration via reflection
        // 2. return the configuration's password

        // begin try to try reflect
        try {

            // 1. get the hotspot's wifi configuration via reflection

            Method getWifiConfigurationMethod = wifiManager.getClass().getMethod( "getWifiApConfiguration" );

            WifiConfiguration hotspotWifiConfiguration = ( WifiConfiguration ) getWifiConfigurationMethod.invoke( wifiManager );

            // 2. return the configuration's password

            return hotspotWifiConfiguration.preSharedKey;

        } // end try to try reflect

        // catch exceptions and return null
        catch ( Exception exception ) { return null; }

    } // end method getHotspotPassword

    // begin method setHotspotPassword
    // changes the hotspot's password to the one in the parameters
    public boolean setHotspotPassword( Activity activity, String newPassword ) {

        // 1. get the hotspot's wifi configuration via reflection
        // 2. change the gotten wifi configuration's password
        // 3. set the hotspot wifi configuration to be the modified wifi configuration

        // begin try to try reflect
        try {

            // 1. get the hotspot's wifi configuration via reflection

            Method getWifiConfigurationMethod = wifiManager.getClass().getMethod( "getWifiApConfiguration" );

            // 2. change the gotten wifi configuration's password

            WifiConfiguration hotspotWifiConfiguration = ( WifiConfiguration ) getWifiConfigurationMethod.invoke( wifiManager );

            hotspotWifiConfiguration.preSharedKey = newPassword;

            // 3. set the hotspot wifi configuration to be the modified wifi configuration

            Method setWifiConfigurationMethod = wifiManager.getClass().getMethod( "setWifiApConfiguration", WifiConfiguration.class );

            return ( Boolean ) setWifiConfigurationMethod.invoke( wifiManager, hotspotWifiConfiguration );

        } // end try to try reflect

        // catch exceptions and return false
        catch ( Exception exception ) { return false; }

    } // end method setHotspotPassword

} // end class WiFiHotspotManager
