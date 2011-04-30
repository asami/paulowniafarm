package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GModule;
import org.goldenport.android.GTabActivity;
import org.goldenport.android.controllers.NullController;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @since   Apr. 28, 2011
 * @version Apr. 30, 2011 
 * @author  ASAMI, Tomoharu
 */
public class PaulowniafarmActivity extends GTabActivity<NullController> implements PaulowniafarmConstants {
    @Override
    protected GModule module() {
        return new PaulowniafarmModule(getApplicationContext());
    }

    @Override
    protected Class<NullController> controller_Class() {
        return NullController.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Resources res = getResources();   
        TabHost tabHost = getTabHost();    
        Intent intent1 = new Intent().setClass(this, LogViewerActivity.class);  
        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1")  
            .setIndicator("Log", res.getDrawable(R.drawable.ic_tab_screen))  
            .setContent(intent1);  
        tabHost.addTab(spec1);  
        Intent intent2 = new Intent().setClass(this, ScreenMonitorActivity.class);  
        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2")  
            .setIndicator("Screen", res.getDrawable(R.drawable.ic_tab_screen))  
            .setContent(intent2);  
        tabHost.addTab(spec2);  
//        tabHost.setCurrentTab(1);
    }
}
