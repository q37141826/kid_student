package cn.dajiahui.kid.http;

import android.app.Activity;
import android.app.ProgressDialog;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.updata.BeUpdate;
import com.fxtx.framework.updata.OnUpdateListener;
import com.fxtx.framework.updata.UpdateManager;
import com.fxtx.framework.util.BaseUtil;
import com.fxtx.framework.util.DjhSputils;
import com.squareup.okhttp.Request;

import java.util.Date;

import cn.dajiahui.kid.util.DateUtils;

/**
 * Created by z on 2016/1/26.
 * 版本更新回调
 */
public class UpdateApp extends UpdateManager {

    private Activity context;

    public UpdateApp(Activity context, OnUpdateListener onUpdate) {
        super(context, onUpdate);
        this.context = context;
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
                /*在自定义测试json*/
                String str = "{    \"data\": {        \"apk_url\": \"http://d-static.s.dajiahui.cn/release/mLecture_online_v2.0.7_c11-b171228.apk\",        \"app_id\": 4,        \"is_update\": \"1\",        \"title\": \"Android魔耳英语学生端 升级更新了\",        \"update_msg\": \"升级说明\",        \"version_code\": \"1.0.0\"    },    \"msg\": \"版本升级信息获取成功\",    \"status\": 0}";
                HeadJson json = new HeadJson(str);

                Logger.d("版本更新response:" + str);

                if (json.getstatus() == 0) {
                    BeUpdate update = json.parsingObject(BeUpdate.class);


                    /*判断 is_update  0 不升级 1.提示升级 2.强制升级 */
                    switch (update.getIs_update()) {

                        case "0":
                            onUpdate.onUpdateCancel(0);
                            break;

                        case "1":
                            //SharedPreferences存的时间与当前的时间进行对比（日）
                            if (!new DjhSputils(context).getCancleUpdateTime().equals(TimeUtil.stampToString(String.valueOf(System.currentTimeMillis() / 1000), "yyyyMMdd"))) {
                                isMustUpdate = false;
                                soft_update_title = update.getTitle();
                                message = update.getUpdate_msg();
                                doUpdate(update.getApk_url());
                            } else {
                                onUpdate.onUpdateCancel(0);
                            }
                            break;
                        case "2":
                            isMustUpdate = true;
                            soft_update_title = update.getTitle();
                            message = update.getUpdate_msg();
                            doUpdate(update.getApk_url());
                            break;
                        default:
                            break;
                    }


                } else {
                    ToastUtil.showToast(context, json.getMsg());

                }
            }
        });

    }


    /*手动检查更新*/
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
                if (json.getstatus() == 0) {

                }
            }
        });
    }
}
