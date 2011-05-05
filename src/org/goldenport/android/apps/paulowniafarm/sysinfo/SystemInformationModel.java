package org.goldenport.android.apps.paulowniafarm.sysinfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.goldenport.android.GModel;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/*
 * @since   Apr. 30, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class SystemInformationModel extends GModel<SystemInformationContext, 
                                                   SystemInformationErrorModel> {
    public Bundle screen() {
        Bundle data = new Bundle();
        return screen(data);
    }

    public Bundle screen(Bundle data) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
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

    public Bundle datetime(long datetime) {
        return datatime(new Bundle(), datetime);
    }

    private Bundle datatime(Bundle bundle, long datetime) {
        bundle.putString("Date Time", _make_date_time(datetime));
        return bundle;
    }

    private String _make_date_time(long datetime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        cal.setTimeInMillis(datetime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = df.format(cal.getTime()).toString();
        return result;
    }

    public Bundle phone() {
        return phone(new Bundle());
    }

    private Bundle phone(Bundle bundle) {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        bundle.putString("Phone Number", tm.getLine1Number()); 
        return bundle;
    }

    protected Object getSystemService(String name) {
        return context.getSystemService(name);
    }

    public Bundle location() {
        return location(new Bundle());
    }

    private Bundle location(Bundle bundle) {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bundle.putString("Location Providers", _make_location_providers(lm));
        return bundle;
    }

    private String _make_location_providers(LocationManager lm) {
        Set<String> enabled = new HashSet<String>();
        enabled.addAll(lm.getProviders(true));
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String name: lm.getAllProviders()) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(name);
            if (enabled.contains(name)) {
                buf.append("(enabled)");
            } else {
                buf.append("(disabled)");
            }
        }
        return buf.toString();
    }

    public Bundle system() {
        return system(new Bundle());
    }

    private Bundle system(Bundle bundle) {
        bundle.putString("OS", _make_os_version());
        bundle.putString("Product", _make_product());
        bundle.putString("Host", Build.HOST);
        return bundle;
    }

    private String _make_os_version() {
        return String.format("%s(%s) %s %s", Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
                Build.VERSION.INCREMENTAL, Build.VERSION.CODENAME);
    }

    private String _make_product() {
        return String.format("%s(%s) %s %s", Build.MODEL, Build.BRAND, Build.PRODUCT, Build.MANUFACTURER); 
    }


    // common
    private String to_string(boolean num) {
        return Boolean.toString(num);
    }

    private String to_string(int num) {
        return Integer.toString(num);
    }

    private String to_string(float num) {
        return Float.toString(num);
    }
}
