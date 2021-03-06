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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.ucdenver.bios.glimmpseandroid.R;
import edu.ucdenver.bios.glimmpseandroid.activity.TabViewActivity;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter;
import edu.ucdenver.bios.glimmpseandroid.adapter.GestureFilter.SimpleGestureListener;
import edu.ucdenver.bios.glimmpseandroid.application.StuyDesignContext;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeIErrorActivity deals with the 'Type I Error' screen of the
 * GLIMMPSE LITE Application.
 * 
 * @author Uttara Sakhadeo
 * @version 1.0.0
 */
public class TypeIErrorActivity extends Activity implements OnClickListener,
        TextWatcher, SimpleGestureListener {

    /** The value. */
    private static EditText value;

    /** The alpha. */
    private Double alpha;

    /** The img. */
    private static Drawable img;

    /** The format. */

    private static final int MAX_PRECISION = 10;
    private static final MathContext mc = new MathContext(MAX_PRECISION,
            RoundingMode.HALF_EVEN);

    /** The stuy design context. */
    private StuyDesignContext stuyDesignContext = StuyDesignContext
            .getInstance();

    /** The detector. */
    private static GestureFilter detector;

    //private static InputMethodManager imm;

    //private boolean isKeyboardVisible = true;

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
        setContentView(R.layout.design_type_one_error);

        if (useTitleFeature) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        int measurement = (int) (density * 20);

        img = getResources().getDrawable(R.drawable.clear_button);
        img.setBounds(0, 0, measurement, measurement);

        Button homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setText(getResources().getString(R.string.title_design));
        homeButton.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.window_title);
        title.setText(getResources().getString(R.string.title_type_i_error));

        value = (EditText) findViewById(R.id.alpha);
        value.addTextChangedListener(this);
        value.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getX() > value.getWidth() - img.getIntrinsicWidth()
                        - 10) {
                    value.setText("0.");
                    resetText();
                }
                return false;
            }

        });

        /*imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
            isKeyboardVisible = true;
        }*/

        if (stuyDesignContext.getTypeIError() == 0.0)
            stuyDesignContext.setDefaultTypeIError();
        alpha = stuyDesignContext.getTypeIError();
        if (alpha != null) {
            value.setText(Double.toString(alpha));
            value.setCompoundDrawables(null, null, img, null);
        } else {
            value.setText("0.");
            value.setCompoundDrawables(null, null, null, null);
        }
        value.requestFocus();
        value.setSelection(value.getText().length());
    }

    /**
     * Reset text.
     */
    private void resetText() {
        if (alpha == null) {
            alpha = 0.0;
        }
        value.requestFocus();
        value.setSelection(value.getText().length());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    public void afterTextChanged(Editable s) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
     * int, int, int)
     */
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
     * int, int)
     */
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        value = (EditText) findViewById(R.id.alpha);
        if (String.valueOf(s) != null && !String.valueOf(s).equals("")) {
            value.setCompoundDrawables(null, null, img, null);
            try {
                alpha = (Double.parseDouble(String.valueOf(s))) % 1;
            } catch (Exception e) {
                if(e != null && ( e.getMessage() != null || !e.getMessage().isEmpty())){
                    Log.e("Tag: " + this.getClass(), "Error: "+e.getMessage());
                }
                else{
                    Log.e("Tag: " + this.getClass(), "Error");
                }
                alpha = 0.0;
                value.setCompoundDrawables(null, null, null, null);
            }
        } else {
            value.setCompoundDrawables(null, null, null, null);
        }
    }

    private void exit() {        
        if (alpha == 0.0)
            stuyDesignContext.setDefaultTypeIError();
        else {
            stuyDesignContext.setTypeIError(new BigDecimal(alpha, mc)
                    .doubleValue());
        }
        finish();
        /*if (imm != null) {            
            if (isKeyboardVisible) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                isKeyboardVisible = false;
            }
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        //isKeyboardVisible = false;        

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {            
            if (stuyDesignContext.getTypeIError() == 0.0)
                stuyDesignContext.setDefaultTypeIError();
            else
                stuyDesignContext.setTypeIError(alpha);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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