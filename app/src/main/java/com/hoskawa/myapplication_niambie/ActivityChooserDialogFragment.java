package com.hoskawa.myapplication_niambie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 24-09-2015.
 */

// begin class ActivityChooserDialogFragment
public class ActivityChooserDialogFragment extends DialogFragment implements View.OnClickListener {

    /** CONSTANTS */

    /** VARIABLES */

    /** Buttons */

    private Button positiveButton, // button for yes
                   negativeButton; // button for no

    /** Text Views */

    private TextView informationTextView; // text view to inform the user what the dialog is about

    /** CONSTRUCTORS */

    // empty constructor for the dialog fragment
    public ActivityChooserDialogFragment() {}

    /** METHODS */

    /** Getters and Setters */

    /** Overrides */
    @Nullable
    @Override
    // begin method onCreateView
    // loads the fragment's view using the provided layout inflater
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_dialog_activity_chooser, container );

        informationTextView = ( TextView ) view.findViewById( R.id.fdac_tv_client_choice );
        informationTextView.setText( R.string.fdac_client_choice );

        positiveButton = ( Button ) view.findViewById( R.id.fdac_b_positive );
        positiveButton.setText( R.string.fdac_positive_button );

        negativeButton = ( Button ) view.findViewById( R.id.fdac_b_negative );
        negativeButton.setText( R.string.fdac_negative_button );

        getDialog().setTitle( "Activity Choice" );

        // bind the buttons to this as the click listener
        positiveButton.setOnClickListener( this );
        negativeButton.setOnClickListener( this );

        return view;

    } // end method onCreateView

    @Override
    // begin method onClick
    public void onClick( View v ) {

        // when a button is clicked, it should send its id to the mother activity then dismiss the dialog

        // 1. send ID to mom activity

        ActivityChooserDialogFragmentListener activity = ( ActivityChooserDialogFragmentListener ) getActivity();
        activity.onFinishActivityChooserDialog( v.getId() );

        // 2. dismiss the dialog

        dismiss();

    } // end method onClick

    /** Other Methods */

} // end class ActivityChooserDialogFragment
