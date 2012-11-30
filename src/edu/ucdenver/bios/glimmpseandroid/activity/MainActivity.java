/*
 * Mobile - Android, User Interface for the GLIMMPSE Software System.  Allows
 * users to perform power, sample size calculations. 
 * 
 * Copyright (C) 2010 Regents of the University of Colorado.  
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package edu.ucdenver.bios.glimmpseandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import edu.ucdenver.bios.glimmpseandroid.R;
import edu.ucdenver.bios.glimmpseandroid.application.StuyDesignContext;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity deals with the HomeScreen of the GLIMMPSE LITE
 * Application.
 * 
 * @author Uttara Sakhadeo
 */
public class MainActivity extends Activity {

    /** The Constant TAB_ID. */
    public static final String TAB_ID = "tab_id";

    /** The Constant ZERO. */
    public static final int ZERO = 0;

    /** The Constant ONE. */
    public static final int ONE = 1;

    /**
     * Called when an Activity is first started. From the incoming Intent, it
     * determines what kind of editing is desired, and then does it.
     *  
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        /* Initialize required activity. */
        super.onCreate(savedInstanceState);

        // Debug.startMethodTracing("GllimpseLiteTrace");

        /* Setting custom title bar. */
        final Window window = getWindow();
        boolean useTitleFeature = false;
        // If the window has a container, then we are not free
        // to request window features.
        if (window.getContainer() == null) {
            useTitleFeature = window
                    .requestFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        setContentView(R.layout.home_screen);

        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

            Button homeButton = (Button) findViewById(R.id.home_button);
            homeButton.setVisibility(View.GONE);

            Resources res = getResources();
            TextView title = (TextView) findViewById(R.id.window_title);            // Setting action event for Window Title.
            title.setText(res.getString(R.string.title_home));

        }

        Button start = (Button) this.findViewById(R.id.start_button_home);
        start.setOnClickListener(new OnClickListener() {                            // Setting action event for Start Button.

            public void onClick(View v) {
                Intent tabIntent = new Intent(v.getContext(),
                        TabViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TAB_ID, ONE);
                tabIntent.putExtras(bundle);
                startActivity(tabIntent);
            }

        });

        Button learnMore = (Button) this
                .findViewById(R.id.learn_more_button_home);
        learnMore.setOnClickListener(new OnClickListener() {                        // Setting action event for Back Button.

            public void onClick(View v) {
                Intent tabIntent = new Intent(v.getContext(),
                        TabViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TAB_ID, ZERO);
                tabIntent.putExtras(bundle);
                startActivity(tabIntent);
            }

        });
    }

    /*
     * Called when the screen is no longer visible to the user. Next possible
     * methods to receive could be either onRestart(), onDestroy(), or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Debug.stopMethodTracing();
    }

    /**
     * Method called when a new hardware key event occurs.
     * 
     * @param keyCode
     *            the key code
     * @param event
     *            the event
     * @return true, if successful
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Finish the activity if user presses back key from keyboard. 
            StuyDesignContext.resetInstance();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
