package com.hoskawa.myapplication_niambie;

import android.os.Process;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by joshua on 1/28/16.
 */

// begin class SearchForContactsThread
public class SearchForContactsThread extends Thread {

    // searches for contacts

    /** CONSTANTS */

    /** VARIABLES */

    /** Buttons */

    private Button scanButton; // the scan button

    /**  Make Call Activities */

    private MakeCallActivity makeCallActivity; // the make call activity

    /** CONSTRUCTORS */

    // constructor
    public SearchForContactsThread( MakeCallActivity makeCallActivity, Button scanButton ) { this.makeCallActivity = makeCallActivity; this.scanButton = scanButton; }

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin method run
    public void run() {

        super.run();

        // 0. set process to background priority
        // 1. search for contacts
        // 2. populate the list view with contacts

        // 0. set process to background priority

        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // begin method makeCallActivity.runOnUiThread
        makeCallActivity.runOnUiThread(

                // begin Runnable
                new Runnable() {

                    @Override
                    // begin method run
                    public void run() {

                        // 0a. show that we have started scanning
                        // 0b. disable the scan button

                        // 0a. show that we have started scanning

                        scanButton.setText( R.string.mcwc_scanning );

                        // 0b. disable the scan button

                        scanButton.setEnabled(false );

                    } // end method run

                } // end Runnable

        ); // end method makeCallActivity.runOnUiThread

        // 1. search for contacts

        final ArrayList< Contact > contacts = makeCallActivity.getContactsFromAvailableHotspots();

        // 2. populate the list view with contacts

        // begin method makeCallActivity.runOnUiThread
        makeCallActivity.runOnUiThread(

            // begin Runnable
            new Runnable() {

                @Override
                // begin method run
                public void run() {

                    makeCallActivity.populateListViewWithContacts( contacts );

                    // 0a. show that we have finished scanning
                    // 0b. enable the scan button

                    // 0a. show that we have finished scanning

                    scanButton.setText( R.string.mcwc_scan );

                    // 0b. enable the scan button

                    scanButton.setEnabled( true );

                } // end method run

            } // end Runnable

        ); // end method makeCallActivity.runOnUiThread

    } // end method run

    /** Other Methods */

} // end class SearchForContactsThread
