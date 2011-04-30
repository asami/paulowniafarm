package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GAgent;
import org.goldenport.android.GContext;
import org.goldenport.android.GController;
import org.goldenport.android.GErrorModel;

import com.google.inject.Inject;

import android.os.Bundle;

/*
 * @since   Apr. 30, 2011
 * @version Apr. 30, 2011
 * @author  ASAMI, Tomoharu
 */
public class SystemInformationController extends GController<GContext, GErrorModel, SystemInformationModel, GAgent<GContext, GErrorModel, SystemInformationModel>> {
    @Inject
    protected SystemInformationModel simodel;

    public Bundle getScreenData() {
        return simodel.getScreenData();
    }
}
