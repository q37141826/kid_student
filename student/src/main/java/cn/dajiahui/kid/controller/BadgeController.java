package cn.dajiahui.kid.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.text.StringUtil;
import com.squareup.okhttp.Request;
import com.umeng.socialize.utils.ContextUtil;

import java.util.List;

import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.bean.BeUnReadCount;

/**
 *
 * 角标控制器  用来控制模块角标显示结果
 */
public class BadgeController {
    //通知，全部功能，角标控制器
    public int noticeBadge;
    //模块角标控制器 作业，评价教师，测评，
    public int paperBadge, auditBadge, testBadge;

    public static BadgeController controller;

    private BadgeController() {
    }

    /**
     * 单一实例
     */
    public static BadgeController getInstance() {
        if (controller == null) {
            synchronized (BadgeController.class) {
                if (controller == null) {
                    controller = new BadgeController();
                }
            }
        }
        return controller;
    }

    public void initNum() {
        noticeBadge = 0;
        paperBadge = 0;
        auditBadge = 0;
        testBadge = 0;
    }

    //发布角标更新通知
    public void sendBadgeMessage(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constant.broad_badge_count_action));
    }

    //初始化 角标数量
    public void initBeDge(List<BeUnReadCount> lists) {
        for (BeUnReadCount c : lists) {
            if (StringUtil.sameStr(Constant.type_tz, c.type)) {
                noticeBadge = c.count;
            }
        }
    }

    /**
     * 获取角标数
     *
     * @return
     */
    public int getNoticeBadgeFunction() {
        return testBadge + paperBadge + auditBadge;
    }

    public int getTypeBadge(String type) {
        int count;
        switch (type) {
            case Constant.type_cp:
                //测评
                count = testBadge;
                break;
            case Constant.type_zybz:
                //作业提交
                count = paperBadge;
                break;
            case Constant.type_pjjs:
                count = auditBadge;
                break;
            default:
                count = 0;
                break;
        }
        return count;
    }


    /**
     * 获取角标数查询
     */
    public void httpCommission() {
        RequestUtill.getInstance().httpCountInfo(ContextUtil.getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 1) {
                    BadgeController.getInstance().paperBadge = json.parsingInt("zybzCount");
                    BadgeController.getInstance().testBadge = json.parsingInt("cpCount");
                    BadgeController.getInstance().auditBadge = json.parsingInt("pjjsCount");
                } else {
                    BadgeController.getInstance().paperBadge = 0;
                    BadgeController.getInstance().testBadge = 0;
                    BadgeController.getInstance().auditBadge = 0;
                }
                BadgeController.getInstance().sendBadgeMessage(ContextUtil.getContext());
            }
        }, UserController.getInstance().getUserId());
    }
}
