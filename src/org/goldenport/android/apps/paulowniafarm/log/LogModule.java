package org.goldenport.android.apps.paulowniafarm.log;

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
public class LogModule extends GModule {
    public LogModule(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends GContext> context_Class() {
        return LogContext.class;
    }

    @Override
    protected Class<? extends GErrorModel<?>> errormodel_Class() {
        return LogErrorModel.class;
    }

    @Override
    protected Class<? extends GModel<?, ?>> model_Class() {
        return LogModel.class;
    }

    @Override
    protected Class<? extends GAgent<?, ?, ?>> agent_Class() {
        return LogAgent.class;
    }

    @Override
    protected Class<? extends GController<?, ?, ?, ?>> controller_Class() {
        return LogController.class;
    }
}
