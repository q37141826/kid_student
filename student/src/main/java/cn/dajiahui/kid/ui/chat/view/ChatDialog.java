package cn.dajiahui.kid.ui.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fxtx.framework.util.BaseUtil;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatui.ImConstant;

import cn.dajiahui.kid.R;

/**
 * Created by z on 2017/5/24.
 */

public abstract class ChatDialog extends Dialog {
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;

    public ChatDialog(Context context, EMMessage message) {
        super(context, R.style.transparentDialog);
        initWindow();
        initView(message);
    }

    public void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.width = (int) (new BaseUtil().getPhoneWidth(getContext()) * 0.9f);
        wl.gravity = Gravity.CENTER;
        wl.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        window.setAttributes(wl);
    }


    private void initView(EMMessage message) {
        this.setCanceledOnTouchOutside(true);
        if (message == null || message.getType() == null) {
            dismiss();
            return;
        }
        int type = message.getType().ordinal();
        int layoutId;
        if (type == EMMessage.Type.TXT.ordinal()) {
            if (message.getBooleanAttribute(ImConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false) ||
                    message.getBooleanAttribute(ImConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                layoutId = R.layout.em_context_menu_for_location;
            } else if (message.getBooleanAttribute(ImConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                layoutId = R.layout.em_context_menu_for_image;
            } else {
                layoutId = R.layout.em_context_menu_for_text;
            }
        } else if (type == EMMessage.Type.LOCATION.ordinal()) {
            layoutId = R.layout.em_context_menu_for_location;
        } else if (type == EMMessage.Type.IMAGE.ordinal()) {
            layoutId = R.layout.em_context_menu_for_image;
        } else if (type == EMMessage.Type.VOICE.ordinal()) {
            layoutId = R.layout.em_context_menu_for_voice;
        } else if (type == EMMessage.Type.VIDEO.ordinal()) {
            layoutId = R.layout.em_context_menu_for_video;
        } else if (type == EMMessage.Type.FILE.ordinal()) {
            layoutId = R.layout.em_context_menu_for_location;
        } else {
            layoutId = -1;
        }
        if (layoutId == -1) {
            dismiss();
            return;
        }
        View contextView = LayoutInflater.from(this.getContext()).inflate(
                layoutId, null);
        contextView.findViewById(R.id.menu_delete).setOnClickListener(onClic);
        if(layoutId== R.layout.em_context_menu_for_text)
        contextView.findViewById(R.id.menu_copy).setOnClickListener(onClic);

        setContentView(contextView);
    }

    private View.OnClickListener onClic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (v.getId() == R.id.menu_delete) {
                getButton(RESULT_CODE_DELETE);

            } else {
                getButton(RESULT_CODE_COPY);
            }
        }
    };


    public abstract void getButton(int RESULT_CODE);
}
