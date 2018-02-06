package cn.dajiahui.kid.ui.chat.bean;

import com.fxtx.framework.text.StringUtil;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by wdj on 2016/4/5.()
 * 学生端联系人列表
 */
public class BeConcact extends BeanObj {
    private String sex;
    private String phone;
    private String id;
    private String nickname;
    private String username;
    private String avatar;
    private String org_id;
    private String user_type;
    private String created_at;
    private String easemob_username;
    private String easemob_passwd;
    private String easemob_nikname;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEasemob_username() {
        return easemob_username;
    }

    public void setEasemob_username(String easemob_username) {
        this.easemob_username = easemob_username;
    }

    public String getEasemob_passwd() {
        return easemob_passwd;
    }

    public void setEasemob_passwd(String easemob_passwd) {
        this.easemob_passwd = easemob_passwd;
    }

    public String getEasemob_nikname() {
        return easemob_nikname;
    }

    public void setEasemob_nikname(String easemob_nikname) {
        this.easemob_nikname = easemob_nikname;
    }
}
