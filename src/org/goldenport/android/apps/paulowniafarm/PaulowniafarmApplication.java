package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GApplication;
import org.goldenport.android.GModule;
import org.goldenport.android.apps.paulowniafarm.log.LogModule;
import org.goldenport.android.apps.paulowniafarm.sysinfo.SystemInformationModule;

import com.google.inject.Module;

import android.content.Context;

/**
 * @since   May.  5, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class PaulowniafarmApplication extends GApplication {
    @Override
    protected Module[] create_Modules() {
        Context ctx = getApplicationContext();
        return new Module[] {
                new PaulowniafarmModule(ctx)
        };
    }
}
