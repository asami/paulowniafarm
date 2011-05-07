package org.goldenport.android.apps.paulowniafarm.sysinfo;

import org.goldenport.android.GController;

import android.os.Bundle;

import com.google.inject.Inject;

/*
 * @since   Apr. 30, 2011
 * @version May.  7, 2011
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
