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
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.util.StudentTextWatcher;

/**
 * Created by wdj on 2016/7/11.
 */
public class ForgetPwdActivity extends FxActivity {
    private TextView btnCode;
    private EditText edLoginAccount, edPhoneNum, edPhoneCode, edNewPwd, edPwdOk;
    private TimeCount time;
    private boolean isBtnCode = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget);
        btnCode = getView(R.id.tv_code);
        btnCode.setOnClickListener(onClick);
        edLoginAccount = getView(R.id.edLoginAccount);
        edPhoneNum = getView(R.id.edPhoneNum);
        edPhoneCode = getView(R.id.edPhoneCode);
        edNewPwd = getView(R.id.edNewPwd);
        edPwdOk = getView(R.id.edPwdOk);
        btnCode.setClickable(false);
        edLoginAccount.addTextChangedListener(new StudentTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBtnCode) {
                    if (edPhoneNum.getText().toString().trim().length() == 11 && edLoginAccount.getText().toString().trim().length() > 0) {
                        btnCode.setBackgroundResource(R.drawable.select_btn_bg);
                        btnCode.setClickable(true);
                    } else {
                        btnCode.setBackgroundResource(R.color.whilte_gray);
                        btnCode.setClickable(false);
                    }
                }
            }
        });
        edPhoneNum.addTextChangedListener(new StudentTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入中
                if (isBtnCode) {
                    if (edPhoneNum.getText().toString().trim().length() == 11 && edLoginAccount.getText().toString().trim().length() > 0) {
                        btnCode.setBackgroundResource(R.drawable.select_btn_bg);
                        btnCode.setClickable(true);
                    } else {
                        btnCode.setBackgroundResource(R.color.whilte_gray);
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
        onRightBtn(R.drawable.ico_updata, R.string.tv_submit);
        time = new TimeCount(60000, 1000);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_code:
                    String phoneNum = edPhoneNum.getText().toString().trim();
                    String userName = edLoginAccount.getText().toString().trim();
                    if (!StringUtil.isEmpty(phoneNum) && !StringUtil.isEmpty(userName)) {
                        showfxDialog("获取验证码");
                        httpPhoneCode(phoneNum, userName);
                    } else {
                        ToastUtil.showToast(context, "数据错误");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRightBtnClick(View view) {
        super.onRightBtnClick(view);
        FxDialog dialog = new FxDialog(context) {
            @Override
            public void onRightBtn(int flag) {
                checkEmpty();
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialog.setTitle(R.string.prompt);
        dialog.setMessage(R.string.change_pwd);
        dialog.show();
    }

    private void checkEmpty() {
        String account = edLoginAccount.getText().toString().trim();
        String phone = edPhoneNum.getText().toString().trim();
        String code = edPhoneCode.getText().toString().trim();
        String newPwd = edNewPwd.getText().toString().trim();
        String pwdAgsin = edPwdOk.getText().toString().trim();
        if (StringUtil.isEmpty(account)) {
            ToastUtil.showToast(context, R.string.input_login_account);
            return;
        }
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.showToast(context, R.string.input_phone_num);
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
            ToastUtil.showToast(context, R.string.newpwdagin);
            return;
        }
        if (!StringUtil.sameStr(newPwd, pwdAgsin)) {
            ToastUtil.showToast(context, R.string.pwd_two_error);
            return;
        }
        httpChange(account, phone, code, newPwd, pwdAgsin);
    }

    private void httpPhoneCode(String phone, String user) {
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
                if (json.getFlag() == 1) {
                    time.start();
                    ToastUtil.showToast(context, "验证码获取成功");
                } else {
                    time.cancel();
                    time.onFinish();
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        };
        RequestUtill.getInstance().sendPhoneCode(context, callback, phone, user);
    }

    private void httpChange(String userName, String phone, String code, String toChangePwd, String pwdAgain) {
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
                if (json.getFlag() == 1) {
                    ToastUtil.showToast(context, "密码修改成功");
                    ActivityUtil.getInstance().finishActivity(ForgetPwdActivity.class);
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        };
        RequestUtill.getInstance().changePwd(context, callback, userName, phone, code, toChangePwd, pwdAgain);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            btnCode.setText("再次获取");
            isBtnCode = true;
            if (edPhoneNum.getText().toString().trim().length() == 11 && edLoginAccount.getText().toString().trim().length() > 0) {
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
