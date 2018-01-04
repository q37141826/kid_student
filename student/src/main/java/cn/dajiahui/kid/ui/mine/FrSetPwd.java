package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.MaxLenghtWatcher;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;
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
import cn.dajiahui.kid.ui.login.bean.BeUserAuth;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2016/3/28.
 * 修改密码
 */
public class FrSetPwd extends FxFragment {
    private EditText edOldPWd, edNewPwd, edTooPwd;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_pwd, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edOldPWd = getView(R.id.edOldPwd);
        edTooPwd = getView(R.id.edToopwd);
        edNewPwd = getView(R.id.ednewPwd);
        edNewPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edNewPwd, getContext()));
        edTooPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edTooPwd, getContext()));

    }

    @Override
    public void httpData() {
        //弹出对话框
        final String oldPwd = edOldPWd.getText().toString().trim();
        if (StringUtil.isEmpty(oldPwd)) {
            ToastUtil.showToast(getContext(), R.string.inputoldpwd);
            return;
        }
        if (StringUtil.hasChinese(oldPwd)) {
            ToastUtil.showToast(getContext(), R.string.no_chinese);
            return;
        }
        final String newPwd = edNewPwd.getText().toString().trim();
        if (StringUtil.isEmpty(newPwd)) {
            ToastUtil.showToast(getContext(), R.string.inputnewpwd);
            return;
        }
        if (StringUtil.hasChinese(newPwd)) {
            ToastUtil.showToast(getContext(), R.string.no_chinese);
            return;
        }
        if (oldPwd.length() < 6 || oldPwd.length() > 16) {
            ToastUtil.showToast(getContext(), R.string.correct_pwd);
            return;
        }
        final String tooPwd = edTooPwd.getText().toString().trim();
        if (StringUtil.isEmpty(tooPwd)) {
            ToastUtil.showToast(getContext(), R.string.inputpwdto);
            return;
        }
        if (StringUtil.hasChinese(tooPwd)) {
            ToastUtil.showToast(getContext(), R.string.no_chinese);
            return;
        }
        if (!StringUtil.sameStr(UserController.getInstance().getUser().getPwd(), oldPwd)) {
            ToastUtil.showToast(getContext(), R.string.pwderror);
            return;
        }
        if (!StringUtil.sameStr(newPwd, tooPwd)) {
            ToastUtil.showToast(getContext(), R.string.text_oldnewpwd1);
            return;
        }

        FxDialog dialg = new FxDialog(getContext()) {
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
     * 修改帐号
     */
    private void httpPwd(String oldPwd, final String newPwd, String tooPwd) {
        showfxDialog(R.string.submiting);
        BeUser user = UserController.getInstance().getUser();
        RequestUtill.getInstance().httpModifyPwd(getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(getContext(), ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus()  == 1) {
                    UserController.getInstance().getUser().setPwd(newPwd);
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                    loginOut();
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), UserController.getInstance().getUser().getUserName(), oldPwd, newPwd, tooPwd);
    }

    public void loginOut() {
        showfxDialog(R.string.logout);
        BeUserAuth userAuth = UserController.getInstance().getUserAuth();
        if (userAuth.isMsn) {
            ImHelper.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    //退出成功
                    dismissfxDialog();
                    ActivityUtil.getInstance().finishActivity(MainActivity.class);
                    DjhJumpUtil.getInstance().startBaseActivity(getContext(), LoginActivity.class);
                    UserController.getInstance().exitLogin(getContext());
                    finishActivity();
                }

                @Override
                public void onError(int i, String s) {
                    dismissfxDialog();
                    ToastUtil.showToast(getContext(), s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        } else {
            dismissfxDialog();
            ActivityUtil.getInstance().finishActivity(MainActivity.class);
            DjhJumpUtil.getInstance().startBaseActivity(getContext(), LoginActivity.class);
            UserController.getInstance().exitLogin(getContext());
            finishActivity();
        }
    }
}