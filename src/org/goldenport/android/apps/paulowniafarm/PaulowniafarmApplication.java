package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GApplication;

import android.content.Context;

import com.google.inject.Module;

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
