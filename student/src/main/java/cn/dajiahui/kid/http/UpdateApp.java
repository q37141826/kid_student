package cn.dajiahui.kid.http;

import android.app.Activity;
import android.app.ProgressDialog;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.updata.BeUpdate;
import com.fxtx.framework.updata.OnUpdateListener;
import com.fxtx.framework.updata.UpdateManager;
import com.fxtx.framework.util.BaseUtil;
import com.squareup.okhttp.Request;

/**
 * Created by z on 2016/1/26.
 * 版本更新回调
 */
public class UpdateApp extends UpdateManager {

    public UpdateApp(Activity context, OnUpdateListener onUpdate) {
        super(context, onUpdate);
    }

    @Override
    public void checkUpdateOrNotAuto() {

        RequestUtill.getInstance().httpUpdateApp(mContext, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                onUpdate.onUpdateCancel(0);
            }

            @Override
            public void onResponse(String response) {
                HeadJson json = new HeadJson(response);

                if (json.getFlag() == 1) {
                    BeUpdate update = json.parsingObject(BeUpdate.class);
                    if (update.getCodeNumber() > BaseUtil.getVersionCode(mContext)) {
                        if (StringUtil.sameStr(update.isForceUpdateFlag(), "1")) {
                            isMustUpdate = true;
                        } else {
                            isMustUpdate = false;
                        }
                        message = update.getContent();
                        doUpdate(update.getDownloadUrl());
                    } else {
                        onUpdate.onUpdateCancel(0);
                    }
                } else {
                    onUpdate.onUpdateCancel(0);
                }
            }
        });
    }

    @Override
    public void checkUpdateOrNot() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("检测更新");
        progressDialog.show();
        RequestUtill.getInstance().httpUpdateApp(mContext, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                onUpdate.onUpdateError("请求更新失败");
                progressDialog.dismiss();
                updateHandler.sendEmptyMessage(DO_NOTHING);
            }

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    BeUpdate update = json.parsingObject(BeUpdate.class);
                    if (update.getCodeNumber() > BaseUtil.getVersionCode(mContext)) {
                        message = update.getContent();
                        if (StringUtil.sameStr(update.isForceUpdateFlag(), "1")) {
                            isMustUpdate = true;
                        } else {
                            isMustUpdate = false;
                        }
                        doUpdate(update.getDownloadUrl());
                    } else {
                        onUpdate.onUpdateCancel(0);
                        updateHandler.sendEmptyMessage(DO_NOTHING);
                    }
                } else {
                    onUpdate.onUpdateCancel(0);
                    updateHandler.sendEmptyMessage(DO_NOTHING);
                }
            }
        });
    }
}
