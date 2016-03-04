package com.hoskawa.myapplication_niambie;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// begin activity MakeCallActivity
// is the activity for when the user wants to make a call
public class MakeCallActivity extends ListActivity {

    // TODO-me done HOW TO MAKE A FRAGMENT BLOCK ITS PARENT ACTIVITY'S EXECUTION
    // TODO-me done TAKE CARE OF RECORDING BUTTON CLICKED TWICE
    // TODO-me      TEST WIFI HOTSPOT IDEA
    // TODO-me      TEST RECORDING AND SENDING FOR BOTH Huawei AND Samsung
    // TODO-me      KNOW HOW THE IMAGES WILL GO
    // TODO-me      HOME

    /** VARIABLES */

    /**
     * Array Lists
     */

//    private ArrayList<Contact> contacts; // the contacts visible

    /** Broadcast Receivers */

    private BroadcastReceiver wiFiStateChangeReceiver; // the receiver to catch wifi state changes

    /**
     * Buttons
     */

    private Button scanButton; // button to let the user scan for other users around

    /** Object I/O Streams */

    public static ObjectInputStream globalClientSideObjectInputStream; // the object input stream used throughout the doing the calling process

    public static ObjectOutputStream globalClientSideObjectOutputStream; // the object output stream used throughout the doing the calling process

    /** Search For Contacts Threads */

    private SearchForContactsThread searchForContactsThread; // the thread used to search for contacts

    /**
     * Sockets
     */

    public static Socket globalClientSideSocket; // the socket used throughout the doing the calling process -> initialized in SetUpGlobalSocketAsyncTask's onPostExecute

    /** Strings */

    private String callerName; // the name of the caller

    /** METHODS */

    /** Getters and Setters */

    // getter for the caller's name
    private String getCallerName() { return callerName; }

    // setter for the caller's name
    public void setCallerName( String callerName ) { this.callerName = callerName; }

    /**
     * Overrides
     */

    @Override
    // begin method onCreate
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_make_call_with_contacts );

//        contacts = new ArrayList<Contact>();

        // 0. initialize the UI
        // 1. show a list of available hotspots
        //    user will select the correct hotspot
        // 1a. get the name of the user's hotspot
        // 1b. initialize the search for contacts thread
        // 1c. register wifi state change broadcast receiver
        // 1d. turn on the wifi
        // 2a. initialize the system wide socket
        // 3. we start call functions
        // 3a. by switching to the calling activity

        // 0. initialize the UI

        initializeUI();

        // 1a. get the name of the user's hotspot

        HotspotUtilsThread hotspotUtilsThread = new HotspotUtilsThread( this );
        hotspotUtilsThread.setHotspotState( HotspotUtilsThread.GET_SSID );
        hotspotUtilsThread.start();

        // 1b. initialize the search for contacts thread

        searchForContactsThread = new SearchForContactsThread( this, scanButton );

        // 1c. register wifi state change broadcast receiver

        wiFiStateChangeReceiver = new WiFiStateChangeReceiver( searchForContactsThread );

        registerReceiver( wiFiStateChangeReceiver, new IntentFilter( WifiManager.WIFI_STATE_CHANGED_ACTION ) );

        // 1d. turn on the wifi

        WifiManager wifiManager = ( WifiManager ) getSystemService( WIFI_SERVICE );
        if( wifiManager.isWifiEnabled() == false ) { wifiManager.setWifiEnabled( true ); }

        // 2. we connect to that hotspot <- done in the contacts array adapter

        // 2a. initialize the system wide socket <- done in the contacts array adapter

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

//    @Override
//    // begin method onPause
//    protected void onPause() {
//        super.onPause();
//    } // end method onPause

    @Override
    // begin method onDestroy
    protected void onDestroy() {

        super.onDestroy();

        // 1. turn the wifi off
        // 2. unregister receivers
        // 3. tear down global connection

        // 1. turn the wifi off

//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager.isWifiEnabled() == true) { wifiManager.setWifiEnabled(false); }

        // 2. unregister receivers

        unregisterReceiver(wiFiStateChangeReceiver);

        searchForContactsThread = null;

        // 3. tear down global connection

        tearDownGlobalConnection();

    } // end method onDestroy

    // begin method getContactsFromAvailableHotspots
    public ArrayList< Contact > getContactsFromAvailableHotspots() {

        // 1. initialize the wifi manager
//        // 2. activate wifi station mode
        // 3. scan for networks
        // 4. get a list of scan results
        // 5. use the scan results to populate the contacts
        // 6. populate the list view with the contacts just acquired

        // these will be done on a separate thread

        ArrayList< Contact > contacts = new ArrayList< Contact >();

        // 1. initialize the wifi manager

        final WifiManager wifiManager = (WifiManager) getSystemService( Context.WIFI_SERVICE );

        HomeActivity.toastInformation(this, "wifiManager.isWifiEnabled() == " + wifiManager.isWifiEnabled());

        // 3. scan for networks

        // begin try to start an active scan
        try {

            Method startScanActiveMethod = wifiManager.getClass().getMethod( "startScanActive" );

            startScanActiveMethod.invoke( wifiManager );

        } // end try to start an active scan

        // catch any exceptions
        catch ( Exception exception ) { HomeActivity.logError( MakeCallActivity.class, exception.getMessage() ); }

        // 4. get a list of scan results

        List< ScanResult > results = wifiManager.getScanResults();

        // 5. use the scan results to populate the contacts

        // begin if to check if the scan results are available
        if ( results != null ) {

            // begin for to go through the scan results
            // use each scan result to get a contact
            for (int i = 0; i < results.size(); i++) {

                Contact newContact = new Contact();

                ScanResult currentResult = results.get( i );

                newContact.setName( currentResult.SSID );
                newContact.setRangeStatus( WifiManager.calculateSignalLevel(currentResult.level, 5 ) );

                contacts.add( newContact );

            } // end for to go through the scan results

        } // end if to check if the scan results are available

        contacts.trimToSize();

        return contacts;

        // 6. populate the list view with the contacts just acquired

//        populateListViewWithContacts();

    } // end method getContactsFromAvailableHotspots

    // begin method populateListViewWithContacts
    public void populateListViewWithContacts( ArrayList< Contact > gottenContacts ) {

        // 1. pass context and data to the custom adapter
        // 2. set list adapter

        // 1. pass context and data to the custom adapter

        ContactsArrayAdapter contactsArrayAdapter = new ContactsArrayAdapter( this, gottenContacts, getCallerName() );

        // 2. set list adapter

        setListAdapter( contactsArrayAdapter );

    } // end method populateListViewWithContacts

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

            if ( globalClientSideObjectOutputStream != null ) { globalClientSideObjectOutputStream.close(); }

            if( globalClientSideObjectInputStream != null ) { globalClientSideObjectInputStream.close(); }

            // 1b. tear down the socket

            if ( globalClientSideSocket != null ) { globalClientSideSocket.close(); }

            return true;

        } // end try to try tearing down

        // 2. catch any I/O issues

        catch ( IOException e ) { HomeActivity.logError( MakeCallActivity.class, e.getMessage() ); return false; }

    } // end method tearDownGlobalConnection

    // begin method initializeUI
    private void initializeUI() {

        populateListViewWithContacts( new ArrayList< Contact >() );

        scanButton = ( Button ) findViewById( R.id.mcwc_b_scan );

        // begin method scanButton.setOnClickListener
        scanButton.setOnClickListener(

                // begin class View.OnClickListener
                new View.OnClickListener() {

                    // begin method onClick
                    @Override
                    public void onClick(View v) {

                        // 0a. show that we are scanning
                        // 0b. disable the scan button

                        // 0a. show that we are scanning

                        scanButton.setText( R.string.mcwc_scanning );

                        // 0b. disable the scan button

                        scanButton.setEnabled( false );

                        // 1b. get the contacts from available hotspots
                        searchForContactsThread.run();

             } // end method onClick

                } // end class View.OnClickListener

        ); // end method scanButton.setOnClickListener

    } // end method initializeUI

} // end activity MakeCallActivity
