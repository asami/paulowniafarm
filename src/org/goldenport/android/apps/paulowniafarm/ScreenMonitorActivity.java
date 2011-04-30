package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GActivity;
import org.goldenport.android.GModule;
import org.goldenport.android.traits.WebViewTrait;

import android.os.Bundle;

import com.google.common.base.Function;

/*
 * @since   Dec. 18, 2010
 * @version Apr. 30, 2011
 * @author  ASAMI, Tomoharu
 */
public class ScreenMonitorActivity extends GActivity<SystemInformationController> {
	public ScreenMonitorActivity() {
		addTrait(new WebViewTrait().withBundleToTable(new Function<Void, Bundle>() {
			@Override
			public Bundle apply(Void nothing) {
				return controller.getScreenData();
			}
		}));
	}

    @Override
    protected GModule module() {
        return new PaulowniafarmModule(getApplicationContext());
    }

	@Override
	protected Class<SystemInformationController> controller_Class() {
		return SystemInformationController.class;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_monitor);
    }
}
