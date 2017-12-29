package cn.dajiahui.kid.ui.notice.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fxtx.framework.util.BaseUtil;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenuBase;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;


/**
 * Created by z on 2016/3/26.
 */
public class FxEmojiconView extends LinearLayout {
    private ImageView btnEmoji;
    private EditText editText;
    private Button btnSend;
    private EaseEmojiconMenu emojiconMenu;
    private boolean isShowEmoji;

    public FxEmojiconView(Context context) {
        super(context);
        initView(context);
    }

    public FxEmojiconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FxEmojiconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setOrientation(VERTICAL);
        inflate(context, R.layout.layout_eval, this);
        btnEmoji = (ImageView) findViewById(R.id.imeMoji);
        editText = (EditText) findViewById(R.id.edEval);
        btnSend = (Button) findViewById(R.id.btn_send);
        emojiconMenu = (EaseEmojiconMenu) findViewById(R.id.emojicon);
        List<EaseEmojiconGroupEntity> emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
//        emojiconGroupList.add(new EaseEmojiconGroupEntity(com.hyphenate.chatui.R.drawable.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));
        // emojicon menu
        emojiconMenu.init(emojiconGroupList);
        emojiconMenu.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

            @Override
            public void onExpressionClicked(EaseEmojicon emojicon) {
                if (emojicon.getType() != EaseEmojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        editText.append(EaseSmileUtils.getSmiledText(FxEmojiconView.this.getContext(), emojicon.getEmojiText()));
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                if (!TextUtils.isEmpty(editText.getText())) {
                    KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    editText.dispatchKeyEvent(event);
                }
            }
        });
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //触摸
                toggleEmojicon(true);
                return false;
            }
        });
        btnEmoji.setOnClickListener(onClickListener);
        btnSend.setOnClickListener(onClickListener);
    }

    public void toggleEmojicon(boolean isInPut) {
        if (isInPut) {
            if (emojiconMenu.getVisibility() == View.VISIBLE) {
                emojiconMenu.setVisibility(View.GONE);
                btnEmoji.setImageResource(R.drawable.ico_emoji);
                isShowEmoji = false;
            }
        } else {
            BaseUtil.hideSoftInput((Activity) getContext());
            if (emojiconMenu.getVisibility() == View.VISIBLE) {
                emojiconMenu.setVisibility(View.GONE);
                btnEmoji.setImageResource(R.drawable.ico_emoji);
                isShowEmoji = false;
            } else {
                isShowEmoji = true;
                emojiconMenu.setVisibility(View.VISIBLE);
                btnEmoji.setImageResource(R.drawable.ico_emoji_k);
            }
        }
    }

    private OnEmojiconInput input;

    public void setEdiTextHint(Object object) {
        editText.setText("");
        if (object instanceof String)
            editText.setHint((String) object);
        else
            editText.setHint((Integer) object);
    }


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imeMoji) {
                toggleEmojicon(false);
            } else {
                //发送按钮
                if (input != null) {
                    input.onEdit(editText.getText().toString());
                }
            }
        }
    };

    public EditText getEdit() {
        return editText;
    }

    public void setInput(OnEmojiconInput input) {
        this.input = input;
    }

    public boolean isShowEmoji() {
        return isShowEmoji;
    }

    public interface OnEmojiconInput {
        void onEdit(String string);
    }

}
