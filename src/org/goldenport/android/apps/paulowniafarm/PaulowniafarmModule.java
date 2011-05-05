package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GAgent;
import org.goldenport.android.GContext;
import org.goldenport.android.GController;
import org.goldenport.android.GErrorModel;
import org.goldenport.android.GModel;
import org.goldenport.android.GModule;
import org.goldenport.android.apps.paulowniafarm.log.LogController;
import org.goldenport.android.apps.paulowniafarm.log.LogModule;
import org.goldenport.android.apps.paulowniafarm.sysinfo.SystemInformationController;
import org.goldenport.android.apps.paulowniafarm.sysinfo.SystemInformationModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

import android.content.Context;

/*
 * @since   Apr. 30, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class PaulowniafarmModule extends GModule {
    private final Injector _log_injector;
    private final Injector _si_injector;

    public PaulowniafarmModule(Context context) {
        super(context);
        _log_injector = Guice.createInjector(new LogModule(context));
        _si_injector = Guice.createInjector(new SystemInformationModule(context));
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
        return PaulowniafarmModel.class;
    }

    @Override
    protected Class<? extends GController<?, ?, ?, ?>> controller_Class() {
        return PaulowniafarmController.class;
    }

    @Override
    protected Class<? extends GAgent<?, ?, ?>> agent_Class() {
        return PaulowniafarmAgent.class;
    }

    @Provides
    SystemInformationController providesSystemInformationController() {
        SystemInformationController c = _si_injector.getInstance(SystemInformationController.class);
        return c;
    }

    @Provides
    LogController providesLogController() {
        LogController c = _log_injector.getInstance(LogController.class);
        return c;
    }
}
