package com.hoskawa.myapplication_niambie;

import com.hoskawa.time.TimeTaken;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 21-07-2015.
 */

// begin class CallInfo
public class CallInfo {

    /** VARIABLES */

    /** Contacts */

    private Contact callerContact, // the caller
                    calledContact; // the called

    /** Primitives */

    private int callType, // type of call made, dependent on constants in CallLog
                callStatus; // status of call made, dependent on constants in CallLog

    /** Times */

    private Calendar callStartTime, // time of call start
                     callEndTime; // time of call end
    // these will be set at call start and end respectively

    /** CONSTRUCTOR */

    // begin constructor
    // initializes fields
    public CallInfo( Contact callerContact, Contact calledContact, int callType, int callStatus ) {

        this.callerContact = callerContact;
        this.calledContact = calledContact;
        this.callType = callType;
        this.callStatus = callStatus;

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    public Calendar getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime( Calendar callEndTime ) {
        this.callEndTime = callEndTime;
    }

    public Calendar getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime( Calendar callStartTime ) {
        this.callStartTime = callStartTime;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus( int callStatus ) {
        this.callStatus = callStatus;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType( int callType ) { this.callType = callType; }

    public Contact getCalledContact() {
        return calledContact;
    }

    public void setCalledContact( Contact calledContact ) {
        this.calledContact = calledContact;
    }

    public Contact getCallerContact() {
        return callerContact;
    }

    public void setCallerContact( Contact callerContact ) {
        this.callerContact = callerContact;
    }

    /** Other Methods */

    // begin method getCallTimeTaken
    // gets the time the call took
    public TimeTaken getCallTimeTaken() {

        long timeTaken =  getCallEndTime().getTimeInMillis() - getCallStartTime().getTimeInMillis();

        return TimeTaken.convertMillisecondsToTime( timeTaken );

    } // end method getCallTimeTaken

} // end class CallInfo
