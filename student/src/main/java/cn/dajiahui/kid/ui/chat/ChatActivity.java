package cn.dajiahui.kid.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.util.EasyUtils;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.MainActivity;

/**
 * Created by z on 2016/3/24.
 */
public class ChatActivity extends EaseBaseActivity {
    private ChatFragment chatFragment;
    String toChatUsername;
    String phonenum;

    @Override
    protected void initView() {
        setContentView(R.layout.em_activity_chat);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toChatUsername = getIntent().getStringExtra("userId");
        phonenum = getIntent().getStringExtra("phone");
        if (getIntent().getExtras() == null) {
            ToastUtil.showToast(context, "数据非法");
            return;
        }
        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (StringUtil.sameStr(toChatUsername, username))
            super.onNewIntent(intent);
        else {
            finishActivity();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

}
