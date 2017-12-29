package cn.dajiahui.kid.ui.login;

import android.os.Bundle;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.hyphenate.chatui.ImConstant;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.util.DjhJumpUtil;


/**
 * Created by z on 2016/3/31.
 */
public class RemoveLoginActivity extends FxActivity {
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private TextView tv_msg;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_removerlogin);
        tv_msg = getView(R.id.tv_msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        UserController.getInstance().exitLogin(context);
        isConflict = getIntent().getBooleanExtra(ImConstant.ACCOUNT_CONFLICT, false);
        isCurrentAccountRemoved = getIntent().getBooleanExtra(ImConstant.ACCOUNT_REMOVED, false);
        ImHelper.getInstance().logout(true);
        if (isConflict) {
            setfxTtitle(R.string.Logoff_notification);
            tv_msg.setText(R.string.connect_conflict);
        } else if (isCurrentAccountRemoved) {
            setfxTtitle(R.string.Remove_the_notification);
            tv_msg.setText(R.string.em_user_remove);
        }
    }

    @Override
    protected void finishActivity() {
        ActivityUtil.getInstance().finishThisActivity(this);
        if (setOnBackAnim()) {
            finishAnim();
        }
        ActivityUtil.getInstance().finishActivity(MainActivity.class);
        if (isConflict) {
        } else if (isCurrentAccountRemoved) {
        }
        DjhJumpUtil.getInstance().startBaseActivity(context, LoginActivity.class);
    }
}
