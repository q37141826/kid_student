package cn.dajiahui.kid.http;

import com.fxtx.framework.widgets.dialog.FxProgressDialog;

/**
 * Created by z on 2016/5/14.
 */
public interface OnDownload {
    void onDownload(String fileurl,FxProgressDialog progressDialog);
}
