package org.goldenport.android.apps.paulowniafarm.log;

import java.util.List;

import org.goldenport.android.GController;
import org.goldenport.android.apps.paulowniafarm.R;
import org.goldenport.android.util.SpinnerDialogAsyncTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * @since   Mar.  4, 2011
 * @version May.  5, 2011
 * @author  ASAMI, Tomoharu
 */
public class LogController extends GController<LogContext,
                                               LogErrorModel,
                                               LogModel,
                                               LogAgent> {
    private long _datetime = System.currentTimeMillis();
    private Dialog _save_dialog;
    private EditText _save_dialog_filename;
    private EditText _save_dialog_memo;

    public void save() {
        _save_dialog = new Dialog(activity);
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
    }

    private void _save() {
        new SpinnerDialogAsyncTask<Void, String>(activity, gcontext, "送信中...") {
            @Override
            protected String do_In_Background(Void[] params) throws Throwable {
                return gagent.save(
                        _save_dialog_filename.getText().toString(),
                        _save_dialog_memo.getText().toString(),
                        _datetime);
            }

            @Override
            protected void on_Post_Execute(String result) {
                new AlertDialog.Builder(activity).setMessage(result).setPositiveButton("OK", null).create().show();
            }

            @Override
            protected void on_Post_Exception(Throwable e) {
                // TODO Auto-generated method stub
            }
        }.execute();
    }
    private String _make_filename() {
        return gmodel.makeDefaultFilename(_datetime);
    }

    public List<String> getLogList() {
        return gmodel.getLogList();
    }
}
