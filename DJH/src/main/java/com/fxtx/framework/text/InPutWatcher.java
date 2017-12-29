package com.fxtx.framework.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by z on 2016/3/22.
 * 输入值后 出现删除图标
 */
public class InPutWatcher implements TextWatcher {
    private View view;
    private EditText editText;

    public InPutWatcher(View view, final EditText editText) {
        this.view = view;
        this.editText = editText;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEditText();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Editable editable = editText.getText();
        int len = editable.length();
        if (len > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
    public void removeEditText(){
        editText.setText("");
    }
}
