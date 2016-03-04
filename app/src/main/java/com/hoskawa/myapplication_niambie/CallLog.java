package com.hoskawa.myapplication_niambie;

/**
 * Created by user on 22-07-2015.
 */

// begin class com.hoskawa.myapplication_niambie.CallLog
// a log entry of a call made/received
// implements Comparable so that it can be sorted by time of call end in milliseconds
// overrides equals so that comparison is made based on time of call end in milliseconds
// Comparable's compareTo should be consistent with equals
public class CallLog implements Comparable {

    /** CONSTANTS */

    // constants for call type
    public static final String CALL_TYPE_INCOMING = "Incoming Call",
                               CALL_TYPE_OUTGOING = "Outgoing Call",
                               CALL_TYPE_MISSED = "Missed Call",

    // constants for call status
                               CALL_STATUS_SUCCESSFUL = "Successful",
                               CALL_TYPE_CANCELLED = "Cancelled";

    /** VARIABLES */

    /** Call Infos */

    private CallInfo callInfo; // call information about the current log
    //sort out call log call info discrepancies

    /** CONSTRUCTORS */

    // begin constructor
    // initializes parameters
    public CallLog( CallInfo callInfo ) {

        setCallInfo( callInfo );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    public CallInfo getCallInfo() {
        return callInfo;
    }

    public void setCallInfo( CallInfo callInfo ) {
        this.callInfo = callInfo;
    }

    /** Overrides */
    @Override
    // begin compareTo
    public int compareTo( Object another ) {

        // How compareTo works
        // Returns a negative integer - less than,
        // 		   zero - equal to,
        //		   a positive integer - greater than
        // the specified object.

        // try to cast object to call log
        // catch a class cast problem and return -2 when it happens
        CallLog anotherLog = null;
        try { anotherLog = ( CallLog ) another; }
        catch ( ClassCastException e ) { e.printStackTrace(); return -2; }

        // do the comparison
        if( getCallInfo().getCallEndTime().getTimeInMillis() < anotherLog.getCallInfo().getCallEndTime().getTimeInMillis() ) { return  -1; }

        else if ( getCallInfo().getCallEndTime().getTimeInMillis() == anotherLog.getCallInfo().getCallEndTime().getTimeInMillis() ) { return 0; }

        else { return 1; }

    } // end compareTo

    /** Other Methods */

} // end class com.hoskawa.myapplication_niambie.CallLog
