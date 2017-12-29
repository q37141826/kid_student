package cn.dajiahui.kid.ui.chat.constant;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.ui.chat.db.UserDao;

import static cn.dajiahui.kid.ui.chat.constant.PreferenceManager.getInstance;
import static cn.dajiahui.kid.ui.chat.constant.PreferenceManager.init;

public class IMModel {
    UserDao dao = null;
    protected Context context = null;
    protected Map<Key, Object> valueCache = new HashMap<Key, Object>();

    public IMModel(Context ctx) {
        context = ctx;
        init(context);
    }

    /**
     * 设置当前用户的环信id
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        getInstance().setCurrentUserName(username);
    }

    /**
     * 获取当前用户的环信id
     */
    public String getCurrentUsernName() {
        return getInstance().getCurrentUsername();
    }

    public boolean getSettingMsgNotification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if (val == null) {
            val = getInstance().getSettingMsgNotification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public boolean getSettingMsgSound() {
        Object val = valueCache.get(Key.PlayToneOn);

        if (val == null) {
            val = getInstance().getSettingMsgSound();
            valueCache.put(Key.PlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public boolean getSettingMsgVibrate() {
        Object val = valueCache.get(Key.VibrateOn);

        if (val == null) {
            val = getInstance().getSettingMsgVibrate();
            valueCache.put(Key.VibrateOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public boolean getSettingMsgSpeaker() {
        Object val = valueCache.get(Key.SpakerOn);

        if (val == null) {
            val = getInstance().getSettingMsgSpeaker();
            valueCache.put(Key.SpakerOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public List<String> getDisabledGroups() {
        Object val = valueCache.get(Key.DisabledGroups);

        if (dao == null) {
            dao = new UserDao(context);
        }

        if (val == null) {
            val = dao.getDisabledGroups();
            valueCache.put(Key.DisabledGroups, val);
        }

        return (List<String>) val;
    }

    public List<String> getDisabledIds() {
        Object val = valueCache.get(Key.DisabledIds);

        if (dao == null) {
            dao = new UserDao(context);
        }

        if (val == null) {
            val = dao.getDisabledIds();
            valueCache.put(Key.DisabledIds, val);
        }

        return (List<String>) val;
    }

    public void setGroupsSynced(boolean synced) {
        getInstance().setGroupsSynced(synced);
    }

    public boolean isGroupsSynced() {
        return getInstance().isGroupsSynced();
    }

    public void setContactSynced(boolean synced) {
        getInstance().setContactSynced(synced);
    }

    public boolean isContactSynced() {
        return getInstance().isContactSynced();
    }

    public void setBlacklistSynced(boolean synced) {
        getInstance().setBlacklistSynced(synced);
    }

    public boolean isBacklistSynced() {
        return getInstance().isBacklistSynced();
    }

    public boolean isChatroomOwnerLeaveAllowed() {
        return getInstance().getSettingAllowChatroomOwnerLeave();
    }

    public boolean isDeleteMessagesAsExitGroup() {
        return getInstance().isDeleteMessagesAsExitGroup();
    }

    public boolean isAutoAcceptGroupInvitation() {
        return getInstance().isAutoAcceptGroupInvitation();
    }

    enum Key {
        VibrateAndPlayToneOn,
        VibrateOn,
        PlayToneOn,
        SpakerOn,
        DisabledGroups,
        DisabledIds
    }
}
