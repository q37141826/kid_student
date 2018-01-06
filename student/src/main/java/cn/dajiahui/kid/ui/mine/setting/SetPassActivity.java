package cn.dajiahui.kid.ui.mine.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.MaxLenghtWatcher;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.hyphenate.EMCallBack;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.login.LoginActivity;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.DjhJumpUtil;


/*
修改密码
* */
public class SetPassActivity extends FxActivity {
    private EditText edOldPWd, edNewPwd, edTooPwd;
    private Button mBtnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_text_fixpass);
        onBackText();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setpass);
        edOldPWd = getView(R.id.edOldPwd);
        edTooPwd = getView(R.id.edToopwd);
        edNewPwd = getView(R.id.ednewPwd);
        mBtnsure = getView(R.id.btn_sure);
        edNewPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edNewPwd, SetPassActivity.this));
        edTooPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edTooPwd, SetPassActivity.this));


        mBtnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpData();
            }
        });
    }

    @Override
    public void httpData() {
        //弹出对话框
        final String oldPwd = edOldPWd.getText().toString().trim();
        if (StringUtil.isEmpty(oldPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.inputoldpwd);
            return;
        }
        if (StringUtil.hasChinese(oldPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.no_chinese);
            return;
        }
        if (oldPwd.length() < 6 || oldPwd.length() > 16) {
            ToastUtil.showToast(SetPassActivity.this, R.string.correct_pwd);
            return;
        }
        final String newPwd = edNewPwd.getText().toString().trim();
        if (StringUtil.isEmpty(newPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.inputnewpwd);
            return;
        }
        if (newPwd.length() < 6 || newPwd.length() > 16) {
            ToastUtil.showToast(SetPassActivity.this, R.string.correct_pwd);
            return;
        }
        if (StringUtil.hasChinese(newPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.no_chinese);
            return;
        }
        final String tooPwd = edTooPwd.getText().toString().trim();
        if (StringUtil.isEmpty(tooPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.inputpwdto);
            return;
        }
        if (StringUtil.hasChinese(tooPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.no_chinese);
            return;
        }
        if (!StringUtil.sameStr(newPwd, tooPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.text_oldnewpwd1);
            return;
        }
        if (!StringUtil.sameStr(UserController.getInstance().getUser().getPwd(), oldPwd)) {
            ToastUtil.showToast(SetPassActivity.this, R.string.pwderror);
            return;
        }
        FxDialog dialg = new FxDialog(SetPassActivity.this) {
            @Override
            public void onRightBtn(int flag) {
                httpPwd(oldPwd, newPwd, tooPwd);
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改密码？");
        dialg.show();
        //执行保存操作
    }

    /**
     * 修改mima
     */
    private void httpPwd(final String oldPwd, final String newPwd, String tooPwd) {
        showfxDialog(R.string.submiting);
        BeUser user = UserController.getInstance().getUser();
        RequestUtill.getInstance().httpModifyPwd(SetPassActivity.this, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(SetPassActivity.this, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    UserController.getInstance().getUser().setPwd(newPwd);
                    ToastUtil.showToast(SetPassActivity.this, R.string.save_ok);
                    loginOut();
                } else {
                    ToastUtil.showToast(SetPassActivity.this, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), user.getUserName(), oldPwd, newPwd, tooPwd);
    }

    public void loginOut() {
        showfxDialog(R.string.logout);
        ImHelper.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                //退出成功
                dismissfxDialog();
                DjhJumpUtil.getInstance().startBaseActivity(SetPassActivity.this, LoginActivity.class);
                ActivityUtil.getInstance().finishActivity(MainActivity.class);
                UserController.getInstance().exitLogin(SetPassActivity.this);
                finishActivity();
            }

            @Override
            public void onError(int i, String s) {
                dismissfxDialog();
                ToastUtil.showToast(SetPassActivity.this, s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
