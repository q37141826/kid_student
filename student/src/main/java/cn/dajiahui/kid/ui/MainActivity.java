package cn.dajiahui.kid.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.chivox.cube.util.FileHelper;
import com.fxtx.framework.chivox.config.Config;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.ui.base.FxTabActivity;
import com.fxtx.framework.ui.bean.BeTab;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.badge.BadgeRadioButton;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatui.ImConstant;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.chat.FrChat;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.chat.db.DemoDBManager;
import cn.dajiahui.kid.ui.chat.db.UserDao;
import cn.dajiahui.kid.ui.homework.FrHomework;
import cn.dajiahui.kid.ui.login.LoginActivity;
import cn.dajiahui.kid.ui.login.bean.BeUserAuth;
import cn.dajiahui.kid.ui.mine.FrMine;
import cn.dajiahui.kid.ui.mine.personalinformation.UserDetailsActivity;
import cn.dajiahui.kid.ui.study.FrStudy;


public class MainActivity extends FxTabActivity {
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private RadioGroup radioGroup;
    private FrChat frChat;
    private FrStudy frStudy;
    public FrHomework frHomework;
    private FrMine frMine;
    private int rediobtnId; // 当前选择的模块
    private BadgeRadioButton noticeRb, functionRb, chatRb;
    private Boolean isExit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                default:
                    break;
            }
        }
    };

    private BeUserAuth userAuth = UserController.getInstance().getUserAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (userAuth.isMsn) {
            if (savedInstanceState != null && savedInstanceState.getBoolean(ImConstant.ACCOUNT_REMOVED, false)) {
                // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
                // 三个fragment里加的判断同理
                ImHelper.getInstance().logout(true, null);
                finishActivity();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
                // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
                // 三个fragment里加的判断同理
                finishActivity();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
        }
        registerBroadcastReceiver();
        initFragment(savedInstanceState);
//     BadgeController.getInstance().httpCommission();
        loadAllResOncce(); // 下载驰声资源
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        radioGroup = getView(R.id.tab_group);

        BeTab tab = new BeTab(R.id.rediobtn_task, "", getString(R.string.tab_homework), R.drawable.radio_task, true);
        BeTab tab2 = new BeTab(R.id.rediobtn_study, "", getString(R.string.tab_study), R.drawable.radio_study, false);
        BeTab tab4 = new BeTab(R.id.rediobtn_mine, "", getString(R.string.tab_mine), R.drawable.radio_mine, false);

        addRadioView(tab, radioGroup);
        functionRb = addRadioView(tab2, radioGroup);
//        if (userAuth.isMsn) {
        BeTab tab3 = new BeTab(R.id.rediobtn_chat, "", getString(R.string.tab_chat), R.drawable.radio_chat, false);
        chatRb = addRadioView(tab3, radioGroup);
//        }
        addRadioView(tab4, radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

    }

//    @Override
//    public void setStatusBar(Toolbar title) {
//        StatusBarCompat.compatMain(this);
//    }

    @Override
    protected FxFragment initIndexFragment() {
        if (frHomework == null) {
            frHomework = new FrHomework();
        }
        return frHomework;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rediobtn_task:
                if (frHomework == null) {
                    frHomework = new FrHomework();//作业
                }
                switchContent(isFragment, frHomework);
                rediobtnId = 0;
                break;
            case R.id.rediobtn_study:
                if (frStudy == null) {
                    frStudy = new FrStudy();//学习
                }
                switchContent(isFragment, frStudy);
                rediobtnId = 2;
                break;
            case R.id.rediobtn_chat:
                if (frChat == null) {
                    frChat = new FrChat();//沟通
                }
                switchContent(isFragment, frChat);
                rediobtnId = 3;
                break;
            case R.id.rediobtn_mine:
                if (frMine == null) {
                    frMine = new FrMine();//我的
                }
                switchContent(isFragment, frMine);
                rediobtnId = 4;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (RESULT_OK == resultCode && requestCode == 2001) {
//            if (frNotice != null)
//                frNotice.indexHttp();
//        }
        if (RESULT_OK == resultCode && requestCode == 3001) {
            ArrayList<String> strings = data.getStringArrayListExtra(Constant.bundle_obj);
            if (strings != null && strings.size() != 0) {
                if (frMine != null) {
//                    httpUserIcon(new File(strings.get(0)));
                }
            }
        }
        if (resultCode == UserDetailsActivity.PICSETSULT) {
            //进入用户详情修改图片后返回来的值
            if (frHomework != null) {
//                GlideUtil.showRoundImage(MainActivity.this, UserController.getInstance().getUser().getAvator(), frHomework.imUer, R.drawable.ico_default_user, mtrue);
            }
            if (frMine != null) {
//                GlideUtil.showRoundImage(MainActivity.this, UserController.getInstance().getUser().getAvator(), frMine.imUser, R.drawable.ico_default_user, mtrue);
            }
        }
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                String avatar = message.getStringAttribute(UserDao.COLUMN_NAME_AVATAR, "");
                String nickname = message.getStringAttribute(UserDao.COLUMN_NAME_NICK, "");
                if (!StringUtil.isEmpty(nickname)) {
                    EaseUser user = new EaseUser(message.getFrom());
                    user.setAvatar(avatar);
                    user.setNick(nickname);
                    DemoDBManager.getInstance().saveContact(user);
                }
                ImHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                if (rediobtnId == 3) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (frChat != null) {
                        frChat.refresh();
                    }
                }
            }
        });
    }

    /*注册广播*/
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.broad_badge_count_action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                //收到广播了
//                if (userAuth.isNotice) {
//                    StudentUtil.setBadge(noticeRb, BadgeController.getInstance().noticeBadge);
//                }
//                StudentUtil.setBadge(functionRb, BadgeController.getInstance().getNoticeBadgeFunction());
                if (rediobtnId == 2 && frStudy != null) {

                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        if (chatRb != null) {
            int count = getUnreadMsgCountTotal();
            if (count > 0) {
                chatRb.showTextBadge("" + count);
            } else {
                chatRb.hiddenBadge();
            }
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    @Override
    protected void onResume() {
        if (UserController.getInstance().getUserAuth().isMsn) {
            updateUnreadLabel();
            ImHelper sdkHelper = ImHelper.getInstance();
            sdkHelper.pushActivity(this);
            EMClient.getInstance().chatManager().addMessageListener(messageListener);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (UserController.getInstance().getUserAuth().isMsn) {
            EMClient.getInstance().chatManager().removeMessageListener(messageListener);
            ImHelper sdkHelper = ImHelper.getInstance();
            sdkHelper.popActivity(this);
        }
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                ToastUtil.showToast(context, R.string.angin_back);
                handler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                moveTaskToBack(false);
                UserController.getInstance().initBean();
                ActivityUtil.getInstance().finishAllActivity();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void loadAllResOncce(){
        loadProvisionFile();
        unZipNativeRes();
    }

    /**
     * 驰声资源下载
     */
    private void loadProvisionFile(){
        File provisionFile = FileHelper.extractProvisionOnce(MainActivity.this, Config.provisionFilename);
        Log.d("loadProvisionFile :", "provisionFile :"+provisionFile.getAbsolutePath());
    }

    /**
     * 驰声资源解压
     */
    private void unZipNativeRes(){

//        Log.d(TAG, "unzip start");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("unZipNativeRes...");
//        pDialog.show();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run()
            {
                File vadFile = FileHelper.extractResourceOnce(MainActivity.this, "vad.zip");
                Log.d("vadFile :", "vadFile :"+vadFile.getAbsolutePath());
                //native resource unzip process
//                pDialog.dismiss();
            }
        }, true);
//        Log.d(TAG, "unzip ended");
    }

}
