package com.hoskawa.myapplication_niambie;

import android.net.wifi.WifiConfiguration;

import java.util.ArrayList;

/**
 * Created by user on 21-07-2015.
 */

// begin class com.hoskawa.myapplication_niambie.Contact
// stores details of a single contact
public class Contact {

    /** CONSTANTS */

    // range status constants
    public static final int NOT_APPLICABLE = -1,
                            UNREACHABLE = 0,
                            VERY_FAR = 1,
                            FAR = 2,
                            NEAR = 3,
                            VERY_NEAR = 4;

    /** VARIABLES */

    /** Array Lists */

    private ArrayList< CallLog > callLogsList; // array list of the call logs

    /** Primitives */

    private boolean vibrate; // boolean to tell if contact should have a vibrate in their ring
    private int rangeStatus; // status of contact WiFi

    /** Strings */

    private String name, // contact name
                   picturePath, // path to the contact profile picture
                   ringtonePath; // path to contact ringtone

    private WifiConfiguration contactWifiConfig; // contact IP


    /** CONSTRUCTOR */

    // begin constructor
    // no fields
    public Contact() {

        this( null, Contact.NOT_APPLICABLE, null, null, null, false, null );

    } // end constructor

    // begin constructor
    // takes fields as parameters
    public Contact( String name, int rangeStatus, String picturePath,
                    WifiConfiguration contactWifiConfig, String ringtonePath, boolean vibrate,
                    ArrayList< CallLog > callLogs ) {

        // initialize parameters
        setName( name );
        setRangeStatus( rangeStatus );
        setPicturePath( picturePath );
        setContactWifiConfig( contactWifiConfig );
        setRingtonePath( ringtonePath );
        setVibrate( vibrate );
        setCallLogsList( callLogs );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    public WifiConfiguration getContactWifiConfig() {
        return contactWifiConfig;
    }

    public void setContactWifiConfig( WifiConfiguration contactWifiConfig ) {
        this.contactWifiConfig = contactWifiConfig;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate( boolean vibrate ) {
        this.vibrate = vibrate;
    }

    public int getRangeStatus() {
        return rangeStatus;
    }

    public void setRangeStatus( int rangeStatus ) { this.rangeStatus = rangeStatus; }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getRingtonePath() {
        return ringtonePath;
    }

    public void setRingtonePath( String ringtonePath ) {
        this.ringtonePath = ringtonePath;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public ArrayList< CallLog > getCallLogsList() {
        return callLogsList;
    }

    // begin method setCallLogsList
    public void setCallLogsList( ArrayList< CallLog > callLogsList ) {

        // sort list by time in milliseconds

        // begin for loop to go through the call logs list
//        for ( int i = 0; i < callLogsList.size(); i++ ) {
//
//            // sort the call log list based on the end call time of each call log
//            SorterAndSearcher.selectionSort( ( CallLog[] )callLogsList.toArray() );
//
//        } // end for loop to go through the call logs list

        this.callLogsList = callLogsList;

    } // end method setCallLogsList

    /** Other Methods */

    // begin method getLatestCallLog
    public CallLog getLatestCallLog() {

        return getCallLogsList().get( 0 );

    } // end method getLatestCallLog

    // begin method addCallLogToList
    // adds the passed log to the list and sorts the list again
    public boolean addCallLogToList( CallLog logToInsert ) {

        getCallLogsList().add( logToInsert );
        setCallLogsList( getCallLogsList() );

        return true;

    } // end method addCallLogToList

    // begin method removeCallLogFromList
    public boolean removeCallLogFromList( CallLog logToRemove ) {

        // TODO-me hiw2 remove call log from list
        return true;

    } // end method removeCallLogFromList

    // begin method getRangeNameFromRangeStatus
    public static String getRangeNameFromRangeStatus( int range ) {

        String[] rangeStrings = { "NOT_APPLICABLE",
                                  "Unreachable",
                                  "Very Far",
                                  "Far",
                                  "Near",
                                  "Very Near" };

        return rangeStrings[ range ];

    } // end method getRangeNameFromRangeStatus

} // end class Contact
