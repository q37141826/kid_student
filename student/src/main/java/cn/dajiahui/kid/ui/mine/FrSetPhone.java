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
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.login.bean.BeUser;

/**
 * Created by z on 2016/3/28.
 * 修改用户手机号
 */
public class FrSetPhone extends FxFragment {
    private TextView oldPhone;
    private EditText edPwd, edPhone, edCode;
    private TextView btnCode;
    private TimeCount time;
    private BeUser user;
    private boolean isBtnCode = true;
    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_phone, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldPhone = getView(R.id.old_phone);
        user = UserController.getInstance().getUser();
        edPwd = getView(R.id.edPwd);
        edPhone = getView(R.id.newPhone);
        edCode = getView(R.id.editcode);
        btnCode = getView(R.id.tv_code);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpCode();
            }
        });
        oldPhone.setText(user.getPhone());
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
        final String pwd = edPwd.getText().toString().trim();
        if (StringUtil.isEmpty(pwd)) {
            ToastUtil.showToast(getContext(), R.string.input_pwd);
            return;
        }
        final String newPhone = edPhone.getText().toString().trim();
        if (StringUtil.isEmpty(newPhone)) {
            ToastUtil.showToast(getContext(), R.string.inputnewphonw);
            return;
        }
        final String code = edCode.getText().toString().trim();
        if (StringUtil.isEmpty(code)) {
            ToastUtil.showToast(getContext(), R.string.inputcode);
            return;
        }
        if (StringUtil.sameStr(newPhone, user.getPhone())) {
            ToastUtil.showToast(getContext(), R.string.text_oldnewpwd);
            return;
        }
        if (pwd.length()<6||pwd.length()>16){
            ToastUtil.showToast(getContext(),R.string.correct_pwd);
            return;
        }
        if (!StringUtil.sameStr(UserController.getInstance().getUser().getPwd(),pwd)) {
            ToastUtil.showToast(getContext(), R.string.pwderror);
            return;
        }
        FxDialog dialg = new FxDialog(getContext()) {
            @Override
            public void onRightBtn(int flag) {
                httpPhone(newPhone, code);
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改手机号？");
        dialg.show();
        //执行保存操作
    }

    /**
     * 修改手机号
     */
    private void httpPhone(final String newPhone, String captcha) {
        showfxDialog(R.string.submiting);
        RequestUtill.getInstance().httpboundPhone(getContext(), new ResultCallback() {
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
                    UserController.getInstance().getUser().setPhone(newPhone);
                    getActivity().setResult(Activity.RESULT_OK);
                    finishActivity();
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), newPhone, captcha);
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
        if (StringUtil.sameStr(newPhone, user.getPhone())) {
            ToastUtil.showToast(getContext(), R.string.text_oldnewpwd);
            return;
        }
        if (!StringUtil.sameStr(UserController.getInstance().getUser().getPwd(),pwd)) {
            ToastUtil.showToast(getContext(), R.string.pwderror);
            return;
        }
        showfxDialog(R.string.getcode);
        time.start();
        RequestUtill.getInstance().httpSendCode(getContext(), new ResultCallback() {
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
                if (json.getstatus()  == 1) {
                    ToastUtil.showToast(getContext(), R.string.hint_inputcode);
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                    time.cancel();
                    time.onFinish();
                }
            }
        }, UserController.getInstance().getUserId(), user.getUserName(), newPhone, pwd);
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
