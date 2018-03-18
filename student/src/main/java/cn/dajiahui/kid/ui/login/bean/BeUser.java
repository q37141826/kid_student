package cn.dajiahui.kid.ui.login.bean;

import com.fxtx.framework.text.StringUtil;

import java.util.ArrayList;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 用户对象
 */
public class BeUser extends BeanObj {
    /*登录*/
    private String token;
    private String user_id;
    private String user_type;
    private String nickname;
    private String gender;
    private String class_status;//加入班级的状态   0: 没任何班级及其申请  1:没有班级，但有班级申请,  2:已经有班级
    /*注册*/
    private String telnum;

    private BeUserThrid third;//解析第三方注册登录信息

    private String avatar;
    private String phone;
    private String realName;
    private String sex;
    private String userName;
    private String birthday;
    private String pwd;


    public BeUserThrid getThird() {
        return third;
    }

    public void setThird(BeUserThrid third) {
        this.third = third;
    }

    public void setAuthList(ArrayList<String> authList) {
        this.authList = authList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }


    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    private ArrayList<String> authList = new ArrayList<String>();

    public ArrayList<String> getAuthList() {
        return authList;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSignature() {
        return "";
    }


    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        if (StringUtil.isEmpty(realName))
            return getUserName();
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        if (StringUtil.isEmpty(userName))
            userName = "";
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(String objectId) {
        setObjectId(objectId);
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClass_status() {
        return class_status;
    }
}
