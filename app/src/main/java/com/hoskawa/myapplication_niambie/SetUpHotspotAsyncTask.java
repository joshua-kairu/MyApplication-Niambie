package com.hoskawa.myapplication_niambie;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by user on 26-12-2015.
 */

// begin class SetUpHotspotAsyncTask
public class SetUpHotspotAsyncTask extends AsyncTask< Boolean, Void, Boolean > { // < parameter type for doInBackground, onProgressUpdate, parameter type for onPostExecute >

    /** CONSTANTS */

    /** VARIABLES */

    /** Contexts */

    private Context context; // the context in which this task works

    /** Progress Dialogs */

    private ProgressDialog progressDialog; // a progress dialog to inform the user

    /** CONSTRUCTORS */

    // begin constructor
    public SetUpHotspotAsyncTask( Context context ) {

        this.context = context;
        progressDialog = new ProgressDialog( this.context );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */

    @Override
    // begin method onPreExecute
    protected void onPreExecute() {

        // 1. set an appropriate message in the progress dialog
        // 2. display the progress dialog

        super.onPreExecute();

        // 1. set an appropriate message in the progress dialog

        progressDialog.setMessage( context.getString( R.string.progress_dialog_setting_up ) );

        // 2. display the progress dialog

        progressDialog.show();

    } // end method onPreExecute

    @Override
    // begin method doInBackground
    // params[ 0 ] - the state of the wifi station mode
    protected Boolean doInBackground( Boolean[] params ) {

        // 1. initialize a wifi hotspot manager
        // 2. enable wifi hotspot

        // 1. initialize a wifi hotspot manager

        WiFiHotspotManager wiFiHotspotManager = new WiFiHotspotManager( context );

        // 2. enable wifi hotspot

        return wiFiHotspotManager.enableWifiHotspot( null, params[ 0 ] );

    } // end method doInBackground

    @Override
    // begin method onPostExecute
    protected void onPostExecute( Boolean aBoolean ) {

        // 1. dismiss the progress dialog

        super.onPostExecute( aBoolean );

        // 1. dismiss the progress dialog

        if( progressDialog.isShowing() == true ) { progressDialog.dismiss(); }

    } // end method onPostExecute

    /** Other Methods */

} // end class SetUpHotspotAsyncTask
