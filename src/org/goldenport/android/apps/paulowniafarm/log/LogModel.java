package org.goldenport.android.apps.paulowniafarm.log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.goldenport.android.GModel;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

/**
 * @since   Mar.  5, 2011
 * @version May.  7, 2011
 * @author  ASAMI, Tomoharu
 */
public class LogModel extends GModel<LogContext, LogErrorModel> {
    public static final String CRLF = "\r\n";
    private String _log_tag = null;

    public String makeDefaultFilename(long datetime) {
        return _make_filename(datetime);
    }

    private String _make_filename(long datetime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        cal.setTimeInMillis(datetime);
        StringBuilder buf = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        buf.append(df.format(cal.getTime()).toString());
        buf.append(".txt");
        return buf.toString();
    }

    public List<String> getLogList() {
        List<String> r = new ArrayList<String>();
        InputStream in = null;
        try {
            String cmd = String.format("logcat -d -v time -s %s:*", getLogTag());
            Process proc = Runtime.getRuntime().exec(cmd);
            in = proc.getInputStream();
            return _filter(CharStreams.readLines(new InputStreamReader(in, "utf-8")));
        } catch (IOException e) {} finally {
            Closeables.closeQuietly(in);
        }
        return r;
    }

    private String getLogTag() {
        if (_log_tag  != null) return _log_tag;
        else return gcontext.getLogTag(); // XXX
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
}

