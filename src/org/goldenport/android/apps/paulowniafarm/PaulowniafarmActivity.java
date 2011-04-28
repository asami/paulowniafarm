package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.AbstractActivity;

import android.app.Activity;
import android.os.Bundle;

/**
 * @since   Apr. 28, 2011
 * @version Apr. 28, 2011 
 * @author  ASAMI, Tomoharu
 */
public class PaulowniafarmActivity extends AbstractActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
