package cn.dajiahui.kid.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.pickerview.TimePickerView;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.Date;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.StudentTextWatcher;

/**
 * 注册
 */
public class RegistActivity extends FxActivity {
    private EditText edRealName, edPwd, edPhone, edVerificationCode;
    private TextView btnCode, tvBirth, tvBoy, tvGirl;
    //    private RadioButton radio_boy, radio_girl;
    private TimePickerView timePickerView;
    //    private RadioGroup radioGroup;
    private TimeCount time;
    private String sex = "1";
    private boolean isBtnCode = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regist);
        btnCode = getView(R.id.tv_code);
        edPhone = getView(R.id.edPhone);
        edPwd = getView(R.id.edPwd);
        edRealName = getView(R.id.edRealName);
        edVerificationCode = getView(R.id.edCode);
        tvBirth = getView(R.id.tvBirth);
        tvBoy = getView(R.id.tv_boy);
        tvGirl = getView(R.id.tv_girl);
//        radioGroup = getView(R.id.radio_sex);
//        radio_boy = getView(R.id.sex_boy);
//        radio_girl = getView(R.id.sex_girl);
        btnCode.setOnClickListener(onClick);
        tvBirth.setOnClickListener(onClick);
        tvBoy.setOnClickListener(onClick);
        tvGirl.setOnClickListener(onClick);

        getView(R.id.btn_sure).setOnClickListener(onClick);
//        radioGroup.setOnCheckedChangeListener(oncheckchanged);
        btnCode.setClickable(false);
        edPhone.addTextChangedListener(new StudentTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBtnCode) {
                    if (edPhone.getText().toString().trim().length() == 11) {
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

    //    private RadioGroup.OnCheckedChangeListener oncheckchanged = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            switch (checkedId) {
//                case R.id.sex_boy:
//                    sex = "1";
//                    radio_boy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_check, 0, 0, 0);
//                    radio_girl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_unchecked, 0, 0, 0);
//                    break;
//                case R.id.sex_girl:
//                    sex = "2";
//                    radio_girl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_check, 0, 0, 0);
//                    radio_boy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_unchecked, 0, 0, 0);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    private View.OnClickListener onClick = new View.OnClickListener() {
        @SuppressLint({"ResourceAsColor", "ResourceType"})
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_boy) {
                sex = "1";
                tvBoy.setTextColor(getResources().getColor(R.color.white));
                tvGirl.setTextColor(getResources().getColor(R.color.text_gray));
                tvBoy.setBackgroundResource( R.drawable.select_btn_yellowbg);
                tvGirl.setBackgroundResource( R.drawable.select_btn_graybg);

            } else if (v.getId() == R.id.tv_girl) {
                tvBoy.setTextColor(getResources().getColor(R.color.text_gray));
                tvGirl.setTextColor(getResources().getColor(R.color.white));
                tvBoy.setBackgroundResource(R.drawable.select_btn_graybg);
                tvGirl.setBackgroundResource(R.drawable.select_btn_yellowbg);
                sex = "2";
            } else if (v.getId() == R.id.tv_code) {
                String PhoneCode = edPhone.getText().toString().trim();
                if (!StringUtil.isEmpty(PhoneCode)) {
                    showfxDialog("获取验证码");
                    httpPhoneCode(PhoneCode);
                } else {
                    ToastUtil.showToast(context, "数据错误");
                }

            } else if (v.getId() == R.id.btn_sure) {
                //注册
                httpData();

            } else if (v.getId() == R.id.tvBirth) {
                //生日选择框
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (timePickerView != null && timePickerView.isShowing()) {
                    timePickerView.dismiss();
                    return;
                }
                timePickerView.show();

            }
        }
    };
    private TimePickerView.OnTimeSelectListener onTimeSelectListener = new TimePickerView.OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date) {
            if (date.getTime() > System.currentTimeMillis()) {
                ToastUtil.showToast(context, "不能选择当前时间之后的日期");
                return;
            } else {
                tvBirth.setText(TimeUtil.timeFormat(date.getTime() + "", TimeUtil.yyMD));
                timePickerView.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        setfxTtitle(R.string.tv_regist);
        time = new TimeCount(60000, 1000);
        timePickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(true);
        timePickerView.setTime(new Date());
        timePickerView.getInAnimation();
        timePickerView.setOnTimeSelectListener(onTimeSelectListener);
    }

    @Override
    public void httpData() {
        String pwd = edPwd.getText().toString().trim();//密码
        String birth = tvBirth.getText().toString().trim();//生日
        String phone = edPhone.getText().toString().trim();//手机号
        String verificationCode = edVerificationCode.getText().toString().trim();//验证码
        String realName = edRealName.getText().toString().trim();//学员姓名

        if (!isEdit(pwd, phone, birth, verificationCode)) {
            return;
        }
        showfxDialog(R.string.tv_regist);//注册
        RequestUtill.getInstance().httpRegist(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                Logger.d("注册返回json:" + response.toString());
                HeadJson json = new HeadJson(response);
//                Logger.d( "注册返回json:" + json.toString());
                if (json.getstatus() == 0) {
                    ToastUtil.showToast(context, R.string.registok);
                    finishActivity();
                } else {
                    if (json.getstatus() == 4) {
                        BeUser temp = json.parsingObject(BeUser.class);
                        ToastUtil.showToast(context, temp.getTelnum());
                        return;
                    } else {
                        ToastUtil.showToast(context, json.getMsg());
                    }

                }
            }
        }, realName, sex, phone, verificationCode, pwd, birth, "Android");
    }

    /*校验*/
    private boolean isEdit(String pwd, String phone, String birth, String verificationCode) {
        boolean isFlase = true;

        if (StringUtil.isEmpty(pwd)) {
            ToastUtil.showToast(context, R.string.input_pwd);
            isFlase = false;
        } else if (StringUtil.isEmpty(phone)) {
            ToastUtil.showToast(context, R.string.forget_user);
            isFlase = false;
        } else if (StringUtil.isEmpty(birth)) {
            ToastUtil.showToast(context, R.string.inputebirth);
            isFlase = false;
        } else if (StringUtil.isEmpty(verificationCode)) {
            ToastUtil.showToast(context, R.string.input_text_code);
            isFlase = false;
        } else if (StringUtil.hasChinese(pwd)) {
            ToastUtil.showToast(context, R.string.no_chinese);
            isFlase = false;
        } else if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtil.showToast(context, R.string.pwd_length);
            isFlase = false;
        }
        return isFlase;
    }

    @Override
    protected void finishActivity() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.finishActivity();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            btnCode.setText("再次获取");
            isBtnCode = true;
            if (edPhone.getText().toString().trim().length() == 11) {
                btnCode.setClickable(true);
                btnCode.setBackgroundResource(R.color.white);
            } else {
                btnCode.setClickable(false);
                btnCode.setBackgroundResource(R.color.white);
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

    /*获取验证码*/
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
                    ToastUtil.showToast(context, json.getMsg());
                } else {
                    time.cancel();
                    time.onFinish();
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        };
        RequestUtill.getInstance().sendPhoneCode(context, callback, phone);
    }
}
