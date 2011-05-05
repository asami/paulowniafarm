package org.goldenport.android.apps.paulowniafarm.sysinfo;

import org.goldenport.android.GAgent;
import org.goldenport.android.GContext;
import org.goldenport.android.GController;
import org.goldenport.android.GErrorModel;
import org.goldenport.android.GModel;
import org.goldenport.android.GModule;

import android.content.Context;

/*
 * @since   May.  5, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class SystemInformationModule extends GModule {
    public SystemInformationModule(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends GContext> context_Class() {
        return SystemInformationContext.class;
    }

    @Override
    protected Class<? extends GErrorModel<?>> errormodel_Class() {
        return SystemInformationErrorModel.class;
    }

    @Override
    protected Class<? extends GModel<?, ?>> model_Class() {
        return SystemInformationModel.class;
    }

    @Override
    protected Class<? extends GAgent<?, ?, ?>> agent_Class() {
        return SystemInformationAgent.class;
    }

    @Override
    protected Class<? extends GController<?, ?, ?, ?>> controller_Class() {
        return SystemInformationController.class;
    }
}
