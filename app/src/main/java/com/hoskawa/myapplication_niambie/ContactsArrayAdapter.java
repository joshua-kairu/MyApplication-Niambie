package com.hoskawa.myapplication_niambie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 20-10-2015.
 */

// begin class ContactsArrayAdapter
// adapter to get arrays from the contact array list to the relevant list view
public class ContactsArrayAdapter extends ArrayAdapter< Contact > {

    /** CONSTANTS */

    /** VARIABLES */

    /** Array Lists */

    private ArrayList< Contact > contacts; // the contacts

    /** Activities */

    private Activity activity; // the current activity

    /** Strings */

    private String callerName; // the name of the caller

    /** CONSTRUCTORS */

    // begin constructor
    public ContactsArrayAdapter( Activity activity, ArrayList< Contact > contacts, String callerName ) {

        // pass to the super class the
        //  current context
        //  resource ID for the layout file that will be used to initialize the individual views in the list view
        //  array list that has the objects we want to see in the list view - in this case the contacts
        super( activity, R.layout.individual_contact_in_list, contacts );

        this.activity = activity;
        this.contacts = contacts;

        setCallerName( callerName );

    } // end constructor

    /** METHODS */

    /** Getters and Setters */

    // getter for the caller's name
    private String getCallerName() { return callerName; }

    // setter for the caller's name
    private void setCallerName( String callerName ) { this.callerName = callerName; }

    /** Overrides */

    // begin method getView
    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {

        // 1. create inflater
        // 2. get rowView from inflater
        // 3. get UI items from rowView
        // 4. initialize UI items from rowView

        // 1. create inflater

        LayoutInflater layoutInflater = ( LayoutInflater ) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // 2. get rowView from inflater

        View rowView = layoutInflater.inflate( R.layout.individual_contact_in_list, parent, false );

        // 3. get UI items from rowView

        ImageView pictureImageView = ( ImageView ) rowView.findViewById( R.id.icil_iv_individual_contact_icon );

        TextView contactNameTextView = ( TextView ) rowView.findViewById( R.id.individual_contact_name ),

                 rangeStatusTextView = ( TextView ) rowView.findViewById( R.id.individual_contact_range_status );

        ImageButton callImageButton = ( ImageButton ) rowView.findViewById( R.id.icil_ib_call_button );

        // 4. initialize UI items from rowView

        pictureImageView.setImageResource( R.drawable.ic_blank_profile_48x48 );
        contactNameTextView.setText( contacts.get( position ).getName() );
        rangeStatusTextView.setText( Contact.getRangeNameFromRangeStatus( contacts.get( position ).getRangeStatus() ) );

        // begin method callImageButton.setOnClickListener
        // is where the calling will take place
        // to be implemented later
        callImageButton.setOnClickListener(

                // begin class View.OnClickListener
                new View.OnClickListener() {

                    // begin method onClick
                    @Override
                    public void onClick( View v ) {

                        // TODO-me implement calling in this here click listener

                        // from the make call activity . . .
                        // 2. we connect to that hotspot
                        // 2a. initialize the system wide socket
                        // 3. we start call functions
                        // 3a. pass the called's name to the calling activity
                        // 3b. pass the caller's name to the calling activity
                        // 3c. switch to the calling activity

                        // 2. we connect to that hotspot

                        activity.runOnUiThread( new Runnable() {
                            @Override
                            public void run() { Toast.makeText( activity, "Selected", Toast.LENGTH_SHORT ).show(); }
                        });

//                        HomeActivity.connectToRemoteHotspot( activity, contacts.get(position).getName() );

                        // 2a. initialize the system wide socket

                        // do this is a different thread

                        String parameters[] = { HomeActivity.HARD_CODED_HOTSPOT_IP_ADDRESS, Integer.toString( HomeActivity.PORT_NUMBER ) };
                        SetUpGlobalSocketAsyncTask setUpGlobalSocketAsyncTask = new SetUpGlobalSocketAsyncTask( null, HomeActivity.MAKE_CALL_ACTIVITY );
                        setUpGlobalSocketAsyncTask.execute( parameters );

                        // 3. we start call functions

                        // 3a. pass the called's name to the calling activity

                        Intent i = new Intent( activity, CallingActivity.class );

                        i.putExtra(HomeActivity.CALLED_NAME, contacts.get( position ).getName() );

                        // 3b. pass the caller's name to the calling activity

                        i.putExtra( HomeActivity.CALLER_NAME, getCallerName() );

                        // 3c. switch to the calling activity
                        activity.startActivity( i );
                        // activity.finish(); or not since we need the Wi-Fi on - the make call activity should be paused at this point

                    } // end method onClick

                } // end class View.OnClickListener

        ); // end method callImageButton.setOnClickListener

        return rowView;

    } // end method getView

    /** Other Methods */

} // end class ContactsArrayAdapter
