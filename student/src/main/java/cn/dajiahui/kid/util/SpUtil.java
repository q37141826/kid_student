package cn.dajiahui.kid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fxtx.framework.json.GsonUtil;

import cn.dajiahui.kid.ui.login.bean.BeUser;

/**
 *  SharedPreferences
 */
public class SpUtil {
    private final String key_wel = "sp_welcome";
    private final String keyLogU = "log_u";
    private final String keyLogP = "log_p";

    private SharedPreferences sp;

    public SpUtil(Context c) {
        sp = c.getApplicationContext().getSharedPreferences("atateacher",
                Context.MODE_PRIVATE);
    }

    public int getWelcomeNum() {
        return sp.getInt(key_wel, 0);
    }

    public void setWelcomeNum(int welcome) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key_wel, welcome);
        edit.commit();
    }

    public void setLogin(String user, String pwd) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(keyLogP, pwd);
        edit.putString(keyLogU, user);
        edit.commit();
    }

    public String getKeyLogU() {
        return sp.getString(keyLogU, "");
    }

    public String getKeyLogP() {
        return sp.getString(keyLogP, "");
    }

    /**
     * 清除登陆信息 isUser 是否清除用户名，是清除
     */
    public void cleanUser() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(keyLogP);
//        editor.remove(keyLogU);
        editor.commit();
    }

    public void setUser(BeUser beUser) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("user", new GsonUtil().getJsonElement(beUser).toString());
        edit.commit();
    }

    public BeUser getUser() {
        BeUser beUser = new GsonUtil().getJsonObject(sp.getString("user", ""), BeUser.class);
        if (beUser != null) return beUser;
        else return new BeUser();
    }
}
