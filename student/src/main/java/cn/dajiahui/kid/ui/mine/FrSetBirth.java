package cn.dajiahui.kid.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.pickerview.TimePickerView;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import java.util.Date;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;


/**
 * Created by wdj on 2016/4/6.
 */
public class FrSetBirth extends FxFragment {
    private TimePickerView timePickerView;
    private TextView tvBirth;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_birth, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBirth = getView(R.id.tv_user_birth);
        tvBirth.setText(TimeUtil.timeFormat(UserController.getInstance().getUser().getBirthday(), TimeUtil.yyMD));
        tvBirth.setOnClickListener(onclick);
        timePickerView = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(true);
        timePickerView.setTime(new Date());
        timePickerView.getInAnimation();
        timePickerView.setOnTimeSelectListener(onTimeSelectListener);
    }

    @Override
    public void httpData() {
        if (StringUtil.isEmpty(tvBirth.getText().toString().trim())) {
            ToastUtil.showToast(getContext(), R.string.ed_user_birth);
            return;
        }
        FxDialog dialg = new FxDialog(getContext()) {
            @Override
            public void onRightBtn(int flag) {
                httpBirth(tvBirth.getText().toString().trim());
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改生日？");
        dialg.show();
    }

    private void httpBirth(final String birth) {
        showfxDialog(R.string.submiting);
        RequestUtill.getInstance().httpUserMessage(getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(getContext(), ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson headJson = new HeadJson(response);
                if (headJson.getFlag() == 1) {
                    UserController.getInstance().getUser().setBirthday(TimeUtil.dateToLong(birth) + "");
                    getActivity().setResult(Activity.RESULT_OK);
                    finishActivity();
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                } else {
                    ToastUtil.showToast(getContext(), headJson.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), null, null, birth, null);
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_user_birth) {
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
                ToastUtil.showToast(getContext(), "不能选择当前时间之后的日期");
                return;
            } else {
                tvBirth.setText(TimeUtil.timeFormat(date.getTime() + "", TimeUtil.yyMD));
                timePickerView.dismiss();
            }
        }
    };
}
