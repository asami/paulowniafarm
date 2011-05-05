package org.goldenport.android.apps.paulowniafarm.log;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.goldenport.android.GAgent;
import org.goldenport.android.apps.paulowniafarm.sysinfo.SystemInformationModel;

import android.os.Bundle;

import com.google.inject.Inject;

/**
 * @since   Mar.  5, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class LogAgent extends GAgent<LogContext, LogErrorModel, LogModel> {
    private String CRLF = LogModel.CRLF;
    @Inject
    protected SystemInformationModel system_information_model;
    
    public String save(String filename, String memo, long datetime) throws IOException {
        DefaultHttpClient dhc = new DefaultHttpClient();
        try {
            String url = "https://asamix.backlog.jp/dav/RMA/logs/" + filename;
            String user = "reporter";
            String password = "reporterreporter";
            dhc.getCredentialsProvider().setCredentials(
                    AuthScope.ANY, new UsernamePasswordCredentials(user, password));
            HttpPut put = new HttpPut(url);
//            StringEntity reqEntity = new StringEntity(_make_data());
            String string = _make_data(memo, datetime);
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

    private String _make_data(String memo, long datetime) {
        StringBuilder buf = new StringBuilder();
        if (StringUtils.isNotBlank(memo)) {
            buf.append(memo.trim());
            buf.append(CRLF);
            _make_hr(buf);
        }
        _make_general_info(buf, datetime);
        _make_hr(buf);
        _make_log_info(buf);
        return buf.toString();
    }

    private void _make_log_info(StringBuilder buf) {
        for (String l: gmodel.getLogList()) {
            buf.append(l);
            buf.append(CRLF);
            buf.append("----------" + CRLF);
        }
    }

    private void _make_hr(StringBuilder buf) {
        buf.append("====================" + CRLF);        
    }

    private void _make_general_info(StringBuilder buf, long datetime) {
        _make_map(buf, system_information_model.datetime(datetime));
        _make_map(buf, system_information_model.phone());
        _make_map(buf, system_information_model.location());
        _make_map(buf, system_information_model.system());
        _make_map(buf, system_information_model.screen());
    }

    private void _make_map(StringBuilder buf, Bundle info) {
        for (String key: info.keySet()) {
            buf.append(key);
            buf.append(":\t");
            buf.append(StringUtils.trimToEmpty(info.getString(key)));
            buf.append(CRLF);
        }
    }
}
