/*
 * Mobile - Android, User Interface for the GLIMMPSE Software System.  Allows
 * users to perform power and sample size calculations. 
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
package edu.ucdenver.bios.glimmpseandroid.activity.design;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import edu.ucdenver.bios.glimmpseandroid.R;
import edu.ucdenver.bios.glimmpseandroid.activity.TabViewActivity;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter.SimpleGestureListener;
import edu.ucdenver.bios.glimmpseandroid.application.StuyDesignContext;

// TODO: Auto-generated Javadoc
/**
 * The Class SolvingForActivity deals with the 'Solving For' screen of the
 * GLIMMPSE LITE Application.
 * 
 * @author Uttara Sakhadeo
 * @version 1.0.0
 */
public class SolvingForActivity extends Activity implements OnClickListener,
        SimpleGestureListener {

    /** The values. */
    private RadioGroup values;

    /** The checked. */
    private RadioButton checked;

    /** The solving for. */
    private String solvingFor;

    /** The stuy design context. */
    private StuyDesignContext stuyDesignContext = StuyDesignContext
            .getInstance();

    /** The detector. */
    private GestureFilter detector;

    /**
     * This method is called by Android when the Activity is first started. From
     * the incoming Intent, it determines what kind of editing is desired, and
     * then does it.
     * 
     * @param savedInstanceState
     *            the saved instance state
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        final Window window = getWindow();
        boolean useTitleFeature = false;
        detector = new GestureFilter(this, this);
        /*
         * If the window has a container, then we are not free to request window
         * features.
         */
        if (window.getContainer() == null) {
            useTitleFeature = window
                    .requestFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        setContentView(R.layout.design_solving_for);

        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        }
        Button homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setText(getResources().getString(R.string.title_design));
        homeButton.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.window_title);
        title.setText(getResources().getString(R.string.title_solving_for));

        values = (RadioGroup) findViewById(R.id.radioGroup_solvingFor);

        solvingFor = stuyDesignContext.getSolvingFor();
        if (solvingFor != null) {
            if (solvingFor.equals(((RadioButton) values.getChildAt(0))
                    .getText())) {
                ((RadioButton) values.getChildAt(0)).setSelected(true);
            } else {
                ((RadioButton) values.getChildAt(1)).setChecked(true);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        exit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (detector != null)
            detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter.SimpleGestureListener
     * #onSwipe(int)
     */
    public void onSwipe(int direction) {
        switch (direction) {

        case GestureFilter.SWIPE_RIGHT:
            exit();
            break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter.SimpleGestureListener
     * #onDoubleTap()
     */
    public void onDoubleTap() {
    }

    /**
     * Exit.
     */
    private void exit() {
        int selected = values.getCheckedRadioButtonId();
        checked = (RadioButton) findViewById(selected);
        solvingFor = "" + checked.getText();

        stuyDesignContext.setSolvingFor(solvingFor);

        if (stuyDesignContext.getPowerListSize() > 0) {
            stuyDesignContext.clearPowerList();
        }
        if (stuyDesignContext.getSampleSizeListSize() > 0) {
            stuyDesignContext.clearSampleSizeList();
        }

        if (values != null)
            values = null;

        if (checked != null)
            checked = null;

        if (solvingFor != null)
            solvingFor = null;

        if (stuyDesignContext != null)
            stuyDesignContext = null;

        if (detector != null)
            detector = null;

        System.gc();

        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    private boolean menuSelection(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_tutorial:
            exit();
            Intent tabIntent = new Intent(this.getBaseContext(),
                    TabViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("tab_id", 0);
            tabIntent.putExtras(bundle);
            tabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tabIntent);
            return true;
        case R.id.menu_start:
            exit();
            return true;
        case R.id.menu_aboutus:
            exit();
            tabIntent = new Intent(this.getBaseContext(), TabViewActivity.class);
            bundle = new Bundle();
            bundle.putInt("tab_id", 2);
            tabIntent.putExtras(bundle);
            tabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tabIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) { // Handle
        return menuSelection(item);
    }
}