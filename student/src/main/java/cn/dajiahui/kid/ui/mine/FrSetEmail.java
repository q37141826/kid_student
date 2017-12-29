package cn.dajiahui.kid.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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


/**
 * Created by wdj on 2016/4/6.
 */
public class FrSetEmail extends FxFragment {
    private EditText edEmail;
    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_email,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edEmail = getView(R.id.tv_user_email);
        edEmail.setOnClickListener(onclick);
        edEmail.setText(UserController.getInstance().getUser().getEmail());
    }
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_user_email:
                    break;
            }
        }
    };

    @Override
    public void httpData() {
       String newEmail= edEmail.getText().toString().trim();
        if (StringUtil.isEmpty(newEmail)){
            ToastUtil.showToast(getContext(), R.string.ed_user_email);
            return;
        }
        if (!StringUtil.checkEmail(newEmail)){
            ToastUtil.showToast(getContext(), R.string.ed_user_erroremail);
            return;
        }
        if (StringUtil.sameStr(newEmail,UserController.getInstance().getUser().getEmail())){
            ToastUtil.showToast(getContext(), R.string.ed_user_copyemail);
            edEmail.setText("");
            return;
        }
        FxDialog dialg = new FxDialog(getContext()) {
            @Override
            public void onRightBtn(int flag) {
                httpEmail(edEmail.getText().toString().trim());
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改邮箱？");
        dialg.show();
    }

    private void httpEmail(final String email){
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
                    UserController.getInstance().getUser().setEmail(email);
                    getActivity().setResult(Activity.RESULT_OK);
                    finishActivity();
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                } else {
                    ToastUtil.showToast(getContext(), headJson.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), null, email, null,null);
    }
}
