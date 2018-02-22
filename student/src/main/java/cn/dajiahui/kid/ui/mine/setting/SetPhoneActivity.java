package cn.dajiahui.kid.ui.mine.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.login.bean.BeUser;


/*
修改手机号
* */
public class SetPhoneActivity extends FxActivity {

    private EditText edCode, mPhonenum;
    private TextView btnCode;
    private TimeCount time;
    private Button mBtnsure;
    private BeUser user;
    private boolean isBtnCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_text_userphone);
        onBackText();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_phone);
        mPhonenum = getView(R.id.phone_num);
        mBtnsure = getView(R.id.btn_sure);
        user = UserController.getInstance().getUser();

        edCode = getView(R.id.editcode);
        btnCode = getView(R.id.tv_code);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpCode();
            }
        });

        time = new TimeCount(60000, 1000);
        btnCode.setClickable(false);
        mPhonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入中
                if (isBtnCode && mPhonenum.getText().toString().length() <= 11) {
                    if (mPhonenum.getText().toString().length() == 11) {
                        btnCode.setBackgroundResource(R.color.white);
                        btnCode.setClickable(true);
                    } else {
                        btnCode.setBackgroundResource(R.color.white);
                        btnCode.setClickable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入后
            }
        });


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
        final String Phonenum = mPhonenum.getText().toString().trim();
        if (StringUtil.isEmpty(Phonenum)) {
            ToastUtil.showToast(SetPhoneActivity.this, R.string.inputnewphonw);
            return;
        }
        final String code = edCode.getText().toString().trim();
        if (StringUtil.isEmpty(code)) {
            ToastUtil.showToast(SetPhoneActivity.this, R.string.inputcode);
            return;
        }

        if (Phonenum.length() < 6 || Phonenum.length() > 16) {
            ToastUtil.showToast(SetPhoneActivity.this, R.string.correct_pwd);
            return;
        }
        if (!StringUtil.sameStr(Phonenum, user.getPwd())) {
            ToastUtil.showToast(SetPhoneActivity.this, R.string.pwderror);
            return;
        }

        FxDialog dialg = new FxDialog(SetPhoneActivity.this) {
            @Override
            public void onRightBtn(int flag) {
                httpModifyPhone(Phonenum, code);
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改手机号？");
        dialg.show();
    }

    /**
     * 修改手机号
     */
    private void httpModifyPhone(final String newPhone, String code) {
        showfxDialog(R.string.submiting);

        RequestUtill.getInstance().httpModifyPhone(SetPhoneActivity.this, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(SetPhoneActivity.this, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    setResult(Activity.RESULT_OK);
                    UserController.getInstance().getUser().setPhone(newPhone);
                    finishActivity();
                    ToastUtil.showToast(SetPhoneActivity.this, R.string.save_ok);
                } else {
                    ToastUtil.showToast(SetPhoneActivity.this, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), newPhone, code);
    }

    /**
     * 获取验证码
     */
    private void httpCode() {
        String newPhone = mPhonenum.getText().toString().trim();
        if (StringUtil.isEmpty(newPhone)) {
            ToastUtil.showToast(SetPhoneActivity.this, R.string.inputnewphonw);
            return;
        }

        showfxDialog(R.string.getcode);
        time.start();

        RequestUtill.getInstance().sendPhoneCode(SetPhoneActivity.this, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                time.cancel();
                time.onFinish();
                ToastUtil.showToast(SetPhoneActivity.this, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    ToastUtil.showToast(SetPhoneActivity.this, R.string.hint_inputcode);
                } else {
                    ToastUtil.showToast(SetPhoneActivity.this, json.getMsg());
                    time.cancel();
                    time.onFinish();
                }
            }
        }, newPhone);
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
            if (mPhonenum.getText().toString().length() == 11) {
                btnCode.setClickable(true);
                btnCode.setBackgroundResource(R.color.white);
            } else {
                btnCode.setClickable(false);
                btnCode.setBackgroundResource(R.color.white);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            isBtnCode = false;
            btnCode.setClickable(false);
            btnCode.setBackgroundResource(R.color.white);
            btnCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
