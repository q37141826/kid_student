package cn.dajiahui.kid.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.pickerview.TimePickerView;
import com.fxtx.framework.text.MaxLenghtWatcher;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.Date;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.util.StudentUtil;

/**
 * Created by z on 2016/3/30.
 */
public class RegistActivity extends FxActivity {
    private EditText edLogName, edRealName, edEmail, edPwd, edPwdTo, edPhone, edCode, edClassCode;
    private TextView btnCode, tvBirth;
    private RadioButton radio_boy, radio_girl;
    private TimePickerView timePickerView;
    private RadioGroup radioGroup;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regist);
        btnCode = getView(R.id.tv_code);
        edCode = getView(R.id.editcode);
        edPhone = getView(R.id.edPhone);
        edPwd = getView(R.id.edPwd); // 密码
        edPwdTo = getView(R.id.edPwdtwo); // 确认密码
        edEmail = getView(R.id.edEmail);
        edRealName = getView(R.id.edRealName);
        edLogName = getView(R.id.edNickName);
        edLogName.addTextChangedListener(new MaxLenghtWatcher(AppSet.login_maxlenght, edLogName, context));
        edClassCode = getView(R.id.edCode);
        tvBirth = getView(R.id.tvBirth);
        radioGroup = getView(R.id.radio_sex);
        radio_boy = getView(R.id.sex_boy);
        radio_girl = getView(R.id.sex_girl);
        btnCode.setOnClickListener(onClick);
        tvBirth.setOnClickListener(onClick);
        getView(R.id.btn_sure).setOnClickListener(onClick);
        radioGroup.setOnCheckedChangeListener(oncheckchanged);

    }

    private RadioGroup.OnCheckedChangeListener oncheckchanged = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.sex_boy:
                    radio_boy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_check, 0, 0, 0);
                    radio_girl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_unchecked, 0, 0, 0);
                    break;
                case R.id.sex_girl:
                    radio_girl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_check, 0, 0, 0);
                    radio_boy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_sex_unchecked, 0, 0, 0);
                    break;
                default:
                    break;
            }
        }
    };
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_code) {
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
        timePickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(true);
        timePickerView.setTime(new Date());
        timePickerView.getInAnimation();
        timePickerView.setOnTimeSelectListener(onTimeSelectListener);
    }

    @Override
    public void httpData() {
        String logname = edLogName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String pwd1 = edPwd.getText().toString().trim();
        String pwd2 = edPwdTo.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String code = edCode.getText().toString().trim();
        String birth = tvBirth.getText().toString().trim();
        String classCode = edClassCode.getText().toString().trim();
        String realName = edRealName.getText().toString().trim();
        if (!isEdit(logname, email, pwd1, pwd2, phone, code, birth, classCode)) {
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
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    ToastUtil.showToast(context, R.string.registok);
                    finishActivity();
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, logname, pwd1, pwd2, classCode, email, phone, birth, radio_boy.isChecked() ? "M" : "W", realName);
    }

    private boolean isEdit(String nickName, String email, String pwd1, String pwd2,
                           String phone, String code, String birth, String classCode) {
        boolean isFlase = true;
        if (StringUtil.isEmpty(nickName)) {
            ToastUtil.showToast(context, R.string.input_user);
            isFlase = false;
        } else if (!StudentUtil.loginUserLenght(nickName)) {
            ToastUtil.showToast(context, R.string.chinese_lenght);
            isFlase = false;
        } else if (StringUtil.isEmpty(pwd1)) {
            ToastUtil.showToast(context, R.string.input_pwd);
            isFlase = false;
        } else if (StringUtil.hasChinese(pwd1)) {
            ToastUtil.showToast(context, R.string.no_chinese);
            isFlase = false;
        } else if (StringUtil.isEmpty(pwd2)) {
            ToastUtil.showToast(context, R.string.inputpwdto);
            isFlase = false;

        } else if (StringUtil.hasChinese(pwd2)) {
            ToastUtil.showToast(context, R.string.no_chinese);
            isFlase = false;
        } else if (pwd1.length() < 6 || pwd1.length() > 16 || pwd2.length() < 6 || pwd2.length() > 16) {
            ToastUtil.showToast(context, R.string.pwd_length);
            isFlase = false;
        } else if (!StringUtil.sameStr(pwd2, pwd1)) {
            ToastUtil.showToast(context, R.string.text_oldnewpwd1);
            isFlase = false;
//        } else if (StringUtil.isEmpty(phone)) {
//            ToastUtil.showToast(context, R.string.input_regit_phone);
//            isFlase = false;
//        }
//        else if (StringUtil.isEmpty(birth)) {
//            ToastUtil.showToast(context, R.string.click_birth);
//            isFlase = false;
//        } else if (!isBeforeToday(birth)) {
//            ToastUtil.showToast(context, R.string.click_birth_real);
//            isFlase = false;
//        } else if (StringUtil.isEmpty(classCode)) {
//            ToastUtil.showToast(context, R.string.input_code);
//            isFlase = false;
//        } else if (StringUtil.isEmpty(email)) {
//            ToastUtil.showToast(context, R.string.inputemail);
//            isFlase = false;
//
//        } else if (!StringUtil.mobilePhone(phone, false)) {
//            ToastUtil.showToast(context, R.string.input_right_phine);
//
//        } else if (!StringUtil.mobilePhone(phone, false)) {
//            ToastUtil.showToast(context, R.string.input_right_phine);
//            isFlase = false;
//        } else if (!StringUtil.checkEmail(email)) {
//            ToastUtil.showToast(context, R.string.emailerror);
//            isFlase = false;
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

//    private Boolean isBeforeToday(String str) {
//        String[] s = str.split("-");
//        if (ParseUtil.parseInt(s[0]) > Calendar.getInstance().get(Calendar.YEAR)) {
//            return false;
//        } else if (ParseUtil.parseInt(s[0]) == Calendar.getInstance().get(Calendar.YEAR)) {
//            if (ParseUtil.parseInt(s[1]) > (Calendar.getInstance().get(Calendar.MONTH) + 1)) {
//                return false;
//            } else if (ParseUtil.parseInt(s[1]) == (Calendar.getInstance().get(Calendar.MONTH) + 1)) {
//                if (ParseUtil.parseInt(s[2]) > Calendar.getInstance().get(Calendar.DATE)) {
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//            return true;
//        }
//        return true;
//    }
}
