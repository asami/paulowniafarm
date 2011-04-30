package org.goldenport.android.apps.paulowniafarm;

import org.goldenport.android.GContext;
import org.goldenport.android.GErrorModel;
import org.goldenport.android.GModel;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/*
 * @since   Apr. 30, 2011
 * @version Apr. 30, 2011
 * @author  ASAMI, Tomoharu
 */
public class SystemInformationModel extends GModel<GContext, GErrorModel> {
    public Bundle getScreenData() {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Bundle data = new Bundle();
        data.putString("Type", to_type(metrics));
        data.putString("Width Pixels", to_string(metrics.widthPixels));
        data.putString("Height Pixels", to_string(metrics.heightPixels));
        data.putString("Density", to_string(metrics.density));
        data.putString("Density DPI", to_density_dpi(metrics.densityDpi));
        data.putString("Scaled Density", to_string(metrics.scaledDensity));
        data.putString("XDPI", to_string(metrics.xdpi));
        data.putString("YDPI", to_string(metrics.ydpi));
        return data;
    }

    private String to_type(DisplayMetrics metrics) {
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        switch (metrics.densityDpi) {
        case DisplayMetrics.DENSITY_LOW:
            if (w == 240 && h == 320) {
                return "QVGA (240x320)";
            } else if (w == 240 && h == 400 ) {
                return "WQVGA400 (240x400)";
            } else if (w == 240 && h == 432) {
                return "WQVGA432 (240x432)";
            }
        case DisplayMetrics.DENSITY_MEDIUM:
            if (w == 320 && h == 480) {
                return "HVGA (320x480)";
            } else if (w == 480 && h == 800) {
                return "WVGA800 (480x800)";
            } else if (w == 480 && h == 854) {
                return "WVGA854 (480x854)";
            }
        case DisplayMetrics.DENSITY_HIGH:
            if (w == 480 && h == 800) {
                return "WVGA800 (480x800)";
            } else if (w == 480 && h == 854) {
                return "WVGA854 (480x854)";
            }
        case 320:
        }
        return String.format("(%dx%d)", w, h);
    }

    private String to_density_dpi(int densityDpi) {
        switch (densityDpi) {
        case DisplayMetrics.DENSITY_LOW:
            return to_string(densityDpi) + " / ldpi";
        case DisplayMetrics.DENSITY_MEDIUM:
            return to_string(densityDpi) + " / mdpi";
        case DisplayMetrics.DENSITY_HIGH:
            return to_string(densityDpi) + " / hdpi";
        case 320:
            return to_string(densityDpi) + " / xdpi";
        default:
            return to_string(densityDpi);
        }
    }

    private String to_string(int num) {
        return Integer.toString(num);
    }

    private String to_string(float num) {
        return Float.toString(num);
    }
}
