package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.hyphenate.EMCallBack;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.login.LoginActivity;
import cn.dajiahui.kid.ui.login.bean.BeUserAuth;
import cn.dajiahui.kid.util.DjhJumpUtil;

public class SetUpActivity extends FxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.text_set_up);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_up);
        TextView title = getView(R.id.tool_title);
        title.setText(R.string.text_set_up);

        TextView tv_security = getView(R.id.tvSecurity);
        tv_security.setOnClickListener(onClick);
        TextView tv_about = getView(R.id.tvAbout);
        tv_about.setOnClickListener(onClick);
        Button bt_exit = getView(R.id.btn_Exit);
        bt_exit.setOnClickListener(onClick);
        getView(R.id.toolbar);
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSecurity:
                    Toast.makeText(SetUpActivity.this, "账户安全", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tvAbout:
                    Toast.makeText(SetUpActivity.this, "关于摩尔 ", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btn_Exit:
                    //退出登录
                    FxDialog dialog = new FxDialog(SetUpActivity.this) {
                        @Override
                        public void onRightBtn(int flag) {
                            loginOut();
                        }

                        @Override
                        public void onLeftBtn(int flag) {

                        }
                    };
                    dialog.setTitle(R.string.prompt);
                    dialog.setMessage(R.string.login_edit);
                    dialog.show();
                    break;

                default:
                    break;
            }
        }
    };


    //退出登录
    public void loginOut() {
        showfxDialog(R.string.logout);
        // 功能拆分 退出登录
        BeUserAuth userAuth = UserController.getInstance().getUserAuth();
        if (userAuth.isMsn) {
            ImHelper.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    //退出成功
                    dismissfxDialog();
                    DjhJumpUtil.getInstance().startBaseActivity(SetUpActivity.this, LoginActivity.class);
                    UserController.getInstance().exitLogin(SetUpActivity.this);
                    finishActivity();
                }

                @Override
                public void onError(int i, String s) {
                    dismissfxDialog();
                    ToastUtil.showToast(SetUpActivity.this, s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        } else {
            dismissfxDialog();
            DjhJumpUtil.getInstance().startBaseActivity(SetUpActivity.this, LoginActivity.class);
            UserController.getInstance().exitLogin(SetUpActivity.this);
            finishActivity();
        }
    }

    private void httpAboutUs() {
        showfxDialog();
        RequestUtill.getInstance().httpAboutUs(SetUpActivity.this, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(SetUpActivity.this, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {

                }
            }
        });
    }


}
