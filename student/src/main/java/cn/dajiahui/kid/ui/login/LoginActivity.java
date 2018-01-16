package cn.dajiahui.kid.ui.login;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.MaxLenghtWatcher;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.StatusBarCompat;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.http.LoginHttp;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.SpUtil;

/**
 * 登录
 */
public class LoginActivity extends FxActivity {
    private EditText edUser, edPwd;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        edUser = getView(R.id.edUser);
        edUser.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edUser, context));
        edPwd = getView(R.id.edPwd);
        edPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edPwd, context));
        getView(R.id.btn_login).setOnClickListener(onClick);
        getView(R.id.regisTv).setOnClickListener(onClick);
        getView(R.id.forgetTv).setOnClickListener(onClick);
        SpUtil util = new SpUtil(this);
        edUser.setText(util.getKeyLogU());
        edUser.setSelection(edUser.getText().length()); // 设置光标在文本末尾
    }

    @Override
    public void setStatusBar(Toolbar title) {
        StatusBarCompat.compatMain(this);
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.regisTv:
                    //进入注册界面
                    DjhJumpUtil.getInstance().startBaseActivity(context, RegistActivity.class);
                    break;
                case R.id.btn_login:
//                    httpData();
                    DjhJumpUtil.getInstance().startBaseActivity(context, MainActivity.class);
                    break;
                case R.id.forgetTv:
                    //忘记密码
                    DjhJumpUtil.getInstance().startBaseActivity(context, ForgetPwdActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void httpData() {
        super.httpData();
        String user = edUser.getText().toString().trim();
        String pwd = edPwd.getText().toString().trim();
        if (StringUtil.isEmpty(user)) {
            ToastUtil.showToast(context, R.string.input_user);
            return;
        }
        if (StringUtil.isEmpty(pwd)) {
            ToastUtil.showToast(context, R.string.input_pwd);
            return;
        }
        showfxDialog(R.string.login);
        new LoginHttp(new LoginHttp.OnLogin() {
            @Override
            public void error() {
                dismissfxDialog();
            }

            @Override
            public void successful() {
                dismissfxDialog();
                finishActivity();
            }
        }, LoginActivity.this).httpData(user, pwd);
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.getInstance().finishAllActivity();
    }
}
