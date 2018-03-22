package com.fxtx.framework.text;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.fxtx.framework.R;
import com.fxtx.framework.log.ToastUtil;

/**
 * Created by z on 2016/3/18.
 * 最大输入值限制检测工具
 */
public class MaxLenghtWatcher implements TextWatcher {
    private int maxLen;
    private EditText editText;
    private Context context;
    private onEditLength onEditLength;
    private Button mBtnSure;

    public void setmBtnSure(Button mBtnSure) {
        this.mBtnSure = mBtnSure;
    }

    public MaxLenghtWatcher(int maxLength, EditText editText, Context context) {
        maxLen = maxLength;
        this.editText = editText;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Editable editable = editText.getText();
        int len = editable.length();
        if (len > maxLen) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();
            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
            ToastUtil.showToast(context, context.getString(R.string.tv_max_lenght, maxLen));

            mBtnSure.setBackgroundColor(context.getResources().getColor(R.color.yellow_FEBF12));
        }
        if (onEditLength != null && maxLen - len >= 0)
            onEditLength.onInputLength(maxLen - len);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setOnEditLength(MaxLenghtWatcher.onEditLength onEditLength) {
        this.onEditLength = onEditLength;
    }

    public interface onEditLength {
        void onInputLength(int length);
    }
}
