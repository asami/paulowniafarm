package org.goldenport.android.apps.paulowniafarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.goldenport.android.util.SpinnerDialogAsyncTask;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

/**
 * @since   Mar.  3, 2011
 * @version Apr. 29, 2011
 * @author  ASAMI, Tomoharu
 */
public class LogViewerActivity extends ListActivity {
    private static final String CRLF = "\r\n";
    private List<String> _list;
    private long _datetime = System.currentTimeMillis();
    private Dialog _save_dialog;
    private EditText _save_dialog_filename;
    private EditText _save_dialog_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_viewer);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _command_save();
            }
        });
        _save_dialog = new Dialog(this);
        _save_dialog.setTitle("Backlogに保存しますか?");
        _save_dialog.setContentView(R.layout.log_viewer_save_dialog);
        _save_dialog_filename = (EditText)_save_dialog.findViewById(R.id.log_filename);
        String filename = _make_filename();
        _save_dialog_filename.setText(filename);
        _save_dialog_filename.setSelection(filename.length() - ".txt".length());
        _save_dialog_memo = (EditText)_save_dialog.findViewById(R.id.log_memo);
        _save_dialog.findViewById(R.id.log_save_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _save_dialog.dismiss();
                _save();
            }
        });
        _save_dialog.findViewById(R.id.log_save_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _save_dialog.dismiss();
            }
        });
        _list = _get_log();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.error_log_raw, _list);
        setListAdapter(adapter);
    }

    private String _make_filename() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        cal.setTimeInMillis(_datetime);
        StringBuilder buf = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        buf.append(df.format(cal.getTime()).toString());
/*
        MoogliModel model = MoogliModelFactory.getModel();
        try {
            String member = model.getUser();
            buf.append("_");
            buf.append(member);
        } catch (IOException e) {
        }
*/
        buf.append(".txt");
        return buf.toString();
    }

    private List<String> _get_log() {
        List<String> r = new ArrayList<String>();
        InputStream in = null;
        try {
            Process proc = Runtime.getRuntime().exec("logcat -d -v time -s moogli:*");
            in = proc.getInputStream();
            return _filter(CharStreams.readLines(new InputStreamReader(in, "utf-8")));
        } catch (IOException e) {} finally {
            Closeables.closeQuietly(in);
        }
        return r;
    }

    private List<String> _filter(List<String> lines) {
        List<String> r = new ArrayList<String>();
        int index = 0;
        int length = lines.size();
        for (;;) {
            if (index >= length) break;
            String l = lines.get(index);
            if (l.startsWith("-----")) {
                index++;
                continue;
            }
            if (index + 2 < length) {
                String l2 = lines.get(index + 2);
                if (_is_stack_line(l2)) {
                     index = _pack_exception(lines, index, r);
                     continue;
                }
            }
            r.add(l);
            index++;
        }
        Collections.reverse(r);
        return r;
    }

    private int _pack_exception(List<String> lines, int index, List<String> r) {
        StringBuilder buf = new StringBuilder();
        buf.append(_normalize_line(lines.get(index)));
        buf.append(CRLF);
        buf.append(_normalize_cont_line(lines.get(index + 1)));
        buf.append(CRLF);
        int i = index + 2;
        for (;;) {
            if (lines.size() <= i) break;
            String l = lines.get(i);
            if (!_is_stack_line(l)) break;
            buf.append(_normalize_cont_line(l));
            i++;
        }
        r.add(buf.toString());
        return i;
    }

    private boolean _is_stack_line(String l) {
        return l.indexOf(".java:") != -1 ||
               l.indexOf("(Native Method)") != -1;
    }

    private String _normalize_line(String l) {
        return l + CRLF;
    }

    private String _normalize_cont_line(String l) {
        String key = "):";
        int i = l.indexOf(key);
        if (i == -1) {
            return _normalize_line(l);
        } else {
            return _normalize_line(l.substring(i + key.length()));
        }
    }

    protected void _command_save() {
        _save_dialog.show();
    }

    private void _save() {
        new SpinnerDialogAsyncTask<Void, String>(this, "送信中...") {
            @Override
            protected String do_In_Background(Void[] params) throws Throwable {
                return _save_do();
            }

            @Override
            protected void on_Post_Execute(String result) {
                new AlertDialog.Builder(LogViewerActivity.this).setMessage(result).setPositiveButton("OK", null).create().show();
            }
        }.execute();
    }

    protected String _save_do() throws ClientProtocolException, IOException {
        DefaultHttpClient dhc = new DefaultHttpClient();
        try {
            String url = "https://asamix.backlog.jp/dav/RMA/logs/" + _save_dialog_filename.getText().toString();
            String user = "reporter";
            String password = "reporterreporter";
            dhc.getCredentialsProvider().setCredentials(
                    AuthScope.ANY, new UsernamePasswordCredentials(user, password));
            HttpPut put = new HttpPut(url);
//            StringEntity reqEntity = new StringEntity(_make_data());
            String string = _make_data();
            ByteArrayEntity reqEntity = new ByteArrayEntity(string.getBytes("utf-8"));
            reqEntity.setContentType("text/plain");
            reqEntity.setContentEncoding("utf-8");
            put.setEntity(reqEntity);
            HttpResponse httpResponse = dhc.execute(put);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            return "" + statusCode;
        } finally {
            dhc.getConnectionManager().shutdown();
        }
    }

    private String _make_data() {
        StringBuilder buf = new StringBuilder();
        String memo = _save_dialog_memo.getText().toString();
        if (StringUtils.isNotBlank(memo)) {
            buf.append(memo.trim());
            buf.append(CRLF);
            _make_hr(buf);
        }
        _make_general_info(buf);
        _make_hr(buf);
        _make_log_info(buf);
        return buf.toString();
    }

    private void _make_log_info(StringBuilder buf) {
        for (String l: _list) {
            buf.append(l);
            buf.append(CRLF);
            buf.append("----------" + CRLF);
        }
    }

    private void _make_hr(StringBuilder buf) {
        buf.append("====================" + CRLF);        
    }

    private void _make_general_info(StringBuilder buf) {
        _make_map(buf, _get_time_info());
//        _make_map(buf, _get_model_info());
        _make_map(buf, _get_phone_info());
        _make_map(buf, _get_location_info());
        _make_map(buf, _get_system_info());
        _make_map(buf, _get_display_info());
    }

    private void _make_map(StringBuilder buf, Map<String, String> info) {
        for (Entry<String, String> entry: info.entrySet()) {
            buf.append(entry.getKey());
            buf.append(":\t");
            buf.append(StringUtils.trimToEmpty(entry.getValue()));
            buf.append(CRLF);
        }
    }

    private Map<String, String> _get_time_info() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Date Time", _make_date_time());
        return map;
    }

    private String _make_date_time() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        cal.setTimeInMillis(_datetime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = df.format(cal.getTime()).toString();
        return result;
    }

/*
    private Map<String, String> _get_model_info() {
        MoogliModel model = MoogliModelFactory.getModel();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Version", _make_version());
        map.put("Build", getString(R.string.app_version));
        map.put("Member", _make_member(model));
        map.put("Debug mode", to_string(model.isDebug()));
        map.put("Network", _make_network_status(model));
        map.put("Location", _make_location_status(model));
        return map;        
    }

    private String _make_version() {
        String packegeName = "jp.co.recruit.moogli.android";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packegeName, PackageManager.GET_META_DATA);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    private String _make_member(MoogliModel model) {
        try {
            return model.getUser();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String _make_network_status(MoogliModel model) {
        if (model.isNetworkNotEnabled()) {
            return "Not Enabled";
        } else if (model.isNetworkUnavailable()) {
            return "Unavailable";
        } else {
            return "Available";
        }
    }

    private String _make_location_status(MoogliModel model) {
        if (model.isLocationNotEnabled()) {
            return "Not Enabled";
        } else if (model.isLocationUnavailable()) {
            return "Unavailable";
        } else {
            try {
                Location loc = model.getLocation();
                return String.format("[%s,%s]", loc.getLatitude(), loc.getLongitude());
            } catch (MessagedIOException e) {
                return e.getMessage();
            }
        }
    }
*/
    private Map<String, String> _get_phone_info() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Phone Number", tm.getLine1Number()); 
        return map;        
    }

    private Map<String, String> _get_location_info() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Location Providers", _make_location_providers(lm));
        return map;        
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

    private Map<String, String> _get_system_info() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("OS", _make_os_version());
        map.put("Product", _make_product());
        map.put("Host", Build.HOST);
        return map;
    }

    private String _make_os_version() {
        return String.format("%s(%s) %s %s", Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
                Build.VERSION.INCREMENTAL, Build.VERSION.CODENAME);
    }

    private String _make_product() {
        return String.format("%s(%s) %s %s", Build.MODEL, Build.BRAND, Build.PRODUCT, Build.MANUFACTURER); 
    }

    private Map<String, String> _get_display_info() {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Type", to_type(metrics));
        map.put("Width Pixels", to_string(metrics.widthPixels));
        map.put("Height Pixels", to_string(metrics.heightPixels));
        map.put("Density", to_string(metrics.density));
        map.put("Density DPI", to_density_dpi(metrics.densityDpi));
        map.put("Scaled Density", to_string(metrics.scaledDensity));
        map.put("XDPI", to_string(metrics.xdpi));
        map.put("YDPI", to_string(metrics.ydpi));
        return map;
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

    private String to_string(boolean num) {
        return Boolean.toString(num);
    }
}
