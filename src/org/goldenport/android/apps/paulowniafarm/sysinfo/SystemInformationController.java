package org.goldenport.android.apps.paulowniafarm.sysinfo;

import org.goldenport.android.GAgent;
import org.goldenport.android.GContext;
import org.goldenport.android.GController;
import org.goldenport.android.GErrorModel;

import com.google.inject.Inject;
import com.google.inject.Injector;

import android.os.Bundle;

/*
 * @since   Apr. 30, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class SystemInformationController extends GController<SystemInformationContext,
                                                             SystemInformationErrorModel,
                                                             SystemInformationModel,
                                                             SystemInformationAgent> {
    @Inject
    protected SystemInformationModel simodel;

    public Bundle getScreenData() {
        return simodel.screen();
    }
}
