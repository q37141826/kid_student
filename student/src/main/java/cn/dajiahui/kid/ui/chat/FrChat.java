package cn.dajiahui.kid.ui.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fxtx.framework.log.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chatui.ImConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.chat.constant.PreferenceManager;
import cn.dajiahui.kid.ui.chat.db.InviteMessgeDao;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2016/2/3.
 * 沟通列表
 */
public class FrChat extends EaseConversationListFragment {
    private TextView errorText;
    private ProgressDialog progressDialog;

    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    ToastUtil.showToast(getActivity(), R.string.Cant_chat_with_yourself);
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(ImConstant.EXTRA_CHAT_TYPE, ImConstant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(ImConstant.EXTRA_CHAT_TYPE, ImConstant.CHATTYPE_GROUP);
                        }
                    }
                    intent.putExtra(ImConstant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
            errorText.setTag("1");
        } else {
            errorText.setText(R.string.the_current_network);
            errorText.setTag("2");
        }
    }

    @Override
    protected void onConnectionConnected() {
        super.onConnectionConnected();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        /*弹出删除会话的框*/
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        try {
            // 删除此会话
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
        // 更新消息未读数
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View errorView = View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        errorText.setTag("-1");
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals((String) errorText.getTag())) {
                    initProgressBar();
                    loginOut();
                }
            }
        });
        setfxTtitle(R.string.tab_chat);
        onRightBtn( R.string.chat_list);

    }

    private void initProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.show();
    }

    private void loginOut() {
        if (ImHelper.getInstance().isLoggedIn()) {
            ImHelper.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    loginHx();
                }

                @Override
                public void onError(int i, String s) {
                    loginHx();
                }

                @Override
                public void onProgress(int i, String s) {
                }
            });
        } else {
            loginHx();
        }
    }

    private void loginHx() {
        BeUser beUser = UserController.getInstance().getUser();
        if (TextUtils.isEmpty(beUser.getThird().getEasemob_username())) {
            ToastUtil.showToast(getContext(), R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(beUser.getThird().getEasemob_passwd())) {
            ToastUtil.showToast(getContext(), R.string.Password_cannot_be_empty);
            return;
        }
        EMClient.getInstance().login(beUser.getThird().getEasemob_username(), beUser.getThird().getEasemob_passwd(), new EMCallBack() {
            @Override
            public void onSuccess() {
                BeUser beUser = UserController.getInstance().getUser();
                ImHelper.getInstance().setCurrentUserName(beUser.getThird().getEasemob_username());
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                PreferenceManager.getInstance().setCurrentUserAvatar(UserController.getInstance().getUser().getAvatar());
                PreferenceManager.getInstance().setCurrentUserNick(beUser.getRealName());
                PreferenceManager.getInstance().setCurrentUserName(beUser.getThird().getEasemob_username());
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onError(int i, String s) {
                Message mes = handler.obtainMessage();
                mes.what = 2;
                mes.obj = s;
                handler.sendMessage(mes);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
            if (msg.what == 1) {
                refresh();
            } else {
                ToastUtil.showToast(getContext(), (String) msg.obj);
            }
        }
    };

    @Override
    protected void onRightBtnClick(View view) {
        super.onRightBtnClick(view);
        DjhJumpUtil.getInstance().startContactActivity(getContext(), "");
    }

}
