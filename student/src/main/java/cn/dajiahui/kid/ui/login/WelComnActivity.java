package cn.dajiahui.kid.ui.login;

import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.base.FxWelcomeAvtivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.LoginHttp;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.SpUtil;

/**
 * 欢迎页面
 */
public class WelComnActivity extends FxWelcomeAvtivity {

    @Override
    public void welcomeClick() {
        SpUtil util = new SpUtil(this);
        String u = util.getKeyLogU();
        String p = util.getKeyLogP();
        if (!StringUtil.isEmpty(u) && !StringUtil.isEmpty(p)) {
            showfxDialog();
            new LoginHttp(new LoginHttp.OnLogin() {
                @Override
                public void error() {
                    dismissfxDialog();
                    DjhJumpUtil.getInstance().startBaseActivity(context, LoginActivity.class);
                }

                @Override
                public void successful() {
                    dismissfxDialog();
                }
            }, WelComnActivity.this).httpData(u, p);
        } else {
            DjhJumpUtil.getInstance().startBaseActivity(context, LoginActivity.class);
        }
        finishActivity();
    }

    @Override
    protected Integer[] initWelcome() {
        return new Integer[]{R.drawable.wel1, R.drawable.wel2, R.drawable.wel3, R.drawable.wel4};
    }

    @Override
    public boolean hineIndicator() {
        return false;
    }
}
