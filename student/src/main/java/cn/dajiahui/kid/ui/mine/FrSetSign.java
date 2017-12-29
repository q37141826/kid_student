package cn.dajiahui.kid.ui.mine;

import android.app.Activity;
import android.os.Bundle;
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
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;

/**
 * Created by wdj on 2016/4/6.
 */
public class FrSetSign extends FxFragment {
    private EditText text;
    private TextView tvNum;
    private int maxLenght = 200;
    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_set_sign,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text = getView(R.id.et_sign);
        tvNum = getView(R.id.tv_sign_num);
        int lengt = 0;
        if (!StringUtil.isEmpty(UserController.getInstance().getUser().getSignature())) {
            lengt = UserController.getInstance().getUser().getSignature().length();
        }
//        tvNum.setText(getString(R.string.text_max, maxLenght - lengt));
        MaxLenghtWatcher maxLenghtWatcher = new MaxLenghtWatcher(maxLenght, text, getContext());
        text.addTextChangedListener(maxLenghtWatcher);
        maxLenghtWatcher.setOnEditLength(new MaxLenghtWatcher.onEditLength() {
            @Override
            public void onInputLength(int length) {
//                if (length >= 0)
//                    tvNum.setText(getString(R.string.text_max, length));
            }
        });
    }

    @Override
    public void httpData() {
        if (StringUtil.isEmpty(text.getText().toString().trim())){
            ToastUtil.showToast(getContext(), R.string.ed_user_sign);
            return;
        }
        FxDialog dialg = new FxDialog(getContext()) {
            @Override
            public void onRightBtn(int flag) {
                httpSign(text.getText().toString().trim());
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        };
        dialg.setTitle(R.string.prompt);
        dialg.setMessage("是否要修改个性签名？");
        dialg.show();
    }
    private void httpSign(final String sign){
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
                    UserController.getInstance().getUser().setSignature(sign);
                    getActivity().setResult(Activity.RESULT_OK);
                    finishActivity();
                    ToastUtil.showToast(getContext(), R.string.save_ok);
                } else {
                    ToastUtil.showToast(getContext(), headJson.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), null, null, null, sign);
    }
}
