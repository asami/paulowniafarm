package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GAgent;
import org.goldenport.android.GContext;
import org.goldenport.android.GController;
import org.goldenport.android.GErrorModel;
import org.goldenport.android.GModel;
import org.goldenport.android.GModule;
import org.goldenport.android.agents.NullAgent;

import android.content.Context;

/*
 * @since   Apr. 30, 2011
 * @version May.  1, 2011
 * @author  ASAMI, Tomoharu
 */
public class PaulowniafarmModule extends GModule {
    public PaulowniafarmModule(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends GContext> context_Class() {
        return PaulowniafarmContext.class;
    }

    @Override
    protected Class<? extends GErrorModel<?>> errormodel_Class() {
        return PaulowniafarmErrorModel.class;
    }

    @Override
    protected Class<? extends GModel<?, ?>> model_Class() {
        return SystemInformationModel.class;
    }

    @Override
    protected Class<? extends GController<?, ?, ?, ?>> controller_Class() {
        return SystemInformationController.class;
    }

    @Override
    protected Class<? extends GAgent<?, ?, ?>> agent_Class() {
        return PaulowniafarmAgent.class;
    }
}
