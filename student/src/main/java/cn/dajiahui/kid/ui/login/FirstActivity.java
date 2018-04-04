package cn.dajiahui.kid.ui.login;

import com.fxtx.framework.log.Logger;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.base.FxFirstActivity;

import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.http.LoginHttp;
import cn.dajiahui.kid.http.UpdateApp;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.SpUtil;

/**
 * 启动活动入口
 */
public class FirstActivity extends FxFirstActivity {
    @Override
    protected void handlerUpdate() {

        UpdateApp manager = new UpdateApp(this, onUpdate);
        manager.checkUpdateOrNotAuto();

        Logger.d("检查版本更新-----------------------manager.checkUpdateOrNotAuto()");
    }

    @Override
    protected void handlerWelcome() {
        SpUtil util = new SpUtil(this);
        if (util.getWelcomeNum() < AppSet.welcome) {
            util.setWelcomeNum(AppSet.welcome);
            DjhJumpUtil.getInstance().startBaseActivity(context, WelComnActivity.class);

            finishActivity();
        } else {
            String u = util.getKeyLogU();
            String p = util.getKeyLogP();
            //直接进入登录
            if (!StringUtil.isEmpty(u) && !StringUtil.isEmpty(p)) {
                showfxDialog();
                new LoginHttp(new LoginHttp.OnLogin() {
                    @Override
                    public void error() {
                        dismissfxDialog();
                        if (ImHelper.getInstance().isLoggedIn()) {
                            ImHelper.getInstance().logout(true);
                        }
                        DjhJumpUtil.getInstance().startBaseActivity(context, LoginActivity.class);
                        finishActivity();
                    }

                    @Override
                    public void successful() {
                        dismissfxDialog();
                        finishActivity();
                    }
                }, FirstActivity.this).httpData(u, p);
            } else {
                if (ImHelper.getInstance().isLoggedIn()) {
                    ImHelper.getInstance().logout(true);
                }
                DjhJumpUtil.getInstance().startBaseActivity(context, LoginActivity.class);
                finishActivity();
            }
        }
    }

    @Override
    protected void initView() {

    }
}
