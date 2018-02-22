package cn.dajiahui.kid.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.util.StudentTextWatcher;


/**
 * 忘记密码
 */
public class ForgetPwdActivity extends FxActivity {
    private TextView btnCode;
    private EditText edLoginPhone, edPhoneCode, edNewPwd, edPwdOk;
    private TimeCount time;
    private boolean isBtnCode = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget);
        btnCode = getView(R.id.tv_code);
        btnCode.setOnClickListener(onClick);
        edLoginPhone = getView(R.id.ed_phone);

        edPhoneCode = getView(R.id.edPhoneCode);
        edNewPwd = getView(R.id.edNewPwd);
        edPwdOk = getView(R.id.edPwdOk);
        btnCode.setClickable(false);

        getView(R.id.btn_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });

        edLoginPhone.addTextChangedListener(new StudentTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBtnCode) {
                    if (edLoginPhone.getText().toString().trim().length() == 11) {
                        btnCode.setBackgroundResource(R.color.white);
                        btnCode.setClickable(true);
                    } else {
                        btnCode.setBackgroundResource(R.color.white);
                        btnCode.setClickable(false);
                    }
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        setfxTtitle(R.string.forget_pwd);
        time = new TimeCount(60000, 1000);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_code:
                    String userName = edLoginPhone.getText().toString().trim();
                    if (!StringUtil.isEmpty(userName)) {
                        showfxDialog("获取验证码");
                        httpPhoneCode(userName);
                    } else {
                        ToastUtil.showToast(context, "数据错误");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /*检验*/
    private void checkEmpty() {
        String phone = edLoginPhone.getText().toString().trim();
        String code = edPhoneCode.getText().toString().trim();
        String newPwd = edNewPwd.getText().toString().trim();
        String pwdAgsin = edPwdOk.getText().toString().trim();
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.showToast(context, R.string.forget_user);
            return;
        }
        if (StringUtil.isEmpty(code)) {
            ToastUtil.showToast(context, R.string.inputcode);
            return;
        }
        if (StringUtil.isEmpty(newPwd)) {
            ToastUtil.showToast(context, R.string.inputnewpwd);
            return;
        }
        if (StringUtil.isEmpty(pwdAgsin)) {
            ToastUtil.showToast(context, R.string.inputnewpwd);
            return;
        }
        if (!StringUtil.sameStr(newPwd, pwdAgsin)) {
            ToastUtil.showToast(context, R.string.inputpwdto);
            return;
        }
        httpChange(phone, newPwd, pwdAgsin, code);

    }

    /*获取手机验证码*/
    private void httpPhoneCode(String phone) {
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                time.cancel();
                time.onFinish();
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    time.start();
                    ToastUtil.showToast(context, "验证码获取成功");
                } else {
                    time.cancel();
                    time.onFinish();
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        };
        RequestUtill.getInstance().sendPhoneCode(context, callback, phone);
    }

    /*忘记密码*/
    private void httpChange(String phone, String code, String toChangePwd, String pwdAgain) {
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    ActivityUtil.getInstance().finishActivity(ForgetPwdActivity.class);
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        };
        RequestUtill.getInstance().changePwd(context, callback, phone, toChangePwd, pwdAgain, code);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            btnCode.setText("再次获取");
            isBtnCode = true;
            if (edLoginPhone.getText().toString().trim().length() == 11) {
                btnCode.setClickable(true);
                btnCode.setBackgroundResource(R.drawable.select_btn_bg);
            } else {
                btnCode.setClickable(false);
                btnCode.setBackgroundResource(R.color.whilte_gray);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isBtnCode = false;
            btnCode.setClickable(false);
            btnCode.setBackgroundResource(R.color.whilte_gray);
            btnCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
