package cn.dajiahui.kid.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.MaxLenghtWatcher;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.constant.PreferenceManager;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.StudentUtil;

/**
 * Created by z on 2016/3/28.
 * 修改帐号
 */
public class FrSetUser extends FxFragment {
    private TextView oldUser;
    private EditText edUser, edPwd, edPhone, edCode;
    private TextView btnCode;
    private TimeCount time;
    private BeUser user;
    private boolean isBtnCode = true;
    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_user, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldUser = getView(R.id.old_user);
        user = UserController.getInstance().getUser();
        edPwd = getView(R.id.edPwd);
        edPwd.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edPwd, getContext()));
        edUser = getView(R.id.edUser);
        edUser.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edUser, getContext()));
        edPhone = getView(R.id.edPhone);
        edCode = getView(R.id.editcode);
        btnCode = getView(R.id.tv_code);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpCode();
            }
        });
        oldUser.setText(user.getUserName());
        time = new TimeCount(60000, 1000);
        btnCode.setClickable(false);
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入中
                if (isBtnCode) {
                    if (edPhone.getText().toString().length() == 11) {
                        btnCode.setBackgroundResource(R.drawable.select_btn_bg);
                        btnCode.setClickable(true);
                    } else {
                        btnCode.setBackgroundResource(R.color.whilte_gray);
                        btnCode.setClickable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入后
            }
        });
    }

    @Override
    public void httpData() {
        //弹出对话框
        final String newUser = edUser.getText().toString().trim();
        if (StringUtil.isEmpty(newUser)) {
            ToastUtil.showToast(getContext(), R.string.inputnewuser);
            return;
        }
        if (!StudentUtil.loginUserLenght(newUser)) {
            ToastUtil.showToast(getContext(), R.string.chinese_lenght);
            return;
        }
        if (StringUtil.sameStr(newUser, user.getUserName())) {
            ToastUtil.showToast(getContext(), R.string.text_oldnewuser);
            return;
        }
        final String pwd = edPwd.getText().toString().trim();
        if (StringUtil.isEmpty(pwd)) {
            ToastUtil.showToast(getContext(), R.string.input_pwd);
            return;
        }
        final String phone = edPhone.getText().toString().trim();
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.showToast(getContext(), R.string.input_regit_phone);
            return;
        }
        final String code = edCode.getText().toString().trim();
        if (StringUtil.isEmpty(code)) {
            ToastUtil.showToast(getContext(), R.string.inputcode);
            return;
        }
        if (!StringUtil.sameStr(UserController.getInstance().getUser().getPwd(),pwd)){
            ToastUtil.showToast(getContext(), R.string.pwderror);
            return;
        }
        FxDialog dialg = new FxDialog(getContext()) {
            @Override
            public void onRightBtn(int flag) {
                httpUser(newUser, phone, code,pwd);
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改帐号？");
        dialg.show();
        //执行保存操作
    }

    /**
     * 修改帐号
     */
    private void httpUser(final String username, String phone, String captcha,String pwd) {
        showfxDialog(R.string.submiting);
        RequestUtill.getInstance().httpModifyAccount(getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(getContext(), ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    getActivity().setResult(Activity.RESULT_OK);
                    PreferenceManager.getInstance().setCurrentUserName(username);
                    UserController.getInstance().getUser().setUserName(username);
                    finishActivity();
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                }
            }
        }, phone, username, captcha, user.getObjectId(),pwd);
    }

    /**
     * 验证码
     */
    private void httpCode() {
        String pwd = edPwd.getText().toString().trim();
        if (StringUtil.isEmpty(pwd)) {
            ToastUtil.showToast(getContext(), R.string.input_pwd);
            return;
        }
        String newPhone = edPhone.getText().toString().trim();
        if (StringUtil.isEmpty(newPhone)) {
            ToastUtil.showToast(getContext(), R.string.inputnewphonw);
            return;
        }
        showfxDialog(R.string.getcode);
        time.start();
        RequestUtill.getInstance().httpUserSendCode(getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                time.cancel();
                time.onFinish();
                ToastUtil.showToast(getContext(), ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    ToastUtil.showToast(getContext(), R.string.hint_inputcode);
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                    time.cancel();
                    time.onFinish();
                }
            }
        }, UserController.getInstance().getUserId(), newPhone);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            btnCode.setText("再次获取");
            isBtnCode = true;
            if (edPhone.getText().toString().length() == 11) {
                btnCode.setClickable(true);
                btnCode.setBackgroundResource(R.drawable.select_btn_bg);
            } else {
                btnCode.setClickable(false);
                btnCode.setBackgroundResource(R.color.whilte_gray);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            isBtnCode = false;
            btnCode.setClickable(false);
            btnCode.setBackgroundResource(R.color.whilte_gray);
            btnCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}