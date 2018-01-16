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

    /*注册*/
    private String telnum;

    private BeUserThrid third;//解析第三方注册登录信息

    private String avator;
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


    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
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

    @Override
    public String toString() {
        return "BeUser{" +
                "token='" + token + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", telnum='" + telnum + '\'' +
                ", third=" + third +
                ", avator='" + avator + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", sex='" + sex + '\'' +
                ", userName='" + userName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", pwd='" + pwd + '\'' +
                ", authList=" + authList +
                '}';
    }
}
