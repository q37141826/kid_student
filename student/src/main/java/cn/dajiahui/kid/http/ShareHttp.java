package cn.dajiahui.kid.http;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

/**
 * Created by z on 2016/5/16.
 * 分享文件上传
 */
public class ShareHttp {
    private FxActivity activity;
    private String pictureId;

    public ShareHttp(FxActivity activity, String pictureId) {
        this.activity = activity;
        this.pictureId = pictureId;
    }

    //开始上传
    public void startShare() {
        activity.showfxDialog();
        RequestUtill.getInstance().shareMsg(activity, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                activity.dismissfxDialog();
                ToastUtil.showToast(activity, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                activity.dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 1) {
                    //成功
//                    BeShareContent beTeFile = json.parsingObject(BeShareContent.class);
//                    new UmengShare().shartShare(activity, beTeFile);
                } else {
                    ToastUtil.showToast(activity, json.getMsg());
                }
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
            }
        }, pictureId);
    }

}