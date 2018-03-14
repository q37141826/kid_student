package cn.dajiahui.kid.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.platforms.jpush.JpushReceiver;
import com.fxtx.framework.text.StringUtil;

import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.MainActivity;

/**
 * Created by z on 2016/5/4.
 */
public class StudentReceiver extends JpushReceiver {

    private BeJpush jpush;

    @Override
    protected void sendToActivity(HeadJson json, int notificationID) {

        jpush = new GsonUtil().getJsonObject(json.getObject().toString(), BeJpush.class);
        Logger.d("摩尔jpush:"+jpush.toString()) ;
        if (jpush != null) {
            //处理逻辑
            String str = jpush.getType();
            if (!StringUtil.isEmpty(str)) {
                jpushSendAct(str, notificationID);
            }
        } else {
            ToastUtil.showToast(context, "数据非法");
        }
    }

    @Override
    protected void openActiviy(HeadJson json) {
        Log.d("majin", "极光：" + json.toString());
        jpush = new GsonUtil().getJsonObject(json.getObject().toString(), BeJpush.class);
        if (jpush != null) {
            //处理逻辑
            String str = jpush.getType();
            if (!StringUtil.isEmpty(str)) {
                jpushJumpAct(str);
            }
        } else {
            ToastUtil.showToast(context, "数据非法");
        }
    }

    /**
     * 根据Notification的种类向相应的activity发送通知
     *
     * @param str
     */
    private void jpushSendAct(String str, int notificationID) {
        Intent intent = new Intent();
        switch (str) {
            case Constant.type_xygl:
                // 班级审批
                intent.putExtra("notificationID", notificationID);
                intent.setAction("NOTIFICATION_XYGL");
                context.sendBroadcast(intent);
                break;
            case Constant.type_zybz:
                intent.putExtra("notificationID", notificationID);
                intent.setAction(Constant.broad_notice_action); // 通知
                context.sendBroadcast(intent);
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void jpushJumpAct(String string) {
        switch (string) {
            case Constant.type_cp:
            case Constant.type_rmcs:
            case Constant.type_zybz:
            case Constant.type_zypz:
            case Constant.type_cppz:


                break;
            case Constant.type_pjjs:
                break;
            case Constant.type_tz:
            case Constant.type_tzpl:
            case Constant.type_tzhf:
                break;
            case Constant.type_xc:
            case Constant.type_xcpl:
            case Constant.type_xchf:


                break;
            case Constant.type_xygl:
                break;
            case Constant.type_wdpj:
                break;
            case Constant.type_zb:
            case Constant.type_jk:
                // 转班   结课
                break;
            case Constant.type_ycbj:
                // 移除班级         无操作
                break;
            case Constant.type_sktx:
                // 上课提醒         无操作
                break;
            case Constant.type_dmtz:
                break;
            default:
                break;

        }
        startAct(context, MainActivity.class,jpush.getForeignId());
    }

    private void startAct(Context context, Class classs, String string) {
        Intent intent = new Intent(context, classs);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.bundle_id, string);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


}
