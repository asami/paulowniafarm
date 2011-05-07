package org.goldenport.android.apps.paulowniafarm.log;

import java.util.List;

import org.goldenport.android.GActivity;
import org.goldenport.android.apps.paulowniafarm.R;
import org.goldenport.android.traits.ListViewTrait;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * @since   Mar.  3, 2011
 * @version May.  7, 2011
 * @author  ASAMI, Tomoharu
 */
public class LogViewerActivity extends GActivity<LogController> {
    public LogViewerActivity() {
        addTrait(new ListViewTrait());
    }
    
    @Override
    protected Class<LogController> controller_Class() {
        return LogController.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_viewer);
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gcontroller.save();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = gcontroller.getLogList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.error_log_raw, list);
        set_list_adapter(adapter);
    }
}
